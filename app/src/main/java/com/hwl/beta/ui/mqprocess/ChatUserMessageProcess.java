package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.bean.ChatUserMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.ui.common.MessageNotifyManage;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/2/9.
 */

public class ChatUserMessageProcess implements IMessageProcess<ChatUserMessageBean> {
    private final static String TAG = "ChatUserMessageProcess";

    @Override
    public void execute(byte messageType, ChatUserMessageBean model) {
        if (model == null) return;

        NetUserInfo user = UserSP.getUserInfo();
        ChatRecordMessage record = new ChatRecordMessage();
//        record.setId(1);
        record.setRecordType(MQConstant.CHAT_RECORD_TYPE_USER);
        record.setRecordImage(model.getFromUserHeadImage());
        record.setFromUserId(model.getFromUserId());
        record.setFromUserName(model.getFromUserName());
        record.setFromUserHeadImage(model.getFromUserHeadImage());
        record.setToUserId(user.getId());
        record.setToUserName(user.getShowName());
        record.setToUserHeadImage(user.getHeadImage());
        record.setTitle(model.getFromUserName());
        record.setContentType(model.getContentType());
        record.setContent(StringUtils.cutString(model.getContent(),25));
        //record.setUnreadCount(1);
        record.setSendTime(model.getSendTime());
        record = DaoUtils.getChatRecordMessageManagerInstance().addOrUpdate(record);

        ChatUserMessage message = new ChatUserMessage();
//        message.setMsgId(1);
        message.setFromUserId(model.getFromUserId());
        message.setFromUserName(model.getFromUserName());
        message.setFromUserHeadImage(model.getFromUserHeadImage());
        message.setToUserId(model.getToUserId());
        message.setContentType(model.getContentType());
        message.setContent(model.getContent());
        message.setLocalUrl("");
        message.setPreviewUrl(model.getPreviewUrl());
        message.setOriginalUrl(model.getOriginalUrl());
        message.setImageWidth(model.getImageWidth());
        message.setImageHeight(model.getImageHeight());
        message.setPlayTime(model.getPlayTime());
        message.setSendStatus(MQConstant.CHAT_SEND_SUCCESS);
        message.setSendTime(model.getSendTime());
        DaoUtils.getChatUserMessageManagerInstance().save(message);

        EventBus.getDefault().post(message);
        EventBus.getDefault().post(record);
        MessageNotifyManage.play();
    }
}
