package com.hwl.beta.ui.near;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.hwl.beta.R;
import com.hwl.beta.databinding.FragmentNearBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.db.ext.NearCircleExt;
import com.hwl.beta.mq.send.NearCircleMessageSend;
import com.hwl.beta.mq.send.UserMessageSend;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.near.NearCircleService;
import com.hwl.beta.net.near.NetNearCircleInfo;
import com.hwl.beta.net.near.NetNearCircleMatchInfo;
import com.hwl.beta.net.near.body.DeleteNearCircleInfoResponse;
import com.hwl.beta.net.near.body.GetNearCircleInfosResponse;
import com.hwl.beta.net.near.body.SetNearLikeInfoResponse;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.busbean.EventBusConstant;
import com.hwl.beta.ui.busbean.EventUpdateFriendRemark;
import com.hwl.beta.ui.common.BaseFragment;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultFunction;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBNearCircleAction;
import com.hwl.beta.ui.dialog.LoadingDialog;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.near.action.INearCircleItemListener;
import com.hwl.beta.ui.near.adp.NearCircleAdapter;
import com.hwl.beta.ui.widget.CircleActionMorePop;
import com.hwl.beta.utils.NetworkUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/27.
 */

public class FragmentNear extends BaseFragment {

    FragmentNearBinding binding;
    Activity activity;
    List<NearCircleExt> nearCircles;
    NearCircleAdapter nearCircleAdapter;
    int pageCount = 15;
    long myUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        nearCircles = new ArrayList<>();
        nearCircleAdapter = new NearCircleAdapter(activity, nearCircles, new NearCircleItemListener());
        myUserId = UserSP.getUserId();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_near, container, false);

        initView();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return binding.getRoot();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addComment(NearCircleComment comment) {
        nearCircleAdapter.addComment(comment);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateMessage(Integer ebType) {
        if (ebType == EventBusConstant.EB_TYPE_NEAR_CIRCLE_MESSAGE_UPDATE) {
            int count = MessageCountSP.getNearCircleMessageCount();
            if (count > 0) {
                binding.llMessageTip.setVisibility(View.VISIBLE);
                binding.tvMessageCount.setText(count + "");
            } else {
                binding.llMessageTip.setVisibility(View.GONE);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateRemark(EventUpdateFriendRemark remark) {
        if (remark == null || remark.getFriendId() <= 0)
            return;

        DaoUtils.getNearCircleManagerInstance().updateNearCircleFriendList(nearCircles, remark.getFriendId(), new Function() {
            @Override
            public Object apply(Object o) throws Exception {
                nearCircleAdapter.notifyItemChanged((Integer) o);
                return o;
            }
        });
    }

    @Override
    protected void onFragmentFirstVisible() {
        binding.pbLoading.setVisibility(View.VISIBLE);
        loadNearCircleInfoFromLocal();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        binding.rvNearContainer.setAdapter(nearCircleAdapter);
        binding.rvNearContainer.setLayoutManager(new LinearLayoutManager(activity));
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadNearCircleInfoFromServer(0);
            }
        });
        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                loadNearCircleInfoFromServer(nearCircles.get(nearCircles.size() - 1).getInfo().getNearCircleId());
            }
        });

        binding.refreshLayout.setEnableLoadMore(false);
        binding.llMessageTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UITransfer.toNearMessagesActivity(activity);
            }
        });

        this.updateMessage(EventBusConstant.EB_TYPE_NEAR_CIRCLE_MESSAGE_UPDATE);
    }

    private void showResult() {
        if (nearCircles.size() <= 0) {
            nearCircles.add(0, new NearCircleExt(NetConstant.CIRCLE_CONTENT_NULL));
            binding.refreshLayout.setEnableLoadMore(false);
            nearCircleAdapter.notifyDataSetChanged();
        } else if (nearCircles.size() == 1 && nearCircles.get(0).getInfo().getContentType() == NetConstant.CIRCLE_CONTENT_NULL) {
            nearCircleAdapter.notifyDataSetChanged();
            binding.refreshLayout.setEnableLoadMore(false);
        } else {
            binding.refreshLayout.setEnableLoadMore(true);
        }
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
        binding.pbLoading.setVisibility(View.GONE);
    }

    private void removeEmptyView() {
        if (nearCircles.size() == 1 && nearCircles.get(0).getInfo().getContentType() == NetConstant.CIRCLE_CONTENT_NULL) {
            nearCircles.remove(0);
        }
    }

    private void autoRefreshOneTime() {
        if (NetworkUtils.isConnected()) {
            nearCircles.clear();
            binding.refreshLayout.autoRefresh();
        }
    }

    @SuppressLint("CheckResult")
    private void loadNearCircleInfoFromLocal() {
        Observable.just(1)
                .map(new Function<Integer, List<NearCircleExt>>() {
                    @Override
                    public List<NearCircleExt> apply(Integer page) throws Exception {
                        List<NearCircleExt> infos = DaoUtils.getNearCircleManagerInstance().getNearCircles(pageCount);
                        if (infos == null || infos.size() <= 0) return new ArrayList<>();
                        return infos;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NearCircleExt>>() {
                    @Override
                    public void accept(List<NearCircleExt> infos) throws Exception {
                        if (infos != null && infos.size() > 0) {
                            nearCircles.addAll(infos);
                            nearCircleAdapter.notifyDataSetChanged();
                        }
                        showResult();
                        autoRefreshOneTime();
                    }
                });
    }

    private void loadNearCircleInfoFromServer(final long minNearCircleId) {
        if (!NetworkUtils.isConnected()) {
            showResult();
            return;
        }

        List<NetNearCircleMatchInfo> circleMatchInfos = new ArrayList<>();
        if (minNearCircleId <= 0) {
            int length = nearCircles.size() > pageCount ? pageCount : nearCircles.size();
            for (int i = 0; i < length; i++) {
                if (nearCircles.get(i).getInfo() != null && nearCircles.get(i).getInfo().getNearCircleId() > 0) {
                    circleMatchInfos.add(new NetNearCircleMatchInfo(nearCircles.get(i).getInfo().getNearCircleId(), nearCircles.get(i).getInfo().getUpdateTime()));
                }
            }
        }

//        final Date startDate = new Date(System.currentTimeMillis());
        NearCircleService.getNearCircleInfos(minNearCircleId, pageCount, circleMatchInfos)
                .flatMap(new NetDefaultFunction<GetNearCircleInfosResponse, NetNearCircleInfo>() {
                    @Override
                    protected ObservableSource<NetNearCircleInfo> onSuccess(GetNearCircleInfosResponse response) {
//                        Date endDate = new Date(System.currentTimeMillis());
//                        long diff = endDate.getTime() - startDate.getTime();
//                        Log.d("FragmentNear", "拉取数据用时：" + diff + " ms");
                        if (response.getNearCircleInfos() != null && response.getNearCircleInfos().size() > 0) {
                            removeEmptyView();
                            return Observable.fromIterable(response.getNearCircleInfos());
                        }
                        return Observable.fromIterable(new ArrayList<NetNearCircleInfo>());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<NetNearCircleInfo, NearCircleExt>() {
                    @Override
                    public NearCircleExt apply(NetNearCircleInfo info) throws Exception {
//                        Date endDate = new Date(System.currentTimeMillis());
//                        long diff = endDate.getTime() - startDate.getTime();
//                        Log.d("FragmentNear", "map数据用时：" + diff + " ms");
                        NearCircleExt circleBean = new NearCircleExt();
                        if (info != null && info.getNearCircleId() > 0) {
                            circleBean.setInfo(DBNearCircleAction.convertToNearCircleInfo(info));
                            circleBean.setImages(DBNearCircleAction.convertToNearCircleImageInfos(info.getNearCircleId(), info.getPublishUserId(), info.getImages()));
                            circleBean.setComments(DBNearCircleAction.convertToNearCircleCommentInfos(info.getCommentInfos()));
                            circleBean.setLikes(DBNearCircleAction.convertToNearCircleLikeInfos(info.getLikeInfos()));
                            return circleBean;
                        }
                        return circleBean;
                    }
                })
                .doOnNext(new Consumer<NearCircleExt>() {
                    @Override
                    public void accept(NearCircleExt nearCircleExt) throws Exception {
                        if (nearCircleExt != null && nearCircleExt.getInfo() != null) {
                            DaoUtils.getNearCircleManagerInstance().save(nearCircleExt.getInfo());
                            DaoUtils.getNearCircleManagerInstance().deleteImages(nearCircleExt.getInfo().getNearCircleId());
                            DaoUtils.getNearCircleManagerInstance().deleteComments(nearCircleExt.getInfo().getNearCircleId());
                            DaoUtils.getNearCircleManagerInstance().deleteLikes(nearCircleExt.getInfo().getNearCircleId());
                            DaoUtils.getNearCircleManagerInstance().saveImages(nearCircleExt.getInfo().getNearCircleId(), nearCircleExt.getImages());
                            DaoUtils.getNearCircleManagerInstance().saveComments(nearCircleExt.getInfo().getNearCircleId(), nearCircleExt.getComments());
                            DaoUtils.getNearCircleManagerInstance().saveLikes(nearCircleExt.getInfo().getNearCircleId(), nearCircleExt.getLikes());
                        }
                    }
                })
                .subscribe(new Observer<NearCircleExt>() {

                    int updateCount = 0;
                    int insertCount = 0;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NearCircleExt nearCircleExt) {
                        if (nearCircleExt != null && nearCircleExt.getInfo() != null) {
                            boolean isExists = false;
                            for (int i = 0; i < nearCircles.size(); i++) {
                                if (nearCircles.get(i).getInfo().getNearCircleId() == nearCircleExt.getInfo().getNearCircleId()) {
                                    nearCircles.remove(i);
                                    nearCircles.add(i, nearCircleExt);
                                    updateCount++;
                                    isExists = true;
                                    break;
                                }
                            }
                            if (!isExists) {
                                if (minNearCircleId > 0) {
                                    nearCircles.add(nearCircleExt);
                                } else {
                                    nearCircles.add(insertCount, nearCircleExt);
                                }
                                updateCount++;
                                insertCount++;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        showResult();
                        if (NetConstant.RESPONSE_RELOGIN.equals(e.getMessage())) {
                            UITransfer.toReloginDialog((FragmentActivity) activity);
                        }
                    }

                    @Override
                    public void onComplete() {
//                        Date endDate = new Date(System.currentTimeMillis());
//                        long diff = endDate.getTime() - startDate.getTime();
//                        Log.d("FragmentNear", "设置数据用时：" + diff + " ms");
                        showResult();
                        if (updateCount > 0) {
                            nearCircleAdapter.notifyItemRangeChanged(0, nearCircles.size());
                        }

//                        if (updateCount > 0 || insertCount > 0) {
//                            isDataChange = true;
//                        }
                    }
                });
    }

    private class NearCircleItemListener implements INearCircleItemListener {

        private CircleActionMorePop mMorePopupWindow;
        boolean isRuning = false;

        private NearCircleExt getNearCircleInfo(long nearCircleId) {
            if (nearCircleId <= 0) return null;
            for (int i = 0; i < nearCircles.size(); i++) {
                if (nearCircles.get(i).getInfo() != null && nearCircles.get(i).getInfo().getNearCircleId() > 0 && nearCircles.get(i).getInfo().getNearCircleId() == nearCircleId) {
                    return nearCircles.get(i);
                }
            }
            return null;
        }

        @Override
        public void onItemViewClick(View view) {
            KeyBoardAction.hideSoftInput(view);
        }

        @Override
        public void onUserHeadClick(NearCircle info) {
            UITransfer.toUserIndexActivity(activity, info.getPublishUserId(), info.getPublishUserName(), info.getPublishUserImage());
        }

        @Override
        public void onLikeUserHeadClick(NearCircleLike likeInfo) {
            UITransfer.toUserIndexActivity(activity, likeInfo.getLikeUserId(), likeInfo.getLikeUserName(), likeInfo.getLikeUserImage());
        }

        @Override
        public void onCommentUserClick(NearCircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getCommentUserId(), comment.getCommentUserName(), comment.getCommentUserImage());
        }

        @Override
        public void onReplyUserClick(NearCircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getReplyUserId(), comment.getReplyUserName(), comment.getReplyUserImage());
        }

        @Override
        public void onCommentContentClick(NearCircleComment comment) {
            NearCircleExt currnetInfo = this.getNearCircleInfo(comment.getNearCircleId());
            if (currnetInfo == null) return;
            if (comment.getCommentUserId() == myUserId) {
                UITransfer.toNearCommentPublishActivity(activity, comment.getNearCircleId(), currnetInfo.getInfo().getPublishUserId(), currnetInfo.getNearCircleMessageContent());
            } else {
                UITransfer.toNearCommentPublishActivity(activity, comment.getNearCircleId(), currnetInfo.getInfo().getPublishUserId(), comment.getCommentUserId(), comment.getCommentUserName(), currnetInfo.getNearCircleMessageContent());
            }
        }

        @Override
        public void onContentClick(NearCircle info) {
//            UITransfer.toNearDetailActivity(activity, info.getNearCircleId());
        }

        @Override
        public void onMoreActionClick(final View view, int position) {
            final NearCircleExt info = nearCircles.get(position);
            if (info == null) return;
            if (mMorePopupWindow == null) {
                mMorePopupWindow = new CircleActionMorePop(activity);
            }
            mMorePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    view.setVisibility(View.VISIBLE);
                }
            });
            mMorePopupWindow.setActionMoreListener(new CircleActionMorePop.IActionMoreListener() {
                @Override
                public void onCommentClick(int position) {
                    UITransfer.toNearCommentPublishActivity(activity, info.getInfo().getNearCircleId(), info.getInfo().getPublishUserId(), info.getNearCircleMessageContent());
                }

                @Override
                public void onLikeClick(int position) {
                    setLikeInfo(position, info);
                }
            });
            mMorePopupWindow.show(position, view, info.getInfo().getIsLiked());
        }

        private void setLikeInfo(final int position, final NearCircleExt info) {
            if (isRuning || info == null || info.getInfo() == null || info.getInfo().getNearCircleId() <= 0)
                return;
            isRuning = true;
            final boolean isLiked = info.getInfo().getIsLiked();
            NearCircleService.setNearLikeInfo(isLiked ? 0 : 1, info.getInfo().getNearCircleId())
                    .subscribe(new NetDefaultObserver<SetNearLikeInfoResponse>() {
                        @Override
                        protected void onSuccess(SetNearLikeInfoResponse response) {
                            isRuning = false;
                            if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                                if (isLiked) {
                                    nearCircleAdapter.addLike(position, null);
                                    NearCircleMessageSend.sendDeleteLikeMessage(info.getInfo().getNearCircleId(), info.getInfo().getPublishUserId()).subscribe();
                                } else {
                                    NearCircleLike likeInfo = new NearCircleLike();
                                    likeInfo.setNearCircleId(info.getInfo().getNearCircleId());
                                    likeInfo.setLikeUserId(myUserId);
                                    likeInfo.setLikeUserName(UserSP.getUserName());
                                    likeInfo.setLikeUserImage(UserSP.getUserHeadImage());
                                    likeInfo.setLikeTime(new Date());
                                    nearCircleAdapter.addLike(position, likeInfo);
                                    NearCircleMessageSend.sendAddLikeMessage(info.getInfo().getNearCircleId(), info.getInfo().getPublishUserId(), info.getNearCircleMessageContent()).subscribe();
                                }
                            } else {
                                onError("操作失败");
                            }
                        }

                        @Override
                        protected void onError(String resultMessage) {
                            super.onError(resultMessage);
                            isRuning = false;
                        }
                    });
        }

        @Override
        public void onMoreCommentClick(NearCircle info) {

        }

        @Override
        public void onDeleteClick(final int position, final NearCircle info) {
            new AlertDialog.Builder(activity)
                    .setMessage("信息删除后,不能恢复,确认删除 ?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCircle(position, info.getNearCircleId());
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

        private void deleteCircle(final int position, final long nearCircleId) {
            if (isRuning) return;
            isRuning = true;
            LoadingDialog.show(activity);
            NearCircleService.deleteNearCircleInfo(nearCircleId)
                    .subscribe(new NetDefaultObserver<DeleteNearCircleInfoResponse>() {
                        @Override
                        protected void onSuccess(DeleteNearCircleInfoResponse response) {
                            if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                                DaoUtils.getNearCircleManagerInstance().delete(nearCircleId);
                                DaoUtils.getNearCircleManagerInstance().deleteImages(nearCircleId);
                                DaoUtils.getNearCircleManagerInstance().deleteComments(nearCircleId);
                                DaoUtils.getNearCircleManagerInstance().deleteLikes(nearCircleId);
                                nearCircleAdapter.remove(position);
                                LoadingDialog.hide();
                                isRuning = false;
                            } else {
                                onError("删除失败");
                            }
                        }

                        @Override
                        protected void onError(String resultMessage) {
                            super.onError(resultMessage);
                            isRuning = false;
                            LoadingDialog.hide();
                        }
                    });
        }

        @Override
        public void onPublishClick() {
            UITransfer.toNearPublishActivity(activity);
        }

        @Override
        public void onImageClick(int position, List<NearCircleImage> images) {
            if (images != null && images.size() > 0) {
                List<String> imageUrls = new ArrayList<>(images.size());
                for (int i = 0; i < images.size(); i++) {
                    imageUrls.add(images.get(i).getImageUrl());
                }
                UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW, position, imageUrls);
            }
        }
    }
}
