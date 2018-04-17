package com.hwl.beta.ui.chat.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.SendAudioItemBinding;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;

/**
 * Created by Administrator on 2018/1/6.
 */

public class ChatMessageSendAudioViewHolder extends RecyclerView.ViewHolder {

    private SendAudioItemBinding itemBinding;

    public ChatMessageSendAudioViewHolder(SendAudioItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(IChatMessageItemListener itemListener,
                               ChatImageViewBean image,
                               int position,
                               long playTime) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setImage(image);
        this.itemBinding.setPosition(position);
        this.itemBinding.setPlayTime(playTime);
    }

    public SendAudioItemBinding getItemBinding() {
        return itemBinding;
    }
}
