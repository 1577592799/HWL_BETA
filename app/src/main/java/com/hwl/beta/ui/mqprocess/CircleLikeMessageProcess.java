package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleLike;
import com.hwl.beta.db.entity.CircleMessage;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.bean.CircleLikeMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.busbean.EventBusConstant;

import org.greenrobot.eventbus.EventBus;

public class CircleLikeMessageProcess implements IMessageProcess<CircleLikeMessageBean> {
    @Override
    public void execute(byte messageType, CircleLikeMessageBean message) {
        if (message == null || message.getCircleId() <= 0 || message.getFromUserId() <= 0 || message.getToUserId() <= 0)
            return;

        if (message.getActionType() == MQConstant.CIRCLE_MESSAGE_ACTION_ADD) {
            CircleMessage model = new CircleMessage();
            model.setCircleId(message.getCircleId());
            model.setUserId(message.getFromUserId());
            model.setUserName(message.getFromUserName());
            model.setUserImage(message.getFromUserImage());
            model.setContent(message.getContent());
            model.setType(DBConstant.CIRCLE_TYPE_LIKE);
            model.setStatus(DBConstant.STATUS_UNREAD);
            model.setActionTime(message.getActionTime());
            if (!DaoUtils.getCircleMessageManagerInstance().save(model)) return;
            MessageCountSP.setCircleMessageCountIncrease();
            EventBus.getDefault().post(EventBusConstant.EB_TYPE_CIRCLE_MESSAGE_UPDATE);
            addLike(message);
        } else if (message.getActionType() == MQConstant.CIRCLE_MESSAGE_ACTION_DELETE) {
            if (DaoUtils.getCircleMessageManagerInstance().updateDelete(message.getCircleId(), DBConstant.CIRCLE_TYPE_LIKE, message.getFromUserId(), 0)) {
                MessageCountSP.setCircleMessageCountReduce();
                EventBus.getDefault().post(EventBusConstant.EB_TYPE_CIRCLE_MESSAGE_UPDATE);
                deleteLike(message);
            }
        }
    }

    private void addLike(CircleLikeMessageBean message) {
        Circle info = DaoUtils.getCircleManagerInstance().getCircle(message.getCircleId());
        if (info == null) return;
        CircleLike likInfo = DaoUtils.getCircleManagerInstance().getLike(message.getCircleId(), message.getFromUserId());
        if (likInfo != null) return;
        CircleLike newLikeInfo = new CircleLike(message.getCircleId(), message.getFromUserId(), message.getFromUserName(),
                message.getFromUserImage(), message.getActionTime());
        DaoUtils.getCircleManagerInstance().saveLike(message.getCircleId(), newLikeInfo);
    }

    private void deleteLike(CircleLikeMessageBean message) {
        CircleLike likInfo = DaoUtils.getCircleManagerInstance().getLike(message.getCircleId(), message.getFromUserId());
        if (likInfo == null) return;
        DaoUtils.getCircleManagerInstance().deleteLike(message.getCircleId(), message.getFromUserId());
    }
}
