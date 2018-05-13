package com.hwl.beta.mq.bean;

import java.util.Date;

public class UserLogoutMessageBean {
    private long userId;
    private String token;
    private Date loginTime;
    private String hintContent;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getHintContent() {
        return hintContent;
    }

    public void setHintContent(String hintContent) {
        this.hintContent = hintContent;
    }
}
