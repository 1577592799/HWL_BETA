package com.hwl.beta.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/4/14.
 */

@Entity
public class NearCircleLike {
    private long nearCircleId;
    private long likeUserId;
    private String likeUserName;
    private String likeUserImage;
    @OrderBy("likeTime desc")
    private String likeTime;
    @Generated(hash = 504663597)
    public NearCircleLike(long nearCircleId, long likeUserId, String likeUserName,
            String likeUserImage, String likeTime) {
        this.nearCircleId = nearCircleId;
        this.likeUserId = likeUserId;
        this.likeUserName = likeUserName;
        this.likeUserImage = likeUserImage;
        this.likeTime = likeTime;
    }
    @Generated(hash = 559624202)
    public NearCircleLike() {
    }
    public long getNearCircleId() {
        return this.nearCircleId;
    }
    public void setNearCircleId(long nearCircleId) {
        this.nearCircleId = nearCircleId;
    }
    public long getLikeUserId() {
        return this.likeUserId;
    }
    public void setLikeUserId(long likeUserId) {
        this.likeUserId = likeUserId;
    }
    public String getLikeUserName() {
        return this.likeUserName;
    }
    public void setLikeUserName(String likeUserName) {
        this.likeUserName = likeUserName;
    }
    public String getLikeUserImage() {
        return this.likeUserImage;
    }
    public void setLikeUserImage(String likeUserImage) {
        this.likeUserImage = likeUserImage;
    }
    public String getLikeTime() {
        return this.likeTime;
    }
    public void setLikeTime(String likeTime) {
        this.likeTime = likeTime;
    }
}
