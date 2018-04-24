package com.hwl.beta.ui.circle.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.CircleHeadItemBinding;
import com.hwl.beta.ui.circle.action.ICircleItemListener;
import com.hwl.beta.ui.user.bean.ImageViewBean;

public class CircleHeadItemViewHolder extends RecyclerView.ViewHolder {

    private CircleHeadItemBinding itemBinding;

    public CircleHeadItemViewHolder(CircleHeadItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(ICircleItemListener itemListener, ImageViewBean imageBean, String lifeNotes) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setImage(imageBean);
        this.itemBinding.setUserLifeNotes(lifeNotes);
    }

    public CircleHeadItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
