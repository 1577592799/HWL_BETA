package com.hwl.beta.net.circle.body;

public class GetCircleInfosRequest {
    /// <summary>
    /// 当前登录的用户id
    /// </summary>
    private long UserId;
    /// <summary>
    /// 如果有值，则获取比这个值小的数据列表
    /// </summary>
    private long MinCircleId;
    private int PageIndex;
    private int Count;

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public long getMinCircleId() {
        return MinCircleId;
    }

    public void setMinCircleId(long minCircleId) {
        MinCircleId = minCircleId;
    }

    public int getPageIndex() {
        return PageIndex;
    }

    public void setPageIndex(int pageIndex) {
        PageIndex = pageIndex;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
