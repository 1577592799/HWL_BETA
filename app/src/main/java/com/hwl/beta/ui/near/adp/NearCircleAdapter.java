package com.hwl.beta.ui.near.adp;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwl.beta.R;
import com.hwl.beta.databinding.NearItemBinding;
import com.hwl.beta.databinding.NearItemNullBinding;
import com.hwl.beta.db.ext.NearCircleExt;
import com.hwl.beta.net.NetConstant;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.near.action.INearCircleItemListener;
import com.hwl.beta.ui.near.holder.NearCircleNullViewHolder;
import com.hwl.beta.ui.near.holder.NearCircleViewHolder;
import com.hwl.beta.ui.user.bean.ImageViewBean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/16.
 */

public class NearCircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<NearCircleExt> nearCircles;
    INearCircleItemListener itemListener;
    LayoutInflater inflater;
    long myUserId;

    public NearCircleAdapter(Context context, List<NearCircleExt> nearCircles, INearCircleItemListener itemListener) {
        this.context = context;
        this.nearCircles = nearCircles;
        this.itemListener = itemListener;
        inflater = LayoutInflater.from(context);
        myUserId = UserSP.getUserId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new NearCircleNullViewHolder((NearItemNullBinding) DataBindingUtil.inflate(inflater, R.layout.near_item_null, parent, false));
        } else {
            return new NearCircleViewHolder((NearItemBinding) DataBindingUtil.inflate(inflater, R.layout.near_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NearCircleExt info = nearCircles.get(position);
        if (holder instanceof NearCircleNullViewHolder) {
            NearCircleNullViewHolder viewHolder = (NearCircleNullViewHolder) holder;
            viewHolder.setItemBinding(itemListener);
        } else if (holder instanceof NearCircleViewHolder) {
            NearCircleViewHolder viewHolder = (NearCircleViewHolder) holder;
            viewHolder.setItemBinding(itemListener,
                    position,
                    info.getInfo(),
                    info.getImages(),
                    info.getLikes(),
                    info.getComments(),
                    new ImageViewBean(info.getInfo().getPublishUserImage())
            );

            if (info.getInfo().getPublishUserId() == myUserId) {
                viewHolder.getItemBinding().ivDelete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.getItemBinding().ivDelete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return nearCircles.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (nearCircles.get(position).getInfo().getContentType() == NetConstant.CIRCLE_CONTENT_NULL)
            return 0;
        return 1;
    }
}
