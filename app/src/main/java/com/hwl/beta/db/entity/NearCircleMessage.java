package com.hwl.beta.db.entity;

import com.hwl.beta.utils.DateUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class NearCircleMessage {
    @Id(autoincrement = true)
    private Long id;
    private int type;
    private long userId;
    private String userName;
    private String comment;
    private String content;
    private String userImage;
    private Date actionTime;
    private int status;

    public String getShowTime() {
        if (this.actionTime != null)
            return DateUtils.dateToStrLong2(this.actionTime);
        return null;
    }

    @Generated(hash = 517306441)
    public NearCircleMessage(Long id, int type, long userId, String userName, String comment,
            String content, String userImage, Date actionTime, int status) {
        this.id = id;
        this.type = type;
        this.userId = userId;
        this.userName = userName;
        this.comment = comment;
        this.content = content;
        this.userImage = userImage;
        this.actionTime = actionTime;
        this.status = status;
    }

    @Generated(hash = 871113473)
    public NearCircleMessage() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserImage() {
        return this.userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Date getActionTime() {
        return this.actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}