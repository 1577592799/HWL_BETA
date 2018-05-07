package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleLike;
import com.hwl.beta.db.entity.NearCircleMessage;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.bean.NearCircleLikeMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.busbean.EventBusConstant;

import org.greenrobot.eventbus.EventBus;

public class NearCircleLikeMessageProcess implements IMessageProcess<NearCircleLikeMessageBean> {
    @Override
    public void execute(byte messageType, NearCircleLikeMessageBean message) {
        if (message == null || message.getNearCircleId() <= 0 || message.getFromUserId() <= 0 || message.getToUserId() <= 0)
            return;

        if (message.getActionType() == MQConstant.CIRCLE_MESSAGE_ACTION_ADD) {
            NearCircleMessage model = new NearCircleMessage();
            model.setNearCircleId(message.getNearCircleId());
            model.setUserId(message.getFromUserId());
            model.setUserName(message.getFromUserName());
            model.setUserImage(message.getFromUserImage());
            model.setContent(message.getContent());
            model.setType(DBConstant.CIRCLE_TYPE_LIKE);
            model.setStatus(DBConstant.STATUS_UNREAD);
            model.setActionTime(message.getActionTime());
            if (!DaoUtils.getNearCircleMessageManagerInstance().save(model)) return;
            MessageCountSP.setNearCircleMessageCountIncrease();
            EventBus.getDefault().post(EventBusConstant.EB_TYPE_NEAR_CIRCLE_MESSAGE_UPDATE);
            addLike(message);
        } else if (message.getActionType() == MQConstant.CIRCLE_MESSAGE_ACTION_DELETE) {
            if (DaoUtils.getNearCircleMessageManagerInstance().updateDelete(message.getNearCircleId(), DBConstant.CIRCLE_TYPE_LIKE, message.getFromUserId(), "")) {
                MessageCountSP.setNearCircleMessageCountReduce();
                EventBus.getDefault().post(EventBusConstant.EB_TYPE_NEAR_CIRCLE_MESSAGE_UPDATE);
                deleteLike(message);
            }
        }
    }

    private void addLike(NearCircleLikeMessageBean message) {
        NearCircle info = DaoUtils.getNearCircleManagerInstance().getNearCircle(message.getNearCircleId());
        if (info == null) return;
        NearCircleLike likInfo = DaoUtils.getNearCircleManagerInstance().getLike(message.getNearCircleId(), message.getFromUserId());
        if (likInfo != null) return;
        NearCircleLike newLikeInfo = new NearCircleLike(message.getNearCircleId(), message.getFromUserId(), message.getFromUserName(),
                message.getFromUserImage(), message.getActionTime());
        DaoUtils.getNearCircleManagerInstance().saveLike(message.getNearCircleId(), newLikeInfo);
    }

    private void deleteLike(NearCircleLikeMessageBean message) {
        NearCircleLike likInfo = DaoUtils.getNearCircleManagerInstance().getLike(message.getNearCircleId(), message.getFromUserId());
        if (likInfo == null) return;
        DaoUtils.getNearCircleManagerInstance().deleteLike(message.getNearCircleId(), message.getFromUserId());
    }
}
