package com.hwl.beta.mq.bean;

import java.util.Date;

public class NearCircleCommentMessageBean {
    private long nearCircleId;
    private long fromUserId;
    private String fromUserName;
    private String fromUserImage;
    private long toUserId;
    private String content;
    private int commentId;
    private String comment;
    private int actionType;
    private Date actionTime;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
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

    public long getNearCircleId() {
        return nearCircleId;
    }

    public void setNearCircleId(long nearCircleId) {
        this.nearCircleId = nearCircleId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
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

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }
}
