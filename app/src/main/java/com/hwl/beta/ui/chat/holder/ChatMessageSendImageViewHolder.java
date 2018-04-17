package com.hwl.beta.ui.chat.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.SendImageItemBinding;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;

/**
 * Created by Administrator on 2018/1/6.
 */

public class ChatMessageSendImageViewHolder extends RecyclerView.ViewHolder {

    private SendImageItemBinding itemBinding;

    public ChatMessageSendImageViewHolder(SendImageItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(IChatMessageItemListener itemListener,
                               ChatImageViewBean image,
                               int position) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setImage(image);
        this.itemBinding.setPosition(position);
    }

    public SendImageItemBinding getItemBinding() {
        return itemBinding;
    }
}
