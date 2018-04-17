package com.hwl.beta.ui.user.bean;

/**
 * Created by Administrator on 2018/4/2.
 */

public class UserIndexBean {

    private long userId;
    private String showName;
    private String userImage;
    private String symbol;
    private String remark;
    private String registerAddress;
    private boolean isFriend;

    public UserIndexBean() {
    }

    public UserIndexBean(long userId, String showName, String userImage) {
        this.userId = userId;
        this.showName = showName;
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRegisterAddress() {
        return registerAddress;
    }

    public void setRegisterAddress(String registerAddress) {
        this.registerAddress = registerAddress;
    }
}
