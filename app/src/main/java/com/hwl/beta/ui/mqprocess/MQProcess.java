package com.hwl.beta.ui.mqprocess;

import com.hwl.beta.mq.MQManager;
import com.hwl.beta.mq.receive.MessageProcess;

/**
 * Created by Administrator on 2018/3/31.
 */

public class MQProcess {

    public static void register() {
        MessageProcess.registerFriendRequestProcess(new FriendRequestProcess());
        MessageProcess.registerFriendDeleteMessageProces(new FriendDeleteMessageProcess());
        MessageProcess.registerChatFriendRequestProcess(new ChatFriendRequestProcess());
        MessageProcess.registerChatUserMessageProcess(new ChatUserMessageProcess());
        MessageProcess.registerChatGroupMessageProcess(new ChatGroupMessageProcess());
        MessageProcess.registerGroupCreateMessageProces(new GroupCreateMessageProcess());
        MessageProcess.registerGroupUsersAddMessageProcess(new GroupUsersAddMessageProcess());
        MessageProcess.registerGroupEditMessageProces(new GroupEditMessageProcess());
        MessageProcess.registerNearCircleMessageProcess(new NearCircleMessageProcess());
        MQManager.registerConnectionStatusEvent(new DefaultConnectionStatus());
    }

}
