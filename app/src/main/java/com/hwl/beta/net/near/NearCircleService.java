package com.hwl.beta.net.near;

import com.hwl.beta.net.RequestBase;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.RetrofitUtils;
import com.hwl.beta.net.near.body.AddNearCircleInfoRequest;
import com.hwl.beta.net.near.body.AddNearCircleInfoResponse;
import com.hwl.beta.net.near.body.AddNearCommentRequest;
import com.hwl.beta.net.near.body.AddNearCommentResponse;
import com.hwl.beta.net.near.body.GetNearCircleDetailRequest;
import com.hwl.beta.net.near.body.GetNearCircleDetailResponse;
import com.hwl.beta.net.near.body.GetNearCircleInfosRequest;
import com.hwl.beta.net.near.body.GetNearCircleInfosResponse;
import com.hwl.beta.net.near.body.GetNearCommentsRequest;
import com.hwl.beta.net.near.body.GetNearCommentsResponse;
import com.hwl.beta.net.near.body.SetNearLikeInfoRequest;
import com.hwl.beta.net.near.body.SetNearLikeInfoResponse;
import com.hwl.beta.sp.UserPosSP;
import com.hwl.beta.sp.UserSP;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/3/8.
 */

public class NearCircleService {

    public static Observable<ResponseBase<GetNearCircleInfosResponse>> getNearCircleInfos(long minNearCircleId, int pageCount) {
        GetNearCircleInfosRequest requestBody = new GetNearCircleInfosRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setLat(UserPosSP.getLatitude());
        requestBody.setLon(UserPosSP.getLontitude());
        requestBody.setMinNearCircleId(minNearCircleId);
//        requestBody.setPageIndex(pageIndex);
        requestBody.setCount(pageCount <= 0 ? 15 : pageCount);
        Observable<ResponseBase<GetNearCircleInfosResponse>> response = RetrofitUtils.createApi(INearCircleService.class)
                .getNearCircleInfos(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io());
//                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

//    public static void getNearComments(int nearCircleId, int lastCommentId, RetrofitCallBack callBack) {
//        GetNearCommentsRequest request = new GetNearCommentsRequest();
//        request.setHeadToken(UserSP.getUserToken());
//        request.setCount(3);
//        request.setLastCommentId(lastCommentId);
//        request.setNearCircleId(nearCircleId);
//        Call call = RetrofitUtils.createApi(INearCircleService.class).getNearComments(request);
//        call.enqueue(callBack);
//    }

    public static Observable<ResponseBase<GetNearCircleDetailResponse>> getNearCircleDetail(long nearCircleId) {
        GetNearCircleDetailRequest requestBody = new GetNearCircleDetailRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setNearCircleId(nearCircleId);
        Observable<ResponseBase<GetNearCircleDetailResponse>> response = RetrofitUtils.createApi(INearCircleService.class)
                .getNearCircleDetail(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    //    public static void getNearCircleDetail(long nearCircleId, RetrofitCallBack callBack) {
//        GetNearCircleDetailRequest request = new GetNearCircleDetailRequest();
//        request.setHeadToken(UserSP.getUserToken());
//        request.setUserId(UserSP.getUserId());
//        request.setNearCircleId((int) nearCircleId);
//        Call call = RetrofitUtils.createApi(INearCircleService.class).getNearCircleDetail(request);
//        call.enqueue(callBack);
//    }
//
    public static Observable<ResponseBase<AddNearCircleInfoResponse>> addNearCircleInfo(String content, List<NetImageInfo> images) {
        return addNearCircleInfo(content, images, null, null, null);
    }

    public static Observable<ResponseBase<AddNearCircleInfoResponse>> addNearCircleInfo(String content, List<NetImageInfo> images, String linkTitle, String linkUrl, String linkImage) {
        AddNearCircleInfoRequest requestBody = new AddNearCircleInfoRequest();
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
        Observable<ResponseBase<AddNearCircleInfoResponse>> response = RetrofitUtils.createApi(INearCircleService.class)
                .addNearCircleInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

//    public static void setNearLikeInfo(int actionType, int nearCircleId, RetrofitCallBack callBack) {
//        SetNearLikeInfoRequest request = new SetNearLikeInfoRequest();
//        request.setHeadToken(UserSP.getUserToken());
//        request.setLikeUserId(UserSP.getUserId());
//        request.setActionType(actionType);
//        request.setNearCircleId(nearCircleId);
//        Call call = RetrofitUtils.createApi(INearCircleService.class).setNearLikeInfo(request);
//        call.enqueue(callBack);
//    }
//
//    public static void addComment(int nearCircleId, String content, RetrofitCallBack callBack) {
//        addComment(nearCircleId, content, 0, callBack);
//    }
//
//    public static void addComment(int nearCircleId, String content, int replyUserId, RetrofitCallBack callBack) {
//        AddNearCommentRequest request = new AddNearCommentRequest();
//        request.setHeadToken(UserSP.getUserToken());
//        request.setCommentUserId(UserSP.getUserId());
//        request.setNearCircleId(nearCircleId);
//        request.setContent(content);
//        request.setReplyUserId(replyUserId);
//        Call call = RetrofitUtils.createApi(INearCircleService.class).addNearComment(request);
//        call.enqueue(callBack);
//    }

    public interface INearCircleService {
        @POST("api/GetNearCircleInfos")
        Observable<ResponseBase<GetNearCircleInfosResponse>> getNearCircleInfos(@Body RequestBase<GetNearCircleInfosRequest> request);

        @POST("api/AddNearCircleInfo")
        Observable<ResponseBase<AddNearCircleInfoResponse>> addNearCircleInfo(@Body RequestBase<AddNearCircleInfoRequest> request);

        @POST("api/GetNearCircleDetail")
        Observable<ResponseBase<GetNearCircleDetailResponse>> getNearCircleDetail(@Body RequestBase<GetNearCircleDetailRequest> request);

        @POST("api/SetNearLikeInfo")
        Observable<ResponseBase<SetNearLikeInfoResponse>> setNearLikeInfo(@Body RequestBase<SetNearLikeInfoRequest> request);

        @POST("api/AddNearComment")
        Observable<ResponseBase<AddNearCommentResponse>> addNearComment(@Body RequestBase<AddNearCommentRequest> request);

        @POST("api/GetNearComments")
        Observable<ResponseBase<GetNearCommentsResponse>> getNearComments(@Body RequestBase<GetNearCommentsRequest> request);
    }
}
