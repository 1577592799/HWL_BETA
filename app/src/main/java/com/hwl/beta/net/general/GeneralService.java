package com.hwl.beta.net.general;

import com.hwl.beta.net.RequestBase;
import com.hwl.beta.net.ResponseBase;
import com.hwl.beta.net.RetrofitUtils;
import com.hwl.beta.net.general.body.CheckVersionRequest;
import com.hwl.beta.net.general.body.CheckVersionResponse;
import com.hwl.beta.net.general.body.SendEmailRequest;
import com.hwl.beta.net.general.body.SendEmailResponse;
import com.hwl.beta.net.general.body.SendSMSRequest;
import com.hwl.beta.net.general.body.SendSMSResponse;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.AppUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/1/14.
 */

public class GeneralService {

    public static Observable<ResponseBase<SendEmailResponse>> sendEmail(String email) {
        SendEmailRequest requestBody = new SendEmailRequest();
        requestBody.setEmail(email);
        return RetrofitUtils.createApi(GeneralService.IGenericService.class)
                .sendEmail(new RequestBase(requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<ResponseBase<SendSMSResponse>> sendSMS(String mobile) {
        SendSMSRequest requestBody = new SendSMSRequest();
        requestBody.setMobile(mobile);
        return RetrofitUtils.createApi(GeneralService.IGenericService.class)
                .sendSMS(new RequestBase(requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<ResponseBase<CheckVersionResponse>> checkVersion() {
        CheckVersionRequest requestBody = new CheckVersionRequest();
        requestBody.setUserId(UserSP.getUserId());
        requestBody.setOldVersion(AppUtils.getAppVersionName());
        Observable<ResponseBase<CheckVersionResponse>> response = RetrofitUtils.createApi(GeneralService.IGenericService.class)
                .checkVersion(new RequestBase(requestBody))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return response;
    }

    public interface IGenericService {
        @POST("api/SendEmail")
        Observable<ResponseBase<SendEmailResponse>> sendEmail(@Body RequestBase<SendEmailRequest> request);

        @POST("api/SendSMS")
        Observable<ResponseBase<SendSMSResponse>> sendSMS(@Body RequestBase<SendSMSRequest> request);

        @POST("api/CheckVersion")
        Observable<ResponseBase<CheckVersionResponse>> checkVersion(@Body RequestBase<CheckVersionRequest> request);
    }
}
