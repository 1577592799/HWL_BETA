package com.hwl.beta.mq.bean;

/**
 * Created by Administrator on 2018/2/4.
 */

public class FriendRequestBean {
    private long userId;
    private String userName;
    private String userHeadImage;
    private long friendId;
    private String remark;

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

    public String getUserHeadImage() {
        return userHeadImage;
    }

    public void setHeadImage(String userHeadImage) {
        this.userHeadImage = userHeadImage;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
