package com.hwl.beta.db.entity;

import com.hwl.beta.db.ListStringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.List;

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
    @Convert(columnType = String.class, converter = ListStringConverter.class)
    private List<String> userImages;
    private String groupNote;
    @OrderBy("buildTime desc")
    private Date buildTime;

    private String myUserName;
    private String groupBackImage;
    private boolean isShield;

    @Generated(hash = 950932202)
    public GroupInfo(String groupGuid, String groupName, String groupImage,
            int groupUserCount, List<String> userImages, String groupNote,
            Date buildTime, String myUserName, String groupBackImage,
            boolean isShield) {
        this.groupGuid = groupGuid;
        this.groupName = groupName;
        this.groupImage = groupImage;
        this.groupUserCount = groupUserCount;
        this.userImages = userImages;
        this.groupNote = groupNote;
        this.buildTime = buildTime;
        this.myUserName = myUserName;
        this.groupBackImage = groupBackImage;
        this.isShield = isShield;
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
    public List<String> getUserImages() {
        return this.userImages;
    }
    public void setUserImages(List<String> userImages) {
        this.userImages = userImages;
    }
    public String getMyUserName() {
        return this.myUserName;
    }
    public void setMyUserName(String myUserName) {
        this.myUserName = myUserName;
    }
    public String getGroupBackImage() {
        return this.groupBackImage;
    }
    public void setGroupBackImage(String groupBackImage) {
        this.groupBackImage = groupBackImage;
    }
    public boolean getIsShield() {
        return this.isShield;
    }
    public void setIsShield(boolean isShield) {
        this.isShield = isShield;
    }

}
