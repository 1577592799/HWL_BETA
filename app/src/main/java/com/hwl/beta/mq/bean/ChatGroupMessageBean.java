package com.hwl.beta.mq.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/2/9.
 */

public class ChatGroupMessageBean {
    private String groupGuid;
    private String groupName;
    private List<String> groupUserImages;
    private long fromUserId;
    private String fromUserName;
    private String fromUserHeadImage;
    private int contentType;
    private String content;
    private String previewUrl;
    private String originalUrl;
    private int imageWidth;
    private int imageHeight;
    private long size;
    private long playTime;
    private Date sendTime;

    public List<String> getGroupUserImages() {
        return groupUserImages;
    }

    public void setGroupUserImages(List<String> groupUserImages) {
        this.groupUserImages = groupUserImages;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getPlayTime() {
        return playTime;
    }

    public void setPlayTime(long playTime) {
        this.playTime = playTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupGuid() {
        return groupGuid;
    }

    public void setGroupGuid(String groupGuid) {
        this.groupGuid = groupGuid;
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

    public String getFromUserHeadImage() {
        return fromUserHeadImage;
    }

    public void setFromUserHeadImage(String fromUserHeadImage) {
        this.fromUserHeadImage = fromUserHeadImage;
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

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
