package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.db.entity.ChatGroupMessage;
import com.hwl.beta.db.entity.ChatRecordMessage;
import com.hwl.beta.db.entity.GroupInfo;
import com.hwl.beta.db.entity.GroupUserInfo;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.bean.GroupUsersAddMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.net.group.GroupService;
import com.hwl.beta.net.group.body.GroupUsersResponse;
import com.hwl.beta.ui.common.MessageNotifyManage;
import com.hwl.beta.ui.common.rxext.NetDefaultObserver;
import com.hwl.beta.ui.convert.DBGroupAction;
import com.hwl.beta.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

public class GroupUsersAddMessageProcess implements IMessageProcess<GroupUsersAddMessageBean> {
    @Override
    public void execute(byte messageType, GroupUsersAddMessageBean message) {
        if (message == null || StringUtils.isBlank(message.getGroupGuid()))
            return;

        GroupInfo groupInfo = DaoUtils.getGroupInfoManagerInstance().get(message.getGroupGuid());
        if (groupInfo == null) {
            groupInfo = DBGroupAction.convertToGroupInfo(message.getGroupGuid(), message.getGroupName(), message.getGroupNote(), message.getBuildUserId(), 0, message.getGroupUserImages(), message.getBuildTime());
            DaoUtils.getGroupInfoManagerInstance().add(groupInfo);
            GroupService.groupUsers(message.getGroupGuid())
                    .subscribe(new NetDefaultObserver<GroupUsersResponse>() {
                        @Override
                        protected void onSuccess(GroupUsersResponse response) {
                            if (response.getGroupUserInfos() != null) {
                                List<GroupUserInfo> userInfoList = DBGroupAction.convertToGroupUserInfos(response.getGroupUserInfos());
                                DaoUtils.getGroupUserInfoManagerInstance().addListAsync(userInfoList);
                                EventBus.getDefault().post(userInfoList);
                            }
                        }
                    });
        } else {
            List<GroupUserInfo> userInfoList = DBGroupAction.convertToGroupUserInfos2(message.getGroupGuid(), message.getUserInfos());
            DaoUtils.getGroupUserInfoManagerInstance().addListAsync(userInfoList);
            EventBus.getDefault().post(userInfoList);
        }

        ChatRecordMessage record = new ChatRecordMessage();
        //record.setRecordId(1);
        record.setRecordType(MQConstant.CHAT_RECORD_TYPE_GROUP);
        record.setGruopGuid(groupInfo.getGroupGuid());
        record.setGroupName(groupInfo.getGroupName());
        record.setGroupImage("");
        record.setRecordImage("");
        record.setTitle(StringUtils.cutString(groupInfo.getGroupName(), 18));
        record.setContentType(message.getContentType());
        record.setContent(StringUtils.cutString(message.getContent(), 25));
        //record.setUnreadCount(1);
        record.setSendTime(new Date());
        record = DaoUtils.getChatRecordMessageManagerInstance().addOrUpdate(record);

        ChatGroupMessage chatGroupMessage = new ChatGroupMessage();
        chatGroupMessage.setGroupGuid(groupInfo.getGroupGuid());
        chatGroupMessage.setGroupName(groupInfo.getGroupName());
        chatGroupMessage.setContentType(message.getContentType());
        chatGroupMessage.setContent(message.getContent());
        chatGroupMessage.setLocalUrl("");
        DaoUtils.getChatGroupMessageManagerInstance().save(chatGroupMessage);

        EventBus.getDefault().post(record);
        EventBus.getDefault().post(chatGroupMessage);
        MessageNotifyManage.play();
    }
}
