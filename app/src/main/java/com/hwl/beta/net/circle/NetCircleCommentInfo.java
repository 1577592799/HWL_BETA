package com.hwl.beta.net.circle;

public class NetCircleCommentInfo {
    private int Id;
    private int CircleId;

    private int CommentUserId;
    private String CommentUserName;
    private String CommentUserImage;

    private int ReplyUserId;
    private String ReplyUserName;
    private String ReplyUserImage;

    private String Content;
    private String CommentTimeDesc;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getCircleId() {
        return CircleId;
    }

    public void setCircleId(int circleId) {
        CircleId = circleId;
    }

    public int getCommentUserId() {
        return CommentUserId;
    }

    public void setCommentUserId(int commentUserId) {
        CommentUserId = commentUserId;
    }

    public String getCommentUserName() {
        return CommentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        CommentUserName = commentUserName;
    }

    public String getCommentUserImage() {
        return CommentUserImage;
    }

    public void setCommentUserImage(String commentUserImage) {
        CommentUserImage = commentUserImage;
    }

    public int getReplyUserId() {
        return ReplyUserId;
    }

    public void setReplyUserId(int replyUserId) {
        ReplyUserId = replyUserId;
    }

    public String getReplyUserName() {
        return ReplyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        ReplyUserName = replyUserName;
    }

    public String getReplyUserImage() {
        return ReplyUserImage;
    }

    public void setReplyUserImage(String replyUserImage) {
        ReplyUserImage = replyUserImage;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCommentTimeDesc() {
        return CommentTimeDesc;
    }

    public void setCommentTimeDesc(String commentTimeDesc) {
        CommentTimeDesc = commentTimeDesc;
    }
}
