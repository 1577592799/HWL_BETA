package com.hwl.beta.net.near.body;

/**
 * Created by Administrator on 2018/3/20.
 */

public class GetNearCommentsRequest {
    private int NearCircleId;
    private int Count;
    private int LastCommentId;

    public int getNearCircleId() {
        return NearCircleId;
    }

    public void setNearCircleId(int nearCircleId) {
        NearCircleId = nearCircleId;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getLastCommentId() {
        return LastCommentId;
    }

    public void setLastCommentId(int lastCommentId) {
        LastCommentId = lastCommentId;
    }
}
