package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DBConstant;
import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.Circle;
import com.hwl.beta.db.entity.CircleComment;
import com.hwl.beta.db.entity.CircleMessage;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.bean.CircleCommentMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.sp.MessageCountSP;
import com.hwl.beta.ui.busbean.EventBusConstant;

import org.greenrobot.eventbus.EventBus;

public class CircleCommentMessageProcess implements IMessageProcess<CircleCommentMessageBean> {
    @Override
    public void execute(byte messageType, CircleCommentMessageBean message) {
        if (message == null || message.getCircleId() <= 0 || message.getFromUserId() <= 0 || message.getToUserId() <= 0)
            return;

        if (message.getActionType() == MQConstant.CIRCLE_MESSAGE_ACTION_ADD) {
            CircleMessage model = new CircleMessage();
            model.setCircleId(message.getCircleId());
            model.setUserId(message.getFromUserId());
            model.setUserName(message.getFromUserName());
            model.setUserImage(message.getFromUserImage());
            model.setContent(message.getContent());
            model.setType(DBConstant.CIRCLE_TYPE_COMMENT);
            model.setStatus(DBConstant.STATUS_UNREAD);
            model.setComment(message.getComment());
            model.setActionTime(message.getActionTime());
            if (!DaoUtils.getCircleMessageManagerInstance().save(model)) return;
            MessageCountSP.setCircleMessageCountIncrease();
            EventBus.getDefault().post(EventBusConstant.EB_TYPE_CIRCLE_MESSAGE_UPDATE);
            addComment(message);
        } else if (message.getActionType() == MQConstant.CIRCLE_MESSAGE_ACTION_DELETE) {
            if (DaoUtils.getCircleMessageManagerInstance().updateDelete(message.getCircleId(), DBConstant.CIRCLE_TYPE_COMMENT, message.getFromUserId(), message.getCommentId())) {
                MessageCountSP.setCircleMessageCountReduce();
                EventBus.getDefault().post(EventBusConstant.EB_TYPE_CIRCLE_MESSAGE_UPDATE);
                deleteComment(message);
            }
        }
    }

    private void addComment(CircleCommentMessageBean message) {
        Circle info = DaoUtils.getCircleManagerInstance().getCircle(message.getCircleId());
        if (info == null) return;
        CircleComment newCommentInfo = new CircleComment(message.getCommentId(), message.getCircleId(), message.getFromUserId(),
                message.getFromUserName(), message.getFromUserImage(), 0, "", "", message.getComment(), message.getActionTime());
        DaoUtils.getCircleManagerInstance().saveComment(message.getCircleId(), newCommentInfo);
    }

    private void deleteComment(CircleCommentMessageBean message) {
        CircleComment commentInfo = DaoUtils.getCircleManagerInstance().getComment(message.getCircleId(), message.getFromUserId(), message.getCommentId());
        if (commentInfo == null) return;
        DaoUtils.getCircleManagerInstance().deleteComment(message.getCircleId(), message.getFromUserId(), message.getCommentId());
    }
}
