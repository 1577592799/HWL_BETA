package com.hwl.beta.ui.circle.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.CircleIndexItemBinding;
import com.hwl.beta.ui.circle.action.ICircleItemListener;

public class CircleIndexItemViewHolder extends RecyclerView.ViewHolder {

    private CircleIndexItemBinding itemBinding;

    public CircleIndexItemViewHolder(CircleIndexItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(ICircleItemListener itemListener) {
        this.itemBinding.setAction(itemListener);
    }

    public CircleIndexItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
