package com.hwl.beta.ui.near;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.hwl.beta.R;
import com.hwl.beta.databinding.ActivityNearDetailBinding;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.db.ext.NearCircleExt;
import com.hwl.beta.mq.send.UserMessageSend;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.near.NearCircleService;
import com.hwl.beta.net.near.body.SetNearLikeInfoResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBNearCircleAction;
import com.hwl.beta.ui.near.action.INearCircleCommentItemListener;
import com.hwl.beta.ui.near.action.INearCircleItemListener;
import com.hwl.beta.ui.near.adp.NearCircleCommentAdapter;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.ui.widget.CircleActionMorePop;
import com.hwl.beta.utils.DisplayUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityNearDetail extends FragmentActivity {

    Activity activity;
    ActivityNearDetailBinding binding;
    long myUserId;
    NearCircleExt info;
    INearCircleItemListener itemListener;
    NearCircleCommentAdapter commentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        myUserId = UserSP.getUserId();

        info = (NearCircleExt) getIntent().getSerializableExtra("nearcircleext");
        if (info == null || info.getInfo() == null) {
            Toast.makeText(activity, "动态不存在", Toast.LENGTH_SHORT).show();
            finish();
        }

        itemListener = new NearCircleItemListener();
        if (info.getComments() == null) {
            info.setComments(new ArrayList<NearCircleComment>());
        }
        commentAdapter = new NearCircleCommentAdapter(activity, info.getComments(), new NearCircleCommentItemListener());
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_near_detail);
        binding.setImage(new ImageViewBean(info.getInfo().getPublishUserImage()));
        binding.setInfo(info.getInfo());
        binding.setAction(itemListener);
        initView();

//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void addComment(EventActionNearCircleComment action) {
//        if (action.getActionType() != EventBusConstant.EB_TYPE_ACTINO_ADD) return;
//        NearCircleComment comment = action.getComment();
//        if (comment == null || comment.getCircleId() <= 0 || comment.getCommentUserId() <= 0)
//            return;
//
//        int position = info.getComments().size();
//        info.getComments().add(position, comment);
//        commentAdapter.notifyItemInserted(position);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        binding.tbTitle.setTitle("动态详细")
                .setImageRightClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                .setImageLeftClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

        if (info.getImages() != null && info.getImages().size() > 0) {
            binding.mivImages.setImagesData(DBNearCircleAction.convertToMultiImages(info.getImages()));
            binding.mivImages.setVisibility(View.VISIBLE);
        } else {
            binding.mivImages.setVisibility(View.GONE);
        }

        this.setLikeViews(info.getLikes());
        binding.rvComments.setAdapter(commentAdapter);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void setLikeView(final NearCircleLike likeInfo) {
        if (likeInfo == null) {
            return;
        }
        int size = DisplayUtils.dp2px(activity, 25);
        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(size, size);
        params.rightMargin = 2;
        params.bottomMargin = 2;
        ImageView ivLikeUser = new ImageView(activity);
        ImageViewBean.loadImage(ivLikeUser, likeInfo.getLikeUserImage());
        ivLikeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onLikeUserHeadClick(likeInfo);
            }
        });
        binding.fblLikeContainer.addView(ivLikeUser, params);
    }

    private void setLikeViews(final List<NearCircleLike> likes) {
        if (likes == null || likes.size() <= 0) return;
        binding.fblLikeContainer.removeAllViews();
        for (int i = 0; i < likes.size(); i++) {
            setLikeView(likes.get(i));
        }
    }

    private void removeLikeView() {
        if (info == null || info.getLikes() == null || info.getLikes().size() <= 0)
            return;
        for (int i = 0; i < info.getLikes().size(); i++) {
            if (info.getLikes().get(i).getLikeUserId() == myUserId) {
//                EventBus.getDefault().post(new EventActionCircleLike(EventBusConstant.EB_TYPE_ACTINO_REMOVE, info.getLikes().get(i)));
                info.getLikes().remove(i);
                binding.fblLikeContainer.removeViewAt(i);
                break;
            }
        }
    }

    //    private void loadFromServer() {
//        if (!NetworkUtils.isConnected()) {
//            return;
//        }
//        CircleService.getCircleDetail(info.getInfo().getCircleId())
//                .subscribe(new NetDefaultObserver<GetCircleDetailResponse>() {
//                    @Override
//                    protected void onSuccess(GetCircleDetailResponse response) {
//                        if (response.getCircleInfo() != null) {
//                            CircleExt circleBean = new CircleExt(CircleExt.CircleIndexItem);
//                            circleBean.setInfo(DBCircleAction.convertToCircleInfo(response.getCircleInfo()));
//                            circleBean.setImages(DBCircleAction.convertToCircleImageInfos(response.getCircleInfo().getCircleId(), response.getCircleInfo().getPublishUserId(), response.getCircleInfo().getImages()));
//                            circleBean.setComments(DBCircleAction.convertToCircleCommentInfos(response.getCircleInfo().getCommentInfos()));
//                            circleBean.setLikes(DBCircleAction.convertToCircleLikeInfos(response.getCircleInfo().getLikeInfos()));
//                            info = circleBean;
//                            initView();
//                        }
//                    }
//                });
//    }
    private class NearCircleItemListener implements INearCircleItemListener {

        private CircleActionMorePop mMorePopupWindow;
        boolean isRuning = false;

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
            if (comment.getCommentUserId() == myUserId) {
                UITransfer.toNearCommentPublishActivity(activity, comment.getNearCircleId());
            } else {
                UITransfer.toNearCommentPublishActivity(activity, comment.getNearCircleId(), comment.getCommentUserId(), comment.getCommentUserName());
            }
        }

        @Override
        public void onContentClick(NearCircle info) {

        }

        @Override
        public void onMoreActionClick(final View view, int position) {
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
                    UITransfer.toNearCommentPublishActivity(activity, info.getInfo().getNearCircleId());
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
                                    //取消点赞
                                    info.getInfo().setIsLiked(false);
                                    removeLikeView();
                                } else {
                                    //点赞
                                    info.getInfo().setIsLiked(true);
                                    NearCircleLike likeInfo = new NearCircleLike();
                                    likeInfo.setNearCircleId(info.getInfo().getNearCircleId());
                                    likeInfo.setLikeUserId(myUserId);
                                    likeInfo.setLikeUserName(UserSP.getUserName());
                                    likeInfo.setLikeUserImage(UserSP.getUserHeadImage());
                                    likeInfo.setLikeTime(new Date());
                                    if (info.getLikes() == null) {
                                        info.setLikes(new ArrayList<NearCircleLike>());
                                    }
                                    info.getLikes().add(likeInfo);
                                    setLikeView(likeInfo);
//                                    EventBus.getDefault().post(new EventActionCircleLike(EventBusConstant.EB_TYPE_ACTINO_ADD, likeInfo));
                                    UserMessageSend.sendNearCircleLikeMessage(info.getInfo().getPublishUserId(), info.getNearCircleMessageContent()).subscribe();
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
        public void onDeleteClick(NearCircle info) {
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
            UITransfer.toNearPublishActivity(activity);
        }
    }

    private class NearCircleCommentItemListener implements INearCircleCommentItemListener {
        @Override
        public void onCommentUserClick(NearCircleComment comment) {
            itemListener.onCommentUserClick(comment);
        }

        @Override
        public void onReplyUserClick(NearCircleComment comment) {
            itemListener.onReplyUserClick(comment);
        }

        @Override
        public void onContentClick(NearCircleComment comment) {
            itemListener.onCommentContentClick(comment);
        }
    }
}
