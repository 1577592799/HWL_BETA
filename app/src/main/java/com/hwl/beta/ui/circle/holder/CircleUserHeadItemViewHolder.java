package com.hwl.beta.ui.circle.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.CircleUserHeadItemBinding;
import com.hwl.beta.ui.user.bean.ImageViewBean;

public class CircleUserHeadItemViewHolder extends RecyclerView.ViewHolder {

    private CircleUserHeadItemBinding itemBinding;

    public CircleUserHeadItemViewHolder(CircleUserHeadItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(ImageViewBean imageBean,
                               String userName,
                               String lifeNotes) {
        this.itemBinding.setImage(imageBean);
        this.itemBinding.setUserName(userName);
        this.itemBinding.setUserLifeNotes(lifeNotes);
    }

    public CircleUserHeadItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
