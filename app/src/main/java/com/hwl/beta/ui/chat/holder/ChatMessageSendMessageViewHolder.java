package com.hwl.beta.ui.chat.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.SendMessageItemBinding;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;

/**
 * Created by Administrator on 2018/1/6.
 */

public class ChatMessageSendMessageViewHolder extends RecyclerView.ViewHolder {

    private SendMessageItemBinding itemBinding;

    public ChatMessageSendMessageViewHolder(SendMessageItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(IChatMessageItemListener itemListener,
                               ChatImageViewBean image,
                               int position,
                               String content) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setImage(image);
        this.itemBinding.setPosition(position);
        this.itemBinding.setContent(content);
    }

    public SendMessageItemBinding getItemBinding() {
        return itemBinding;
    }
}
