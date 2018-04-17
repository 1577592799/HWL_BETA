package com.hwl.beta.net.near.body;

/**
 * Created by Administrator on 2018/3/8.
 */

public class GetNearCircleInfosRequest {

    private long UserId;
//    private int PageIndex;
    private long MinNearCircleId;
    private double Lon;
    private double Lat;
    private int Count;

    public long getMinNearCircleId() {
        return MinNearCircleId;
    }

    public void setMinNearCircleId(long minNearCircleId) {
        MinNearCircleId = minNearCircleId;
    }

//    public int getPageIndex() {
//        return PageIndex;
//    }
//
//    public void setPageIndex(int pageIndex) {
//        PageIndex = pageIndex;
//    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long userId) {
        UserId = userId;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }
}
