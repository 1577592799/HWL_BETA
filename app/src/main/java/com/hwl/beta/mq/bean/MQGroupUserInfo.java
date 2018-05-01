package com.hwl.beta.mq.bean;

public class MQGroupUserInfo {
    private long userId;
    private String userName;
    private String userImage;

    public MQGroupUserInfo(long userId, String userName, String userImage) {
        this.userId = userId;
        this.userName = userName;
        this.userImage = userImage;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
