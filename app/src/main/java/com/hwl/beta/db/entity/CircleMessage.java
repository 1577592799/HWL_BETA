package com.hwl.beta.db.entity;

import com.hwl.beta.utils.DateUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

@Entity
public class CircleMessage {
    @Id(autoincrement = true)
    private Long id;
    private int type;
    private long circleId;
    private long userId;
    private String userName;
    private int commentId;
    private String comment;
    private String content;
    private String userImage;
    private Date actionTime;
    private int status;

    @Generated(hash = 792838443)
    public CircleMessage(Long id, int type, long circleId, long userId,
            String userName, int commentId, String comment, String content,
            String userImage, Date actionTime, int status) {
        this.id = id;
        this.type = type;
        this.circleId = circleId;
        this.userId = userId;
        this.userName = userName;
        this.commentId = commentId;
        this.comment = comment;
        this.content = content;
        this.userImage = userImage;
        this.actionTime = actionTime;
        this.status = status;
    }

    @Generated(hash = 943442392)
    public CircleMessage() {
    }

    public String getShowTime() {
        if (this.actionTime != null)
            return DateUtils.getChatShowTime(this.actionTime);
        return null;
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

    public long getCircleId() {
        return this.circleId;
    }

    public void setCircleId(long circleId) {
        this.circleId = circleId;
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

    public int getCommentId() {
        return this.commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
