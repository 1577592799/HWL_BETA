package com.hwl.beta.ui.circle.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.CircleUserIndexItemBinding;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleImage;
import com.hwl.beta.ui.circle.action.ICircleUserItemListener;

import java.util.List;

public class CircleUserIndexItemViewHolder extends RecyclerView.ViewHolder {

    private CircleUserIndexItemBinding itemBinding;

    public CircleUserIndexItemViewHolder(CircleUserIndexItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(String timeYear,
                               String timeMonth,
                               String timeDay,
                               String content,
                               List<CircleImage> images) {
        this.itemBinding.setTimeYear(timeYear);
        this.itemBinding.setTimeMonth(timeMonth);
        this.itemBinding.setTimeDay(timeDay);
        this.itemBinding.setContent(content);
    }

    public CircleUserIndexItemBinding getItemBinding() {
        return this.itemBinding;
    }
}
