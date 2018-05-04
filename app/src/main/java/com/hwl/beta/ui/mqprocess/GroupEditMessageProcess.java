package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.bean.GroupEditMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

public class GroupEditMessageProcess implements IMessageProcess<GroupEditMessageBean> {
    @Override
    public void execute(byte messageType, GroupEditMessageBean model) {
        if (model == null) return;
        GroupInfo groupInfo = null;
        if (messageType == MQConstant.GROUP_EDIT_NAME_MESSAGE) {
            groupInfo = DaoUtils.getGroupInfoManagerInstance().setGroupName(model.getGroupGuid(), model.getGroupName());
        } else if (messageType == MQConstant.GROUP_EDIT_NOTE_MESSAGE) {
            groupInfo = DaoUtils.getGroupInfoManagerInstance().setGroupNote(model.getGroupGuid(), model.getGroupNote());
        } else if (messageType == MQConstant.GROUP_EDIT_USER_NAME_MESSAGE) {
            groupInfo = DaoUtils.getGroupInfoManagerInstance().get(model.getGroupGuid());
            DaoUtils.getGroupUserInfoManagerInstance().setUserName(model.getGroupGuid(), model.getUserId(), model.getUserName());
            return;
        }
        if (groupInfo == null) return;

        ChatRecordMessage record = new ChatRecordMessage();
        //record.setRecordId(1);
        record.setRecordType(MQConstant.CHAT_RECORD_TYPE_GROUP);
        record.setGruopGuid(model.getGroupGuid());
        record.setGroupName(groupInfo.getGroupName());
        record.setGroupImage("");
        record.setRecordImage("");
//        record.setFromUserId(model.getBuildUserId());
//        record.setFromUserName(model.getBuildUserName());
//        record.setFromUserHeadImage(model.getBuildUserImage());
        record.setTitle(StringUtils.cutString(groupInfo.getGroupName(), 18));
        record.setContentType(model.getContentType());
        record.setContent(StringUtils.cutString(model.getContent(), 25));
        //record.setUnreadCount(1);
        record.setSendTime(new Date());
        record = DaoUtils.getChatRecordMessageManagerInstance().addOrUpdate(record);
        EventBus.getDefault().post(record);

        ChatGroupMessage message = new ChatGroupMessage();
//        message.setMsgId(1);
        message.setGroupGuid(model.getGroupGuid());
        message.setGroupName(groupInfo.getGroupName());
//        message.setGroupImage(model.getGroupImage());
//        message.setFromUserId(model.getBuildUserId());
//        message.setFromUserName(model.getBuildUserName());
//        message.setFromUserHeadImage(model.getBuildUserImage());
        message.setContentType(model.getContentType());
        message.setContent(model.getContent());
        message.setLocalUrl("");
        DaoUtils.getChatGroupMessageManagerInstance().save(message);
        EventBus.getDefault().post(message);
    }
}
