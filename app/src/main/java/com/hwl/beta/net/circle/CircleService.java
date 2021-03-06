package com.hwl.beta.net.circle;

import com.hwl.beta.net.RequestBase;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.RetrofitUtils;
import com.hwl.beta.net.circle.body.AddCircleCommentInfoRequest;
import com.hwl.beta.net.circle.body.AddCircleCommentInfoResponse;
import com.hwl.beta.net.circle.body.AddCircleInfoRequest;
import com.hwl.beta.net.circle.body.AddCircleInfoResponse;
import com.hwl.beta.net.circle.body.DeleteCircleInfoRequest;
import com.hwl.beta.net.circle.body.DeleteCircleInfoResponse;
import com.hwl.beta.net.circle.body.DeleteCommentInfoRequest;
import com.hwl.beta.net.circle.body.DeleteCommentInfoResponse;
import com.hwl.beta.net.circle.body.GetCircleDetailRequest;
import com.hwl.beta.net.circle.body.GetCircleDetailResponse;
import com.hwl.beta.net.circle.body.GetCircleInfosRequest;
import com.hwl.beta.net.circle.body.GetCircleInfosResponse;
import com.hwl.beta.net.circle.body.GetUserCircleInfosRequest;
import com.hwl.beta.net.circle.body.GetUserCircleInfosResponse;
import com.hwl.beta.net.circle.body.SetLikeInfoRequest;
import com.hwl.beta.net.circle.body.SetLikeInfoResponse;
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

    public static Observable<ResponseBase<GetCircleInfosResponse>> getCircleInfos(long viewUserId, long minCircleId, int pageCount) {
        return getCircleInfos(viewUserId, minCircleId, pageCount, null);
    }

    public static Observable<ResponseBase<GetCircleInfosResponse>> getCircleInfos(long minCircleId, int pageCount, List<NetCircleMatchInfo> circleMatchInfos) {
        return getCircleInfos(0, minCircleId, pageCount, circleMatchInfos);
    }

    public static Observable<ResponseBase<GetCircleInfosResponse>> getCircleInfos(long viewUserId, long minCircleId, int pageCount, List<NetCircleMatchInfo> circleMatchInfos) {
        GetCircleInfosRequest requestBody = new GetCircleInfosRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setViewUserId(viewUserId);
        requestBody.setMinCircleId(minCircleId);
        requestBody.setCircleMatchInfos(circleMatchInfos);
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

    public static Observable<ResponseBase<SetLikeInfoResponse>> setLikeInfo(int actionType, long CircleId) {
        SetLikeInfoRequest requestBody = new SetLikeInfoRequest();
        requestBody.setLikeUserId(UserSP.getUserId());
        requestBody.setActionType(actionType);
        requestBody.setCircleId(CircleId);
        Observable<ResponseBase<SetLikeInfoResponse>> response = RetrofitUtils.createApi(ICircleService.class)
                .setCircleLikeInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static void addComment(long nearCircleId, String content) {
        addComment(nearCircleId, content, 0);
    }

    public static Observable<ResponseBase<AddCircleCommentInfoResponse>> addComment(long CircleId, String content, long replyUserId) {
        AddCircleCommentInfoRequest requestBody = new AddCircleCommentInfoRequest();
        requestBody.setCommentUserId(UserSP.getUserId());
        requestBody.setCircleId(CircleId);
        requestBody.setContent(content);
        requestBody.setReplyUserId(replyUserId);
        Observable<ResponseBase<AddCircleCommentInfoResponse>> response = RetrofitUtils.createApi(ICircleService.class)
                .AddCircleCommentInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

//    public static Observable<ResponseBase<GetUserCircleInfosResponse>> getUserCircleInfos(long viewUserId, long minCircleId, int pageCount) {
//        GetUserCircleInfosRequest requestBody = new GetUserCircleInfosRequest();
//        requestBody.setUserId(UserSP.getUserId());
//        requestBody.setViewUserId(viewUserId);
//        requestBody.setMinCircleId(minCircleId);
//        requestBody.setCount(pageCount <= 0 ? 15 : pageCount);
//        Observable<ResponseBase<GetUserCircleInfosResponse>> response = RetrofitUtils.createApi(ICircleService.class)
//                .getUserCircleInfos(new RequestBase(UserSP.getUserToken(), requestBody))
//                .subscribeOn(Schedulers.io());
////                .observeOn(AndroidSchedulers.mainThread());
//        return response;
//    }

    public static Observable<ResponseBase<GetCircleDetailResponse>> getCircleDetail(long circleId) {
        GetCircleDetailRequest requestBody = new GetCircleDetailRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setCircleId(circleId);
        Observable<ResponseBase<GetCircleDetailResponse>> response = RetrofitUtils.createApi(ICircleService.class)
                .getCircleDetail(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<DeleteCircleInfoResponse>> deleteCircleInfo(long circleId) {
        DeleteCircleInfoRequest requestBody = new DeleteCircleInfoRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setCircleId(circleId);
        Observable<ResponseBase<DeleteCircleInfoResponse>> response = RetrofitUtils.createApi(ICircleService.class)
                .deleteCircleInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public static Observable<ResponseBase<DeleteCommentInfoResponse>> deleteCommentInfo(int commentId) {
        DeleteCommentInfoRequest requestBody = new DeleteCommentInfoRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setCommentId(commentId);
        Observable<ResponseBase<DeleteCommentInfoResponse>> response = RetrofitUtils.createApi(ICircleService.class)
                .deleteCommentInfo(new RequestBase(UserSP.getUserToken(), requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public interface ICircleService {
        @POST("api/GetCircleInfos")
        Observable<ResponseBase<GetCircleInfosResponse>> getCircleInfos(@Body RequestBase<GetCircleInfosRequest> request);

        @POST("api/AddCircleInfo")
        Observable<ResponseBase<AddCircleInfoResponse>> addCircleInfo(@Body RequestBase<AddCircleInfoRequest> request);

        @POST("api/SetCircleLikeInfo")
        Observable<ResponseBase<SetLikeInfoResponse>> setCircleLikeInfo(@Body RequestBase<SetLikeInfoRequest> request);

        @POST("api/AddCircleCommentInfo")
        Observable<ResponseBase<AddCircleCommentInfoResponse>> AddCircleCommentInfo(@Body RequestBase<AddCircleCommentInfoRequest> request);

        @POST("api/GetUserCircleInfos")
        Observable<ResponseBase<GetUserCircleInfosResponse>> getUserCircleInfos(@Body RequestBase<GetUserCircleInfosRequest> request);

        @POST("api/GetCircleDetail")
        Observable<ResponseBase<GetCircleDetailResponse>> getCircleDetail(@Body RequestBase<GetCircleDetailRequest> request);

        @POST("api/DeleteCircleInfo")
        Observable<ResponseBase<DeleteCircleInfoResponse>> deleteCircleInfo(@Body RequestBase<DeleteCircleInfoRequest> request);

        @POST("api/DeleteCommentInfo")
        Observable<ResponseBase<DeleteCommentInfoResponse>> deleteCommentInfo(@Body RequestBase<DeleteCommentInfoRequest> request);
    }
}
