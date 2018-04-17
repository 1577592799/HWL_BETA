package com.hwl.beta.net.near.body;

/**
 * Created by Administrator on 2018/3/14.
 */

public class SetNearLikeInfoRequest {

    /// <summary>
    /// 0表示取消 1表示点赞
    /// </summary>
    private int ActionType;
    /// <summary>
    /// 点赞的用户id
    /// </summary>
    private int LikeUserId;
    private int NearCircleId;

    public int getActionType() {
        return ActionType;
    }

    public void setActionType(int actionType) {
        ActionType = actionType;
    }

    public int getLikeUserId() {
        return LikeUserId;
    }

    public void setLikeUserId(int likeUserId) {
        LikeUserId = likeUserId;
    }

    public int getNearCircleId() {
        return NearCircleId;
    }

    public void setNearCircleId(int nearCircleId) {
        NearCircleId = nearCircleId;
    }
}
