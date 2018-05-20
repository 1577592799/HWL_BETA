package com.hwl.beta.mq.receive;

import com.hwl.beta.mq.bean.ChatFriendRequestBean;
import com.hwl.beta.mq.bean.ChatGroupMessageBean;
import com.hwl.beta.mq.bean.ChatUserMessageBean;
import com.hwl.beta.mq.bean.CircleCommentMessageBean;
import com.hwl.beta.mq.bean.CircleLikeMessageBean;
import com.hwl.beta.mq.bean.FriendDeleteMessageBean;
import com.hwl.beta.mq.bean.FriendRequestBean;
import com.hwl.beta.mq.bean.GroupCreateMessageBean;
import com.hwl.beta.mq.bean.GroupEditMessageBean;
import com.hwl.beta.mq.bean.GroupUsersAddMessageBean;
import com.hwl.beta.mq.bean.NearCircleLikeMessageBean;
import com.hwl.beta.mq.bean.NearCircleCommentMessageBean;
import com.hwl.beta.mq.bean.UserLogoutMessageBean;
import com.hwl.beta.mq.bean.UserRejectChatMessageBean;
import com.hwl.beta.ui.mqprocess.FriendRequestProcess;

/**
 * Created by Administrator on 2018/2/5.
 */

public class MessageProcess {
    //在这里注册消息处理
    private static IMessageProcess<UserLogoutMessageBean> userLogoutMessageProcess;
    private static IMessageProcess<FriendRequestBean> friendRequestProcess;
    private static IMessageProcess<ChatFriendRequestBean> chatFriendRequestProcess;
    private static IMessageProcess<ChatUserMessageBean> chatUserMessageProcess;
    private static IMessageProcess<ChatGroupMessageBean> chatGroupMessageProcess;
    private static IMessageProcess<GroupCreateMessageBean> groupCreateMessageProcess;
    private static IMessageProcess<GroupEditMessageBean> groupEditMessageProcess;
    private static IMessageProcess<FriendDeleteMessageBean> friendDeleteMessageProcess;
    private static IMessageProcess<GroupUsersAddMessageBean> groupUsersAddMessageProcess;
    private static IMessageProcess<NearCircleLikeMessageBean> nearCircleLikeMessageProcess;
    private static IMessageProcess<NearCircleCommentMessageBean> nearCircleCommentMessageProcess;
    private static IMessageProcess<CircleLikeMessageBean> circleLikeMessageProcess;
    private static IMessageProcess<CircleCommentMessageBean> circleCommentMessageProcess;
    private static IMessageProcess<UserRejectChatMessageBean> userRejectChatMessageProcess;

    public static IMessageProcess<UserRejectChatMessageBean> getUserRejectChatMessageProcess() {
        return userRejectChatMessageProcess;
    }

    public static void registerUserRejectChatMessageProcess(IMessageProcess<UserRejectChatMessageBean> process) {
        userRejectChatMessageProcess = process;
    }

    public static IMessageProcess<UserLogoutMessageBean> getUserLogoutMessageProcess() {
        return userLogoutMessageProcess;
    }

    public static void registerUserLogoutMessageProcess(IMessageProcess<UserLogoutMessageBean> process) {
        userLogoutMessageProcess = process;
    }

    public static IMessageProcess<CircleCommentMessageBean> getCircleCommentMessageProcess() {
        return circleCommentMessageProcess;
    }

    public static void registerCircleCommentMessageProcess(IMessageProcess<CircleCommentMessageBean> process) {
        circleCommentMessageProcess = process;
    }

    public static IMessageProcess<CircleLikeMessageBean> getCircleLikeMessageProcess() {
        return circleLikeMessageProcess;
    }

    public static void registerCircleLikeMessageProcess(IMessageProcess<CircleLikeMessageBean> process) {
        circleLikeMessageProcess = process;
    }

    public static IMessageProcess<NearCircleCommentMessageBean> getNearCircleCommentMessageProcess() {
        return nearCircleCommentMessageProcess;
    }

    public static void registerNearCircleCommentMessageProcess(IMessageProcess<NearCircleCommentMessageBean> process) {
        nearCircleCommentMessageProcess = process;
    }

    public static IMessageProcess<NearCircleLikeMessageBean> getNearCircleLikeMessageProcess() {
        return nearCircleLikeMessageProcess;
    }

    public static void registerNearCircleLikeMessageProcess(IMessageProcess<NearCircleLikeMessageBean> process) {
        nearCircleLikeMessageProcess = process;
    }

    public static IMessageProcess<GroupUsersAddMessageBean> getGroupUsersAddMessageProcess() {
        return groupUsersAddMessageProcess;
    }

    public static void registerGroupUsersAddMessageProcess(IMessageProcess<GroupUsersAddMessageBean> process) {
        groupUsersAddMessageProcess = process;
    }

    public static IMessageProcess<FriendDeleteMessageBean> getFriendDeleteMessageProcess() {
        return friendDeleteMessageProcess;
    }

    public static void registerFriendDeleteMessageProces(IMessageProcess<FriendDeleteMessageBean> process) {
        friendDeleteMessageProcess = process;
    }

    public static IMessageProcess<GroupEditMessageBean> getGroupEditMessageProcess() {
        return groupEditMessageProcess;
    }

    public static void registerGroupEditMessageProces(IMessageProcess<GroupEditMessageBean> process) {
        groupEditMessageProcess = process;
    }

    public static IMessageProcess<GroupCreateMessageBean> getGroupCreateMessageProcess() {
        return groupCreateMessageProcess;
    }

    public static void registerGroupCreateMessageProces(IMessageProcess<GroupCreateMessageBean> process) {
        groupCreateMessageProcess = process;
    }

    public static IMessageProcess<ChatUserMessageBean> getChatUserMessageProcess() {
        return chatUserMessageProcess;
    }

    public static void registerChatUserMessageProcess(IMessageProcess<ChatUserMessageBean> process) {
        chatUserMessageProcess = process;
    }

    public static IMessageProcess<ChatGroupMessageBean> getChatGroupMessageProcess() {
        return chatGroupMessageProcess;
    }

    public static void registerChatGroupMessageProcess(IMessageProcess<ChatGroupMessageBean> process) {
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
