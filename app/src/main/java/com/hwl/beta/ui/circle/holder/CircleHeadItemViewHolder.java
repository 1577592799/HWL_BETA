package com.hwl.beta.ui.circle.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.CircleHeadItemBinding;
import com.hwl.beta.ui.circle.action.ICircleItemListener;

public class CircleHeadItemViewHolder extends RecyclerView.ViewHolder {

    private CircleHeadItemBinding itemBinding;

    public CircleHeadItemViewHolder(CircleHeadItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(ICircleItemListener itemListener) {
        this.itemBinding.setAction(itemListener);
    }

    public CircleHeadItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
