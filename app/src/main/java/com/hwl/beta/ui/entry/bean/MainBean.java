package com.hwl.beta.ui.entry.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.hwl.beta.BR;

/**
 * Created by Administrator on 2018/3/30.
 */

public class MainBean extends BaseObservable {
    private String chatMessageCountDesc;
    private String friendRequestCountDesc;

    public MainBean(String chatMessageCountDesc, String friendRequestCountDesc) {
        this.chatMessageCountDesc = chatMessageCountDesc;
        this.friendRequestCountDesc = friendRequestCountDesc;
    }

    @Bindable
    public String getChatMessageCountDesc() {
        return chatMessageCountDesc;
    }

    public void setChatMessageCountDesc(String chatMessageCountDesc) {
        this.chatMessageCountDesc = chatMessageCountDesc;
        notifyPropertyChanged(BR.chatMessageCountDesc);
    }

    @Bindable
    public String getFriendRequestCountDesc() {
        return friendRequestCountDesc;
    }

    public void setFriendRequestCountDesc(String friendRequestCountDesc) {
        this.friendRequestCountDesc = friendRequestCountDesc;
        notifyPropertyChanged(BR.friendRequestCountDesc);
    }

    public MainBean() {
    }


}
