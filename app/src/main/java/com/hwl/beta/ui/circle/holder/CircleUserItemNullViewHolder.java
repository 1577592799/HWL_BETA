package com.hwl.beta.ui.circle.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.CircleUserItemNullBinding;
import com.hwl.beta.ui.circle.action.ICircleUserItemListener;

public class CircleUserItemNullViewHolder extends RecyclerView.ViewHolder {

    private CircleUserItemNullBinding itemBinding;

    public CircleUserItemNullViewHolder(CircleUserItemNullBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(ICircleUserItemListener itemListener) {
        this.itemBinding.setAction(itemListener);
    }

    public CircleUserItemNullBinding getItemBinding() {
        return this.itemBinding;
    }
}
