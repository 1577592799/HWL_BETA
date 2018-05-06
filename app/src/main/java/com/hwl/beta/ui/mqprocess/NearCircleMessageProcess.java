package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.NearCircleMessage;
import com.hwl.beta.mq.bean.NearCircleMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.busbean.EventBusConstant;

import org.greenrobot.eventbus.EventBus;

public class NearCircleMessageProcess implements IMessageProcess<NearCircleMessageBean> {
    @Override
    public void execute(byte messageType, NearCircleMessageBean message) {
        if (message == null || message.getFromUserId() <= 0 || message.getToUserId() <= 0) return;

        NearCircleMessage model = new NearCircleMessage();
        model.setUserId(message.getFromUserId());
        model.setUserName(message.getFromUserName());
        model.setUserImage(message.getFromUserImage());
        model.setType(message.getActionType());
        model.setContent(message.getContent());
        model.setComment(message.getComment());
        model.setStatus(DBConstant.STATUS_UNREAD);
        model.setActionTime(message.getActionTime());
        if (!DaoUtils.getNearCircleManagerInstance().addNearCircleMessage(model)) return;

        MessageCountSP.setNearCircleMessageCountAuto();
        EventBus.getDefault().post(EventBusConstant.EB_TYPE_NEAR_CIRCLE_MESSAGE_UPDATE);
    }
}
