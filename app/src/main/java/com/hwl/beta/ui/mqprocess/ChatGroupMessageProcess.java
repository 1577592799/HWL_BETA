package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.Friend;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.bean.ChatGroupMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.ui.common.MessageNotifyManage;
import com.hwl.beta.ui.convert.DBFriendAction;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/2/9.
 */

public class ChatGroupMessageProcess implements IMessageProcess<ChatGroupMessageBean> {
    private final static String TAG = "ChatGroupMessageProcess";

    @Override
    public void execute(byte messageType, ChatGroupMessageBean model) {

        String fromUserName = model.getFromUserName();
        Friend friend = DaoUtils.getFriendManagerInstance().get(model.getFromUserId());
        if (friend != null) {
            fromUserName = friend.getShowName();
            if (DBFriendAction.updateFriendNameAndImage(friend, model.getFromUserName(), model.getFromUserHeadImage())) {
                EventBus.getDefault().post(friend);
            }
        }

        ChatRecordMessage record = new ChatRecordMessage();
        //record.setRecordId(1);
        record.setRecordType(MQConstant.CHAT_RECORD_TYPE_GROUP);
        record.setGruopGuid(model.getGroupGuid());
        record.setGroupName(model.getGroupName());
        record.setGroupImage(model.getGroupImage());
        record.setRecordImage(model.getGroupImage());
        record.setFromUserId(model.getFromUserId());
        record.setFromUserName(fromUserName);
        record.setFromUserHeadImage(model.getFromUserHeadImage());
        record.setTitle(model.getGroupName());
        record.setContentType(model.getContentType());
        record.setContent(StringUtils.isBlank(fromUserName) ? "" : (fromUserName + " : ") + StringUtils.cutString(model.getContent(), 25));
        //record.setUnreadCount(1);
        record.setSendTime(model.getSendTime());
        record = DaoUtils.getChatRecordMessageManagerInstance().addOrUpdate(record);

        ChatGroupMessage message = new ChatGroupMessage();
//        message.setMsgId(1);
        message.setGroupGuid(model.getGroupGuid());
        message.setGroupName(model.getGroupName());
        message.setGroupImage(model.getGroupImage());
        message.setFromUserId(model.getFromUserId());
        message.setFromUserName(fromUserName);
        message.setFromUserHeadImage(model.getFromUserHeadImage());
        message.setContentType(model.getContentType());
        message.setContent(model.getContent());
        message.setLocalUrl("");
        message.setPreviewUrl(model.getPreviewUrl());
        message.setOriginalUrl(model.getOriginalUrl());
        message.setImageWidth(model.getImageWidth());
        message.setImageHeight(model.getImageHeight());
        message.setPlayTime(model.getPlayTime());
        message.setSendTime(model.getSendTime());
        DaoUtils.getChatGroupMessageManagerInstance().save(message);

        EventBus.getDefault().post(record);
        EventBus.getDefault().post(message);
        MessageNotifyManage.play(DaoUtils.getGroupInfoManagerInstance().getGroupSettingIsShield(record.getGruopGuid()));
    }
}
