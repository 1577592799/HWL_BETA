package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.mq.bean.UserLogoutMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.busbean.EventBusConstant;

import org.greenrobot.eventbus.EventBus;

public class UserLogoutMessageProcess implements IMessageProcess<UserLogoutMessageBean> {
    @Override
    public void execute(byte messageType, UserLogoutMessageBean message) {
        if (message == null || message.getUserId() != UserSP.getUserId() || !message.getToken().equals(message.getToken()))
            return;

        EventBus.getDefault().post(message);
    }
}
