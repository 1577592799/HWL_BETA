package com.hwl.beta.mq.bean;

import java.util.Date;
import java.util.List;

public class GroupCreateMessageBean {
    private String groupGuid;
    private String groupName;
    private List<String> groupImage;
    private long buildUserId;
    private String buildUserName;
    private String buildUserImage;
    private Date buildTime;
    private int contentType;
    private String content;
    private List<MQGroupUserInfo> groupUsers;

    public String getBuildUserName() {
        return buildUserName;
    }

    public void setBuildUserName(String buildUserName) {
        this.buildUserName = buildUserName;
    }

    public String getBuildUserImage() {
        return buildUserImage;
    }

    public void setBuildUserImage(String buildUserImage) {
        this.buildUserImage = buildUserImage;
    }

    public List<String> getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(List<String> groupImage) {
        this.groupImage = groupImage;
    }

    public List<MQGroupUserInfo> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<MQGroupUserInfo> groupUsers) {
        this.groupUsers = groupUsers;
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

    public Date getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
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
}
