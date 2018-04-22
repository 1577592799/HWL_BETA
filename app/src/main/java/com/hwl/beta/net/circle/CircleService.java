package com.hwl.beta.net.circle;

import com.hwl.beta.net.RequestBase;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.RetrofitUtils;
import com.hwl.beta.net.circle.body.AddCircleInfoRequest;
import com.hwl.beta.net.circle.body.AddCircleInfoResponse;
import com.hwl.beta.net.circle.body.GetCircleInfosRequest;
import com.hwl.beta.net.circle.body.GetCircleInfosResponse;
import com.hwl.beta.net.near.NetImageInfo;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class CircleService {

    public static Observable<ResponseBase<GetCircleInfosResponse>> getCircleInfos(long minCircleId, int pageCount) {
        GetCircleInfosRequest requestBody = new GetCircleInfosRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setMinCircleId(minCircleId);
//        requestBody.setPageIndex(pageIndex);
        requestBody.setCount(pageCount <= 0 ? 15 : pageCount);
        Observable<ResponseBase<GetCircleInfosResponse>> response = RetrofitUtils.createApi(ICircleService.class)
                .getCircleInfos(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io());
//                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<AddCircleInfoResponse>> addCircleInfo(String content, List<NetImageInfo> images) {
        return addCircleInfo(content, images, null, null, null);
    }

    public static Observable<ResponseBase<AddCircleInfoResponse>> addCircleInfo(String content, List<NetImageInfo> images, String linkTitle, String linkUrl, String linkImage) {
        AddCircleInfoRequest requestBody = new AddCircleInfoRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setLat(UserPosSP.getLatitude());
        requestBody.setLon(UserPosSP.getLontitude());
        requestBody.setContent(content);
        requestBody.setPosId(UserPosSP.getUserPosId());
        requestBody.setPosDesc(UserPosSP.getPublishDesc());
        requestBody.setImages(images);
        requestBody.setLinkImage(linkImage);
        requestBody.setLinkTitle(linkTitle);
        requestBody.setLinkUrl(linkUrl);
        Observable<ResponseBase<AddCircleInfoResponse>> response = RetrofitUtils.createApi(ICircleService.class)
                .addCircleInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public interface ICircleService {
        @POST("api/GetCircleInfos")
        Observable<ResponseBase<GetCircleInfosResponse>> getCircleInfos(@Body RequestBase<GetCircleInfosRequest> request);

        @POST("api/AddCircleInfo")
        Observable<ResponseBase<AddCircleInfoResponse>> addCircleInfo(@Body RequestBase<AddCircleInfoRequest> request);
    }
}
