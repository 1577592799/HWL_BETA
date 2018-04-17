package com.hwl.beta.mq.receive;

import com.hwl.beta.mq.bean.ChatFriendRequestBean;
import com.hwl.beta.mq.bean.FriendRequestBean;
import com.hwl.beta.ui.mqprocess.FriendRequestProcess;

/**
 * Created by Administrator on 2018/2/5.
 */

public class MessageProcess {
    //在这里注册消息处理
    private static IMessageProcess<FriendRequestBean> friendRequestProcess;
    private static IMessageProcess<ChatFriendRequestBean> chatFriendRequestProcess;
    private static IMessageProcess chatUserMessageProcess;
    private static IMessageProcess chatGroupMessageProcess;

    public static IMessageProcess getChatUserMessageProcess() {
        return chatUserMessageProcess;
    }

    public static void registerChatUserMessageProcess(IMessageProcess process) {
        chatUserMessageProcess = process;
    }

    public static IMessageProcess getChatGroupMessageProcess() {
        return chatGroupMessageProcess;
    }

    public static void registerChatGroupMessageProcess(IMessageProcess process) {
        chatGroupMessageProcess = process;
    }

    public static void registerFriendRequestProcess(FriendRequestProcess process) {
        friendRequestProcess = process;
    }

    public static IMessageProcess<FriendRequestBean> getFriendRequestProcess() {
        return friendRequestProcess;
    }

    public static void registerChatFriendRequestProcess(IMessageProcess<ChatFriendRequestBean> process) {
        chatFriendRequestProcess = process;
    }

    public static IMessageProcess<ChatFriendRequestBean> getChatFriendRequestProcess() {
        return chatFriendRequestProcess;
    }

}
