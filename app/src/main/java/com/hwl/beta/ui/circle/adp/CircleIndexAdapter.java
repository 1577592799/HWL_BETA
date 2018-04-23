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
import com.hwl.beta.db.ext.CircleExt;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.circle.action.ICircleItemListener;
import com.hwl.beta.ui.circle.holder.CircleHeadItemViewHolder;
import com.hwl.beta.ui.circle.holder.CircleIndexItemViewHolder;
import com.hwl.beta.ui.circle.holder.CircleItemNullViewHolder;

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
        if (holder instanceof CircleItemNullViewHolder) {
            CircleItemNullViewHolder viewHolder = (CircleItemNullViewHolder) holder;
            viewHolder.setItemBinding(itemListener);
        } else if (holder instanceof CircleHeadItemViewHolder) {

        } else if (holder instanceof CircleIndexItemViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }
}
