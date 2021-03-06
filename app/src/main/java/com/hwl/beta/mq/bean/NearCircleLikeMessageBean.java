package com.hwl.beta.mq.bean;

import java.util.Date;

public class NearCircleLikeMessageBean {
    private long nearCircleId;
    private long fromUserId;
    private String fromUserName;
    private String fromUserImage;
    private long toUserId;
    private String content;
    private int actionType;
    private Date actionTime;

    public long getNearCircleId() {
        return nearCircleId;
    }

    public void setNearCircleId(long nearCircleId) {
        this.nearCircleId = nearCircleId;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromUserImage() {
        return fromUserImage;
    }

    public void setFromUserImage(String fromUserImage) {
        this.fromUserImage = fromUserImage;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }
}
