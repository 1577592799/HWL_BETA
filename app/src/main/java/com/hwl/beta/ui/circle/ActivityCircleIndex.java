package com.hwl.beta.ui.circle;

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
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityCircleIndexBinding;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.db.ext.CircleExt;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.circle.CircleService;
import com.hwl.beta.net.circle.NetCircleInfo;
import com.hwl.beta.net.circle.body.GetCircleInfosResponse;
import com.hwl.beta.net.circle.body.SetLikeInfoResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.busbean.EventActionCircleComment;
import com.hwl.beta.ui.busbean.EventActionCircleLike;
import com.hwl.beta.ui.busbean.EventBusConstant;
import com.hwl.beta.ui.circle.action.ICircleItemListener;
import com.hwl.beta.ui.circle.adp.CircleIndexAdapter;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultFunction;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBCircleAction;
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

public class ActivityCircleIndex extends FragmentActivity {

    Activity activity;
    ActivityCircleIndexBinding binding;
    List<CircleExt> circles;
    CircleIndexAdapter circleAdapter;
    boolean isDataChange = false;
    int pageCount = 15;
    long myUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        myUserId = UserSP.getUserId();
        circles = this.getCircles();
        circleAdapter = new CircleIndexAdapter(activity, circles, new CircleItemListener());

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_circle_index);
        initView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addComment(EventActionCircleComment action) {
        if (action.getActionType() == EventBusConstant.EB_TYPE_ACTINO_ADD) {
            circleAdapter.addComment(action.getComment());
        } else if (action.getActionType() == EventBusConstant.EB_TYPE_ACTINO_REMOVE) {
            circleAdapter.removeComment(action.getComment());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addLike(EventActionCircleLike action) {
        if (action.getActionType() == EventBusConstant.EB_TYPE_ACTINO_ADD) {
            circleAdapter.addLike(action.getLike());
        } else if (action.getActionType() == EventBusConstant.EB_TYPE_ACTINO_REMOVE) {
            circleAdapter.removeLike(action.getLike());
        }
    }

    private void initView() {
        binding.tbTitle.setTitle("我的圈子")
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UITransfer.toCirclePublishActivity(activity);
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        binding.rvCircleContainer.setAdapter(circleAdapter);
        binding.rvCircleContainer.setLayoutManager(new LinearLayoutManager(activity));
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadCircleFromServer(0);
            }
        });
        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                loadCircleFromServer(circles.get(circles.size() - 1).getInfo().getCircleId());
            }
        });

        binding.refreshLayout.autoRefresh();
        binding.refreshLayout.setEnableLoadMore(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveInfo();
    }

    private void saveInfo() {
        if (!isDataChange || circles == null || circles.size() <= 0) return;
        Log.d("ActivityCircleIndex", "save circle info");
        isDataChange = false;
        Observable.fromIterable(circles.subList(0, (pageCount > circles.size() ? circles.size() : pageCount)))
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<CircleExt>() {
                    @Override
                    public void accept(CircleExt circleExt) throws Exception {
                        if (circleExt != null && circleExt.getInfo() != null) {
                            DaoUtils.getCircleManagerInstance().save(circleExt.getInfo());
                            DaoUtils.getCircleManagerInstance().deleteImages(circleExt.getInfo().getCircleId());
                            DaoUtils.getCircleManagerInstance().deleteComments(circleExt.getInfo().getCircleId());
                            DaoUtils.getCircleManagerInstance().deleteLikes(circleExt.getInfo().getCircleId());
                            DaoUtils.getCircleManagerInstance().saveImages(circleExt.getInfo().getCircleId(), circleExt.getImages());
                            DaoUtils.getCircleManagerInstance().saveComments(circleExt.getInfo().getCircleId(), circleExt.getComments());
                            DaoUtils.getCircleManagerInstance().saveLikes(circleExt.getInfo().getCircleId(), circleExt.getLikes());
                        }
                    }
                })
                .subscribe();
    }

    private List<CircleExt> getCircles() {
        List<CircleExt> infos = DaoUtils.getCircleManagerInstance().getAll();
        if (infos == null) {
            infos = new ArrayList<>();
        }
        infos.add(0, new CircleExt(CircleExt.CircleHeadItem));
        if (infos.size() == 1) {
            infos.add(new CircleExt(CircleExt.CircleNullItem));
        }
        return infos;
    }

    private void showResult() {
        if (circles.size() == 2 && circles.get(1).getCircleItemType() == CircleExt.CircleNullItem) {
            circleAdapter.notifyDataSetChanged();
            binding.refreshLayout.setEnableLoadMore(false);
        } else {
            binding.refreshLayout.setEnableLoadMore(true);
        }
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
//        binding.pbLoading.setVisibility(View.GONE);
    }

    private void removeEmptyView() {
        if (circles.size() == 2 && circles.get(1).getCircleItemType() == CircleExt.CircleNullItem) {
            circles.remove(1);
        }
    }

    private void loadCircleFromServer(final long minCircleId) {
        if (!NetworkUtils.isConnected()) {
            showResult();
            return;
        }
        CircleService.getCircleInfos(minCircleId, pageCount)
                .flatMap(new NetDefaultFunction<GetCircleInfosResponse, NetCircleInfo>() {
                    @Override
                    protected ObservableSource<NetCircleInfo> onSuccess(GetCircleInfosResponse response) {
                        if (response.getCircleInfos() != null && response.getCircleInfos().size() > 0) {
                            removeEmptyView();
                            return Observable.fromIterable(response.getCircleInfos());
                        }
                        return Observable.fromIterable(new ArrayList<NetCircleInfo>());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<NetCircleInfo, CircleExt>() {
                    @Override
                    public CircleExt apply(NetCircleInfo info) throws Exception {
                        CircleExt circleBean = new CircleExt(CircleExt.CircleIndexItem);
                        if (info != null && info.getCircleId() > 0) {
                            circleBean.setInfo(DBCircleAction.convertToCircleInfo(info));
                            circleBean.setImages(DBCircleAction.convertToCircleImageInfos(info.getCircleId(), info.getPublishUserId(), info.getImages()));
                            circleBean.setComments(DBCircleAction.convertToCircleCommentInfos(info.getCommentInfos()));
                            circleBean.setLikes(DBCircleAction.convertToCircleLikeInfos(info.getLikeInfos()));
                            return circleBean;
                        }
                        return circleBean;
                    }
                })
                .subscribe(new Observer<CircleExt>() {

                    int updateCount = 0;
                    int insertCount = 1;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CircleExt circleExt) {
                        if (circleExt != null && circleExt.getInfo() != null) {
                            boolean isExists = false;
                            for (int i = 0; i < circles.size(); i++) {
                                if (circles.get(i).getCircleItemType() == CircleExt.CircleIndexItem &&
                                        circles.get(i).getInfo().getCircleId() == circleExt.getInfo().getCircleId()) {
                                    circles.remove(i);
                                    circles.add(i, circleExt);
                                    updateCount++;
                                    isExists = true;
                                    break;
                                }
                            }
                            if (!isExists) {
                                if (minCircleId > 0) {
                                    circles.add(circleExt);
                                } else {
                                    circles.add(insertCount, circleExt);
                                }
                                updateCount++;
                                insertCount++;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        showResult();
                    }

                    @Override
                    public void onComplete() {
                        showResult();
                        if (updateCount > 0) {
                            circleAdapter.notifyItemRangeChanged(0, circles.size());
                        }

                        if (updateCount > 0 || insertCount > 0) {
                            isDataChange = true;
                        }
                    }
                });
    }

    private class CircleItemListener implements ICircleItemListener {

        private CircleActionMorePop mMorePopupWindow;
        boolean isRuning = false;

        @Override
        public void onItemViewClick(View view) {
            KeyBoardAction.hideSoftInput(view);
        }

        @Override
        public void onMyUserHeadClick() {
            UITransfer.toCircleUserIndexActivity(activity, UserSP.getUserId(), UserSP.getUserName(), UserSP.getUserHeadImage(), UserSP.getUserCirclebackimage(), UserSP.getLifeNotes());
        }

        @Override
        public void onUserHeadClick(Circle info) {
            UITransfer.toUserIndexActivity(activity, info.getPublishUserId(), info.getPublishUserName(), info.getPublishUserImage());
        }

        @Override
        public void onLikeUserHeadClick(CircleLike likeInfo) {
            UITransfer.toUserIndexActivity(activity, likeInfo.getLikeUserId(), likeInfo.getLikeUserName(), likeInfo.getLikeUserImage());
        }

        @Override
        public void onCommentUserClick(CircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getCommentUserId(), comment.getCommentUserName(), comment.getCommentUserImage());
        }

        @Override
        public void onReplyUserClick(CircleComment comment) {
            UITransfer.toUserIndexActivity(activity, comment.getReplyUserId(), comment.getReplyUserName(), comment.getReplyUserImage());
        }

        @Override
        public void onCommentContentClick(CircleComment comment) {
            if (comment.getCommentUserId() == myUserId) {
                UITransfer.toCircleCommentPublishActivity(activity, comment.getCircleId());
            } else {
                UITransfer.toCircleCommentPublishActivity(activity, comment.getCircleId(), comment.getCommentUserId(), comment.getCommentUserName());
            }
        }

        @Override
        public void onContentClick() {

        }

        @Override
        public void onMoreActionClick(final View view, int position) {
            final CircleExt info = circles.get(position);
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
                    UITransfer.toCircleCommentPublishActivity(activity, info.getInfo().getCircleId());
                }

                @Override
                public void onLikeClick(int position) {
                    setLikeInfo(position, info);
                }
            });
            mMorePopupWindow.show(position, view, info.getInfo().getIsLiked());
        }

        private void setLikeInfo(final int position, final CircleExt info) {
            if (isRuning || info == null || info.getInfo() == null || info.getInfo().getCircleId() <= 0)
                return;
            isRuning = true;
            final boolean isLiked = info.getInfo().getIsLiked();
            CircleService.setLikeInfo(isLiked ? 0 : 1, info.getInfo().getCircleId())
                    .subscribe(new NetDefaultObserver<SetLikeInfoResponse>() {
                        @Override
                        protected void onSuccess(SetLikeInfoResponse response) {
                            isRuning = false;
                            if (response.getStatus() == NetConstant.RESULT_SUCCESS) {
                                if (isLiked) {
                                    circleAdapter.addLike(position, null);
                                } else {
                                    CircleLike likeInfo = new CircleLike();
                                    likeInfo.setCircleId(info.getInfo().getCircleId());
                                    likeInfo.setLikeUserId(myUserId);
                                    likeInfo.setLikeUserName(UserSP.getUserName());
                                    likeInfo.setLikeUserImage(UserSP.getUserHeadImage());
                                    likeInfo.setLikeTime(new Date());
                                    circleAdapter.addLike(position, likeInfo);
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
        public void onMoreCommentClick() {

        }

        @Override
        public void onDeleteClick() {
            new AlertDialog.Builder(activity)
                    .setMessage("信息删除后,不能恢复,确认删除 ?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCircle();
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }

        private void deleteCircle() {
        }

        @Override
        public void onPublishClick() {
            UITransfer.toCirclePublishActivity(activity);
        }
    }
}
