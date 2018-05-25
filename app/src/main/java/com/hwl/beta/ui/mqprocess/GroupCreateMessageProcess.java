package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.bean.GroupCreateMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

public class GroupCreateMessageProcess implements IMessageProcess<GroupCreateMessageBean> {

    @Override
    public void execute(byte messageType, GroupCreateMessageBean model) {
        if (model == null) return;
        if (DaoUtils.getGroupInfoManagerInstance().get(model.getGroupGuid()) != null) return;

        DaoUtils.getGroupInfoManagerInstance().add(DBGroupAction.convertToGroupInfo(model.getGroupGuid(), model.getGroupName(),"",model.getBuildUserId(), model.getGroupUsers().size(), model.getGroupImage(), model.getBuildTime()));
        DaoUtils.getGroupUserInfoManagerInstance().addListAsync(DBGroupAction.convertToGroupUserInfos2(model.getGroupGuid(), model.getGroupUsers()));

        ChatRecordMessage record = new ChatRecordMessage();
        //record.setRecordId(1);
        record.setRecordType(MQConstant.CHAT_RECORD_TYPE_GROUP);
        record.setGruopGuid(model.getGroupGuid());
        record.setGroupName(model.getGroupName());
        record.setRecordImage("");
        record.setFromUserId(model.getBuildUserId());
        record.setFromUserName(model.getBuildUserName());
        record.setFromUserHeadImage(model.getBuildUserImage());
//        record.setTitle(model.getGroupName());
        record.setTitle(StringUtils.cutString(model.getGroupName(), 18));
        record.setContentType(model.getContentType());
        record.setContent(StringUtils.cutString(model.getContent(), 25));
        //record.setUnreadCount(1);
        record.setSendTime(model.getBuildTime());
        record = DaoUtils.getChatRecordMessageManagerInstance().addOrUpdate(record);
        EventBus.getDefault().post(record);

        ChatGroupMessage message = new ChatGroupMessage();
//        message.setMsgId(1);
        message.setGroupGuid(model.getGroupGuid());
        message.setGroupName(model.getGroupName());
//        message.setGroupImage(model.getGroupImage());
        message.setFromUserId(model.getBuildUserId());
        message.setFromUserName(model.getBuildUserName());
        message.setFromUserHeadImage(model.getBuildUserImage());
        message.setContentType(model.getContentType());
        message.setContent(model.getContent());
        message.setLocalUrl("");
        DaoUtils.getChatGroupMessageManagerInstance().save(message);
        EventBus.getDefault().post(message);
    }
}
