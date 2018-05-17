package com.hwl.beta.ui.mqprocess;

import android.util.Log;

import com.hwl.beta.mq.bean.UserLogoutMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.busbean.EventBusConstant;

import org.greenrobot.eventbus.EventBus;

public class UserLogoutMessageProcess implements IMessageProcess<UserLogoutMessageBean> {
    @Override
    public void execute(byte messageType, UserLogoutMessageBean message) {
        Log.d("UserLogoutProcess", message.getToken() + "==" + UserSP.getUserToken());
        if (message == null || message.getUserId() != UserSP.getUserId() || !message.getToken().equals(UserSP.getUserToken()))
            return;
        EventBus.getDefault().post(message);
    }
}
