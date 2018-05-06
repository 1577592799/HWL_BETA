package com.hwl.beta.mq.bean;

import java.util.Date;
import java.util.List;

public class GroupUsersAddMessageBean {
    private long actionUserId;
    private String groupGuid;
    private String groupName;
    private long buildUserId;
    private String groupNote;
    private Date buildTime;
    private List<String> groupUserImages;
    private List<MQGroupUserInfo> userInfos;
    private int contentType;
    private String content;
    private Date actoinTime;

    public List<String> getGroupUserImages() {
        return groupUserImages;
    }

    public void setGroupUserImages(List<String> groupUserImages) {
        this.groupUserImages = groupUserImages;
    }

    public String getGroupGuid() {
        return groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getBuildUserId() {
        return buildUserId;
    }

    public void setBuildUserId(long buildUserId) {
        this.buildUserId = buildUserId;
    }

    public String getGroupNote() {
        return groupNote;
    }

    public void setGroupNote(String groupNote) {
        this.groupNote = groupNote;
    }

    public Date getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
    }

    public long getActionUserId() {
        return actionUserId;
    }

    public void setActionUserId(long actionUserId) {
        this.actionUserId = actionUserId;
    }

    public List<MQGroupUserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<MQGroupUserInfo> userInfos) {
        this.userInfos = userInfos;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getActoinTime() {
        return actoinTime;
    }

    public void setActoinTime(Date actoinTime) {
        this.actoinTime = actoinTime;
    }
}
