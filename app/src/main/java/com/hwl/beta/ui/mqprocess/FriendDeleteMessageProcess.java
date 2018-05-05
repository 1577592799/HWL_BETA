package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.db.DaoUtils;
import com.hwl.beta.mq.bean.FriendDeleteMessageBean;
import com.hwl.beta.mq.receive.IMessageProcess;
import com.hwl.beta.ui.busbean.EventDeleteFriend;

import org.greenrobot.eventbus.EventBus;

public class FriendDeleteMessageProcess implements IMessageProcess<FriendDeleteMessageBean> {
    @Override
    public void execute(byte messageType, FriendDeleteMessageBean message) {
        if (message == null || message.getActionUserId() <= 0 || message.getFriendUserId() <= 0)
            return;

        DaoUtils.getFriendManagerInstance().deleteFriend(message.getActionUserId());
        EventBus.getDefault().post(new EventDeleteFriend(message.getActionUserId()));
    }
}
