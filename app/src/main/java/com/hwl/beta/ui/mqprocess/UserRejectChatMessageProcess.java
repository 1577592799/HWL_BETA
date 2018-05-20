package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.bean.UserRejectChatMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;

import org.greenrobot.eventbus.EventBus;

public class UserRejectChatMessageProcess implements IMessageProcess<UserRejectChatMessageBean> {
    @Override
    public void execute(byte messageType, UserRejectChatMessageBean model) {
        if (model == null || model.getMsgId() <= 0) return;

        //检测是否已经包含了reject信息,如果包含则不提示用户
        if (DaoUtils.getChatUserMessageManagerInstance().isExistsRejectMessage(model.getToUserId(), model.getFromUserId())) {
            ChatUserMessage userMessage = DaoUtils.getChatUserMessageManagerInstance().get(model.getMsgId());
            if (userMessage == null) return;
            userMessage.setSendStatus(MQConstant.CHAT_SEND_FAILD);
            DaoUtils.getChatUserMessageManagerInstance().save(userMessage);
            EventBus.getDefault().post(userMessage);
            return;
        }

        ChatUserMessage message = new ChatUserMessage();
        message.setFromUserId(model.getFromUserId());
        message.setFromUserName(model.getFromUserName());
        message.setToUserId(model.getToUserId());
        message.setContentType(MQConstant.CHAT_MESSAGE_CONTENT_TYPE_REJECT);
        message.setContent(model.getFromUserName() + " 已经设置不跟陌生人聊天,你还是别打扰TA了 !");
        message.setSendStatus(MQConstant.CHAT_SEND_SUCCESS);
        message.setSendTime(model.getSendTime());
        DaoUtils.getChatUserMessageManagerInstance().save(message);
        EventBus.getDefault().post(message);

        ChatUserMessage userMessage = DaoUtils.getChatUserMessageManagerInstance().get(model.getMsgId());
        if (userMessage == null) return;
        userMessage.setSendStatus(MQConstant.CHAT_SEND_FAILD);
        DaoUtils.getChatUserMessageManagerInstance().save(userMessage);
        EventBus.getDefault().post(userMessage);
    }
}
