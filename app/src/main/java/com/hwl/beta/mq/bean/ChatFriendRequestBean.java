package com.hwl.beta.mq.bean;

import java.util.Date;

/**
 * Created by Administrator on 2018/2/8.
 */

public class ChatFriendRequestBean {
    private long fromUserId;
    private String fromUserSymbol;
    private String fromUserName;
    private String fromUserHeadImage;
    private long toUserId;
    private int contentType;
    private String content;
    private Date sendTime;

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getFromUserSymbol() {
        return fromUserSymbol;
    }

    public void setFromUserSymbol(String fromUserSymbol) {
        this.fromUserSymbol = fromUserSymbol;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromUserHeadImage() {
        return fromUserHeadImage;
    }

    public void setFromUserHeadImage(String fromUserHeadImage) {
        this.fromUserHeadImage = fromUserHeadImage;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

//    public String getToUserName() {
//        return toUserName;
//    }
//
//    public void setToUserName(String toUserName) {
//        this.toUserName = toUserName;
//    }
//
//    public String getToUserHeadImage() {
//        return toUserHeadImage;
//    }
//
//    public void setToUserHeadImage(String toUserHeadImage) {
//        this.toUserHeadImage = toUserHeadImage;
//    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
