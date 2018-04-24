package com.hwl.beta.ui.circle.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.CircleHeadItemBinding;
import com.hwl.beta.databinding.CircleIndexItemBinding;
import com.hwl.beta.databinding.CircleItemNullBinding;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.db.ext.CircleExt;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.action.ICircleItemListener;
import com.hwl.beta.ui.circle.holder.CircleHeadItemViewHolder;
import com.hwl.beta.ui.circle.holder.CircleIndexItemViewHolder;
import com.hwl.beta.ui.circle.holder.CircleItemNullViewHolder;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.ArrayList;
import java.util.List;

public class CircleIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CircleExt> circles;
    private ICircleItemListener itemListener;
    private LayoutInflater inflater;
    private long myUserId;

    public CircleIndexAdapter(Context context, List<CircleExt> circles, ICircleItemListener itemListener) {
        this.context = context;
        this.circles = circles;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
        myUserId = UserSP.getUserId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
            default:
                return new CircleItemNullViewHolder((CircleItemNullBinding) DataBindingUtil.inflate(inflater, R.layout.circle_item_null, parent, false));
            case 1:
                return new CircleHeadItemViewHolder((CircleHeadItemBinding) DataBindingUtil.inflate(inflater, R.layout.circle_head_item, parent, false));
            case 2:
                return new CircleIndexItemViewHolder((CircleIndexItemBinding) DataBindingUtil.inflate(inflater, R.layout.circle_index_item, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (circles.get(position).getCircleItemType()) {
            case CircleExt.CircleNullItem:
            default:
                return 0;
            case CircleExt.CircleHeadItem:
                return 1;
            case CircleExt.CircleIndexItem:
                return 2;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CircleExt info = circles.get(position);
        if (holder instanceof CircleItemNullViewHolder) {
            CircleItemNullViewHolder viewHolder = (CircleItemNullViewHolder) holder;
            viewHolder.setItemBinding(itemListener);
        } else if (holder instanceof CircleHeadItemViewHolder) {
            CircleHeadItemViewHolder viewHolder = (CircleHeadItemViewHolder) holder;
            viewHolder.setItemBinding(itemListener, new ImageViewBean(UserSP.getUserHeadImage(), UserSP.getUserCirclebackimage()), UserSP.getLifeNotes());
        } else if (holder instanceof CircleIndexItemViewHolder) {
            CircleIndexItemViewHolder viewHolder = (CircleIndexItemViewHolder) holder;
            viewHolder.setItemBinding(itemListener, position, new ImageViewBean(info.getInfo().getPublishUserImage()), info.getInfo(), info.getImages(), info.getLikes(), info.getComments());
        }
    }

    public void addComment(CircleComment comment) {
        if (comment == null || comment.getCircleId() <= 0 || comment.getCommentUserId() <= 0)
            return;

        int position = -1;
        List<CircleComment> comments = null;
        for (int i = 0; i < circles.size(); i++) {
            if (circles.get(i).getInfo().getCircleId() == comment.getCircleId()) {
                comments = circles.get(i).getComments();
                if (comments == null) {
                    comments = new ArrayList<>();
                    circles.get(i).setComments(comments);
                }
                position = i;
                break;
            }
        }


        comments.add(comment);
        if (position == -1) {
            notifyItemChanged(0);
        } else {
            notifyItemChanged(position);
        }
    }

    public void addLike(int position, CircleLike likeInfo) {
        CircleExt info = circles.get(position);
        if (info.getLikes() == null) {
            info.setLikes(new ArrayList<CircleLike>());
        }

        if (likeInfo == null) {
            //取消点赞
            info.getInfo().setIsLiked(false);
            for (int i = 0; i < info.getLikes().size(); i++) {
                if (info.getLikes().get(i).getLikeUserId() == myUserId) {
                    info.getLikes().remove(i);
                    notifyItemChanged(position);
                    break;
                }
            }
        } else {
            //点赞
            info.getInfo().setIsLiked(true);
            info.getLikes().add(info.getLikes().size(), likeInfo);
            notifyItemChanged(position);
        }
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }
}
