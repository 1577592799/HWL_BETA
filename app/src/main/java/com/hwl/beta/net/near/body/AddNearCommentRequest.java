package com.hwl.beta.net.near.body;

/**
 * Created by Administrator on 2018/3/14.
 */

public class AddNearCommentRequest {

    /// <summary>
    /// 附近信息id
    /// </summary>
    private int NearCircleId;
    /// <summary>
    /// 发评论的用户id
    /// </summary>
    private int CommentUserId;
    /// <summary>
    /// 回复用户id
    /// </summary>
    private int ReplyUserId;
    private String Content;

    public int getNearCircleId() {
        return NearCircleId;
    }

    public void setNearCircleId(int nearCircleId) {
        NearCircleId = nearCircleId;
    }

    public int getCommentUserId() {
        return CommentUserId;
    }

    public void setCommentUserId(int commentUserId) {
        CommentUserId = commentUserId;
    }

    public int getReplyUserId() {
        return ReplyUserId;
    }

    public void setReplyUserId(int replyUserId) {
        ReplyUserId = replyUserId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
