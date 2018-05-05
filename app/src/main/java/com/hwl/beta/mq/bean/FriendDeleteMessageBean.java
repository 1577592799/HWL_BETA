package com.hwl.beta.mq.bean;

public class FriendDeleteMessageBean {
    private long actionUserId;
    private long friendUserId;

    public long getActionUserId() {
        return actionUserId;
    }

    public void setActionUserId(long actionUserId) {
        this.actionUserId = actionUserId;
    }

    public long getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(long friendUserId) {
        this.friendUserId = friendUserId;
    }
}
