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
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.db.ext.NearCircleExt;
import com.hwl.beta.mq.send.NearCircleMessageSend;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.net.near.NearCircleService;
import com.hwl.beta.net.near.body.GetNearCircleDetailResponse;
import com.hwl.beta.net.near.body.SetNearLikeInfoResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.BaseActivity;
import com.hwl.beta.ui.common.KeyBoardAction;
import com.hwl.beta.ui.common.UITransfer;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBNearCircleAction;
import com.hwl.beta.ui.imgselect.ActivityImageBrowse;
import com.hwl.beta.ui.near.action.INearCircleCommentItemListener;
import com.hwl.beta.ui.near.action.INearCircleDetailListener;
import com.hwl.beta.ui.near.action.INearCircleItemListener;
import com.hwl.beta.ui.near.adp.NearCircleCommentAdapter;
import com.hwl.beta.ui.user.bean.ImageViewBean;
import com.hwl.beta.ui.widget.CircleActionMorePop;
import com.hwl.beta.ui.widget.MultiImageView;
import com.hwl.beta.utils.DisplayUtils;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityNearDetail extends BaseActivity {

    Activity activity;
    ActivityNearDetailBinding binding;
    long myUserId;
    NearCircleExt info;
    INearCircleDetailListener itemListener;
    NearCircleCommentAdapter commentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        myUserId = UserSP.getUserId();
        itemListener = new NearCircleDetailListener();
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_near_detail);
        binding.setAction(itemListener);

        initView();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addComment(NearCircleComment comment) {
        if (comment == null && comment.getNearCircleId() != info.getInfo().getNearCircleId())
            return;
        info.getComments().add(comment);
        commentAdapter.notifyItemInserted(info.getComments().size());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        binding.tbTitle.setTitle("附近动态详细")
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

        //数据源可能会从三个位置来，从本地数据库，根据id,序列化传进来
        long nearCircleId = getIntent().getLongExtra("nearcircleid", 0);
        info = (NearCircleExt) getIntent().getSerializableExtra("nearcircleext");
        if (info == null) {
            info = DaoUtils.getNearCircleManagerInstance().get(nearCircleId);
        }

        if (info == null) {
            info = new NearCircleExt(nearCircleId);
            binding.pbCircleLoading.setVisibility(View.VISIBLE);
            binding.svCircleContainer.setVisibility(View.GONE);
            this.loadFromServer(true);
        } else {
            binding.pbCircleLoading.setVisibility(View.GONE);
            binding.svCircleContainer.setVisibility(View.VISIBLE);
            bindData();
            this.loadFromServer(false);
        }

        this.setLikeViews(info.getLikes());
        commentAdapter = new NearCircleCommentAdapter(activity, info.getComments(), new NearCircleCommentItemListener());
        binding.rvComments.setAdapter(commentAdapter);
        binding.rvComments.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void bindData() {
        ImageViewBean.loadImage(binding.ivHeader, info.getInfo().getPublishUserImage());
        binding.tvUsername.setText(info.getInfo().getPublishUserName());
        binding.tvPosDesc.setText(info.getInfo().getFromPosDesc());
        binding.tvPublicTime.setText(info.getInfo().getShowTime());

        if (StringUtils.isBlank(info.getInfo().getContent())) {
            binding.tvContent.setVisibility(View.GONE);
        } else {
            binding.tvContent.setVisibility(View.VISIBLE);
            binding.tvContent.setText(info.getInfo().getContent());
        }

        if (info.getInfo().getPublishUserId() == myUserId) {
            binding.ivDelete.setVisibility(View.VISIBLE);
        } else {
            binding.ivDelete.setVisibility(View.GONE);
        }

        if (info.getImages() != null && info.getImages().size() > 0) {
            binding.mivImages.setImageListener(new MultiImageView.IMultiImageListener() {
                @Override
                public void onImageClick(int position, String imageUrl) {
                    itemListener.onImageClick(position);
                }
            });
            binding.mivImages.setImagesData(DBNearCircleAction.convertToMultiImages(info.getImages()));
            binding.mivImages.setVisibility(View.VISIBLE);
        } else {
            binding.mivImages.setVisibility(View.GONE);
        }
    }

    private void loadFromServer(final boolean isResetInfo) {
        NearCircleService.getNearCircleDetail(info.getInfo().getNearCircleId())
                .subscribe(new NetDefaultObserver<GetNearCircleDetailResponse>() {
                    @Override
                    protected void onSuccess(GetNearCircleDetailResponse response) {
                        List<NearCircleComment> comments = DBNearCircleAction.convertToNearCircleCommentInfos(response.getNearCircleInfo().getCommentInfos());
                        if (comments != null && comments.size() > 0) {
                            comments.removeAll(info.getComments());
                            info.getComments().addAll(comments);
                        }
                        info.setLikes(DBNearCircleAction.convertToNearCircleLikeInfos(response.getNearCircleInfo().getLikeInfos()));
                        if (response.getNearCircleInfo() != null) {
                            if (isResetInfo) {
                                info.setInfo(DBNearCircleAction.convertToNearCircleInfo(response.getNearCircleInfo()));
                                info.setImages(DBNearCircleAction.convertToNearCircleImageInfos(response.getNearCircleInfo().getNearCircleId(), response.getNearCircleInfo().getPublishUserId(), response.getNearCircleInfo().getImages()));
                                bindData();
                            } else {
                                if (response.getNearCircleInfo().getUpdateTime() != null && !response.getNearCircleInfo().getUpdateTime().equals(info.getInfo().getUpdateTime())) {
                                    info.getInfo().setUpdateTime(response.getNearCircleInfo().getUpdateTime());
                                    setLikeViews(info.getLikes());
                                    commentAdapter.notifyItemRangeChanged(0, info.getComments().size());
                                }
                            }
                            saveInfo(response.getNearCircleInfo().getUpdateTime());
                        } else {
                            Toast.makeText(activity, "数据已经被用户删除!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        binding.pbCircleLoading.setVisibility(View.GONE);
                        binding.svCircleContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void onError(String resultMessage) {
                        super.onError(resultMessage);
                        binding.pbCircleLoading.setVisibility(View.GONE);
                        binding.svCircleContainer.setVisibility(View.GONE);
                    }
                });
    }

    private void saveInfo(String lastUpdateTime) {
        //如果没有新的更新就不保存
        if (StringUtils.isBlank(lastUpdateTime) || lastUpdateTime.equals(info.getInfo().getUpdateTime()))
            return;
        //只存在我发布的信息
        if (info != null && info.getInfo() != null && info.getInfo().getPublishUserId() == myUserId) {
            DaoUtils.getNearCircleManagerInstance().save(info.getInfo());
            DaoUtils.getNearCircleManagerInstance().deleteImages(info.getInfo().getNearCircleId());
            DaoUtils.getNearCircleManagerInstance().deleteComments(info.getInfo().getNearCircleId());
            DaoUtils.getNearCircleManagerInstance().deleteLikes(info.getInfo().getNearCircleId());
            DaoUtils.getNearCircleManagerInstance().saveImages(info.getInfo().getNearCircleId(), info.getImages());
            DaoUtils.getNearCircleManagerInstance().saveComments(info.getInfo().getNearCircleId(), info.getComments());
            DaoUtils.getNearCircleManagerInstance().saveLikes(info.getInfo().getNearCircleId(), info.getLikes());
        }
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

    private class NearCircleDetailListener implements INearCircleDetailListener {

        private CircleActionMorePop mMorePopupWindow;
        boolean isRuning = false;

        @Override
        public void onItemViewClick(View view) {
            KeyBoardAction.hideSoftInput(view);
        }

        @Override
        public void onUserHeadClick() {
            UITransfer.toUserIndexActivity(activity, info.getInfo().getPublishUserId(), info.getInfo().getPublishUserName(), info.getInfo().getPublishUserImage());
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
                UITransfer.toNearCommentPublishActivity(activity, comment.getNearCircleId(), info.getInfo().getPublishUserId(), info.getNearCircleMessageContent());
            } else {
                UITransfer.toNearCommentPublishActivity(activity, comment.getNearCircleId(), info.getInfo().getPublishUserId(), comment.getCommentUserId(), comment.getCommentUserName(), info.getNearCircleMessageContent());
            }
        }

        @Override
        public void onContentClick() {

        }

        @Override
        public void onMoreCommentClick() {

        }

        @Override
        public void onMoreActionClick(final View view) {
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
                    setLikeInfo(position);
                }
            });
            mMorePopupWindow.show(0, view, info.getInfo().getIsLiked());
        }

        private void setLikeInfo(final int position) {
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
                                    NearCircleMessageSend.sendDeleteLikeMessage(info.getInfo().getNearCircleId(), info.getInfo().getPublishUserId()).subscribe();
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
            UITransfer.toNearPublishActivity(activity);
        }

        @Override
        public void onImageClick(int position) {
            if (info.getImages() != null && info.getImages().size() > 0) {
                List<String> imageUrls = new ArrayList<>(info.getImages().size());
                for (int i = 0; i < info.getImages().size(); i++) {
                    imageUrls.add(info.getImages().get(i).getImageUrl());
                }
                UITransfer.toImageBrowseActivity(activity, ActivityImageBrowse.MODE_VIEW, position, imageUrls);
            }
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
