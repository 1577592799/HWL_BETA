package com.hwl.beta.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;

/**
 * Created by Administrator on 2018/2/10.
 */
@Entity
public class GroupInfo {
    @Unique
    private String groupGuid;
    private String groupName;
    private String groupImage;
    private int groupUserCount;
    private String groupNote;
    @OrderBy("buildTime desc")
    private Date buildTime;
    @Generated(hash = 1438844928)
    public GroupInfo(String groupGuid, String groupName, String groupImage,
            int groupUserCount, String groupNote, Date buildTime) {
        this.groupGuid = groupGuid;
        this.groupName = groupName;
        this.groupImage = groupImage;
        this.groupUserCount = groupUserCount;
        this.groupNote = groupNote;
        this.buildTime = buildTime;
    }
    @Generated(hash = 1250265142)
    public GroupInfo() {
    }
    public String getGroupGuid() {
        return this.groupGuid;
    }
    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getGroupImage() {
        return this.groupImage;
    }
    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }
    public int getGroupUserCount() {
        return this.groupUserCount;
    }
    public void setGroupUserCount(int groupUserCount) {
        this.groupUserCount = groupUserCount;
    }
    public String getGroupNote() {
        return this.groupNote;
    }
    public void setGroupNote(String groupNote) {
        this.groupNote = groupNote;
    }
    public Date getBuildTime() {
        return this.buildTime;
    }
    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
    }

}
