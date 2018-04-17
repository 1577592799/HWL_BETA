package com.hwl.beta.ui.chat.holder;

import android.support.v7.widget.RecyclerView;

import com.hwl.beta.databinding.ReceivedAudioItemBinding;
import com.hwl.beta.databinding.ReceivedVideoItemBinding;
import com.hwl.beta.ui.chat.action.IChatMessageItemListener;
import com.hwl.beta.ui.chat.bean.ChatImageViewBean;

/**
 * Created by Administrator on 2018/1/6.
 */

public class ChatMessageReceivedVideoViewHolder extends RecyclerView.ViewHolder {

    private ReceivedVideoItemBinding itemBinding;

    public ChatMessageReceivedVideoViewHolder(ReceivedVideoItemBinding itemBinding) {
        super(itemBinding.getRoot());
        this.itemBinding = itemBinding;
    }

    public void setItemBinding(IChatMessageItemListener itemListener,
                               ChatImageViewBean image,
                               int position,
                               String userName,
                               String showTime) {
        this.itemBinding.setAction(itemListener);
        this.itemBinding.setPosition(position);
        this.itemBinding.setShowTime(showTime);
        this.itemBinding.setUserName(userName);
        this.itemBinding.setImage(image);
    }

    public ReceivedVideoItemBinding getItemBinding() {
        return itemBinding;
    }
}
