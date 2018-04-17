package com.hwl.beta.ui.chat.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.SendVideoItemBinding;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;

/**
 * Created by Administrator on 2018/1/6.
 */

public class ChatMessageSendVideoViewHolder extends RecyclerView.ViewHolder {

    private SendVideoItemBinding itemBinding;

    public ChatMessageSendVideoViewHolder(SendVideoItemBinding itemBinding) {
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

    public SendVideoItemBinding getItemBinding() {
        return itemBinding;
    }
}
