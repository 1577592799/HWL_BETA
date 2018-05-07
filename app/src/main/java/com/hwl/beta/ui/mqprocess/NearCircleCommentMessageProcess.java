package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.NearCircle;
import com.hwl.beta.db.entity.NearCircleComment;
import com.hwl.beta.db.entity.NearCircleMessage;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.bean.NearCircleCommentMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.busbean.EventBusConstant;

import org.greenrobot.eventbus.EventBus;

public class NearCircleCommentMessageProcess implements IMessageProcess<NearCircleCommentMessageBean> {
    @Override
    public void execute(byte messageType, NearCircleCommentMessageBean message) {
        if (message == null || message.getNearCircleId() <= 0 || message.getFromUserId() <= 0 || message.getToUserId() <= 0)
            return;

        if (message.getActionType() == MQConstant.CIRCLE_MESSAGE_ACTION_ADD) {
            NearCircleMessage model = new NearCircleMessage();
            model.setNearCircleId(message.getNearCircleId());
            model.setUserId(message.getFromUserId());
            model.setUserName(message.getFromUserName());
            model.setUserImage(message.getFromUserImage());
            model.setContent(message.getContent());
            model.setType(DBConstant.CIRCLE_TYPE_COMMENT);
            model.setStatus(DBConstant.STATUS_UNREAD);
            model.setComment(message.getComment());
            model.setActionTime(message.getActionTime());
            if (!DaoUtils.getNearCircleMessageManagerInstance().save(model)) return;
            MessageCountSP.setNearCircleMessageCountIncrease();
            EventBus.getDefault().post(EventBusConstant.EB_TYPE_NEAR_CIRCLE_MESSAGE_UPDATE);
            addComment(message);
        } else if (message.getActionType() == MQConstant.CIRCLE_MESSAGE_ACTION_DELETE) {
            if (DaoUtils.getNearCircleMessageManagerInstance().updateDelete(message.getNearCircleId(), DBConstant.CIRCLE_TYPE_COMMENT, message.getFromUserId(), message.getComment())) {
                MessageCountSP.setNearCircleMessageCountReduce();
                EventBus.getDefault().post(EventBusConstant.EB_TYPE_NEAR_CIRCLE_MESSAGE_UPDATE);
                deleteComment(message);
            }
        }
    }

    private void addComment(NearCircleCommentMessageBean message) {
        NearCircle info = DaoUtils.getNearCircleManagerInstance().getNearCircle(message.getNearCircleId());
        if (info == null) return;
        NearCircleComment newCommentInfo = new NearCircleComment(message.getCommentId(), message.getNearCircleId(), message.getFromUserId(),
                message.getFromUserName(), message.getFromUserImage(), 0, "", "", message.getComment(), message.getActionTime());
        DaoUtils.getNearCircleManagerInstance().saveComment(message.getNearCircleId(), newCommentInfo);
    }

    private void deleteComment(NearCircleCommentMessageBean message) {
        NearCircleComment commentInfo = DaoUtils.getNearCircleManagerInstance().getComment(message.getNearCircleId(), message.getFromUserId(), message.getCommentId());
        if (commentInfo == null) return;
        DaoUtils.getNearCircleManagerInstance().deleteComment(message.getNearCircleId(), message.getFromUserId(), message.getCommentId());
    }
}
