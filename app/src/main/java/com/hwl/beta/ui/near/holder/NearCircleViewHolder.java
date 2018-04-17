package com.hwl.beta.ui.near.holder;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hwl.beta.databinding.NearItemBinding;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleImage;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.ui.convert.DBNearCircleAction;
import com.hwl.beta.ui.near.action.INearCircleCommentItemListener;
import com.hwl.beta.ui.near.action.INearCircleItemListener;
import com.hwl.beta.ui.near.adp.NearCircleCommentAdapter;
import com.hwl.beta.ui.near.adp.NearCircleLikeAdapter;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/16.
 */

public class NearCircleViewHolder extends RecyclerView.ViewHolder {

    private NearItemBinding itemBinding;

    public NearCircleViewHolder(NearItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(final INearCircleItemListener itemListener,
                               int position,
                               NearCircle info,
                               List<NearCircleImage> images,
                               List<NearCircleLike> likes,
                               List<NearCircleComment> comments,
                               ImageViewBean headImage
    ) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setPosition(position);
        this.itemBinding.setInfo(info);
        this.itemBinding.setImage(headImage);

        if (images == null || images.size() <= 0) {
            this.itemBinding.mivImages.setVisibility(View.GONE);
        } else {
            this.itemBinding.mivImages.setVisibility(View.VISIBLE);
            this.itemBinding.mivImages.setImagesData(DBNearCircleAction.convertToMultiImages(images));
        }

        boolean isShowActionContainer = false;
        if (likes != null && likes.size() > 0) {
            isShowActionContainer = true;
            this.itemBinding.rlLikeContainer.setVisibility(View.VISIBLE);
            Context context = this.itemBinding.rvLikes.getContext();
            this.itemBinding.rvLikes.setAdapter(
                    new NearCircleLikeAdapter(
                            context,
                            likes,
                            new NearCircleLikeAdapter.ILikeItemListener() {
                                @Override
                                public void onLikeUserClick(NearCircleLike like) {
                                    itemListener.onLikeUserHeadClick(like);
                                }
                            })
            );
            this.itemBinding.rvLikes.setLayoutManager(new GridLayoutManager(context, 5));
        } else {
            this.itemBinding.rlLikeContainer.setVisibility(View.GONE);
        }
        if (comments != null && comments.size() > 0) {
            isShowActionContainer = true;
            this.itemBinding.rlCommentContainer.setVisibility(View.VISIBLE);
            Context context = this.itemBinding.rvComments.getContext();
            this.itemBinding.rvComments.setVisibility(View.VISIBLE);
            this.itemBinding.rvComments.setAdapter(
                    new NearCircleCommentAdapter(
                            context,
                            comments,
                            new INearCircleCommentItemListener() {
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
                    )
            );
            this.itemBinding.rvComments.setLayoutManager(new LinearLayoutManager(context));
        } else {
            this.itemBinding.rlCommentContainer.setVisibility(View.GONE);
        }
        if (isShowActionContainer) {
            this.itemBinding.llActionContainer.setVisibility(View.VISIBLE);
        } else {
            this.itemBinding.llActionContainer.setVisibility(View.GONE);
        }
    }

    public NearItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
