package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.ChatUserMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.bean.ChatFriendRequestBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.sp.UserSP;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/2/8.
 */

public class ChatFriendRequestProcess implements IMessageProcess<ChatFriendRequestBean> {
    private final static String TAG = "ChatFriendRequestProcess";

    @Override
    public void execute(byte messageType, ChatFriendRequestBean model) {
        //添加好友到数据库中
        //先添加消息记录
        //再添加消息
        //再通知Activity刷新
        Friend friend = new Friend();
        friend.setId(model.getFromUserId());
        friend.setSymbol(model.getFromUserSymbol());
        friend.setName(model.getFromUserName());
        friend.setRemark(model.getFromUserName());
        friend.setHeadImage(model.getFromUserHeadImage());
        DaoUtils.getFriendManagerInstance().save(friend);

        NetUserInfo user = UserSP.getUserInfo();
        ChatRecordMessage record = new ChatRecordMessage();
        record.setRecordType(MQConstant.CHAT_RECORD_TYPE_USER);
        record.setToUserId(model.getToUserId());
        record.setToUserName(user.getName());
        record.setToUserHeadImage(user.getHeadImage());
        record.setFromUserId(model.getFromUserId());
        record.setFromUserName(model.getFromUserName());
        record.setFromUserHeadImage(model.getFromUserHeadImage());
        record.setRecordImage(model.getFromUserHeadImage());
        record.setTitle(model.getFromUserName());
        record.setContentType(model.getContentType());
        record.setContent(model.getContent());
//        record.setUnreadCount(1);
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
        message.setSendStatus(MQConstant.CHAT_SEND_SUCCESS);
        message.setSendTime(model.getSendTime());
        DaoUtils.getChatUserMessageManagerInstance().save(message);

        EventBus.getDefault().post(friend);
        EventBus.getDefault().post(message);
        EventBus.getDefault().post(record);
    }
}
