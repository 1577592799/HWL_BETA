package com.hwl.beta.mq.send;

import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.MQManager;
import com.hwl.beta.mq.bean.GroupCreateMessageBean;
import com.hwl.beta.mq.bean.GroupEditMessageBean;
import com.hwl.beta.mq.bean.MQGroupUserInfo;
import com.hwl.beta.mq.receive.MessageReceive;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.ByteUtils;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class GroupActionMessageSend {

    public static Observable<Boolean> sendCreateMessage(String groupGuid,
                                                        String groupName,
                                                        List<String> groupImage,
                                                        Date buildTime,
                                                        String content,
                                                        List<MQGroupUserInfo> users) {
        GroupCreateMessageBean message = new GroupCreateMessageBean();
        message.setBuildUserId(UserSP.getUserId());
        message.setBuildTime(buildTime);
        message.setContentType(MQConstant.CHAT_MESSAGE_CONTENT_TYPE_WELCOME_TIP);
        message.setContent(content);
        message.setGroupGuid(groupGuid);
        message.setGroupName(groupName);
        message.setGroupImage(groupImage);
        message.setGroupUsers(users);

        byte[] userIdBytes = new byte[]{0};
        byte[] groupIdBytes = groupGuid.getBytes();
        byte[] contentBytes = MessageReceive.convertToBytes(message);
        byte[] messageBytes = new byte[3 + userIdBytes.length + groupIdBytes.length + contentBytes.length];

        messageBytes[0] = MQConstant.GROUP_CREATE_MESSAGE;
        messageBytes[1] = (byte) userIdBytes.length;
        messageBytes[2] = (byte) groupIdBytes.length;
        ByteUtils.setPositionBytes(3, userIdBytes, messageBytes);
        ByteUtils.setPositionBytes(3 + userIdBytes.length, groupIdBytes, messageBytes);
        ByteUtils.setPositionBytes(3 + userIdBytes.length + groupIdBytes.length, contentBytes, messageBytes);

        return Observable.just(messageBytes)
                .subscribeOn(Schedulers.io())
                .map(new Function<byte[], Boolean>() {
                    @Override
                    public Boolean apply(byte[] bytes) throws Exception {
                        MQManager.sendMessage(MessageReceive.GROUP_QUEUE_NAME, bytes);
                        return true;
                    }
                });
    }

    public static Observable<Boolean> sendEditGroupNameMessage(String groupGuid,
                                                               String groupName,
                                                               String content) {
        return sendEditMessage(MQConstant.GROUP_EDIT_NAME_MESSAGE,false, groupGuid, groupName, null, 0, null, content);
    }

    public static Observable<Boolean> sendEditGroupUserNameMessage(String groupGuid,
                                                                   long userId,
                                                                   String userName,
                                                                   String content) {
        return sendEditMessage(MQConstant.GROUP_EDIT_NAME_MESSAGE,false, groupGuid, null, null, userId, userName, content);
    }

    public static Observable<Boolean> sendExitGroupUserMessage(String groupGuid,
                                                               long userId,
                                                               String content) {
        return sendEditMessage(MQConstant.GROUP_EXIT_USER_MESSAGE,true, groupGuid, null, null, userId, null, content);
    }

    public static Observable<Boolean> sendDismissGroupUserMessage(String groupGuid,
                                                               long userId,
                                                               String content) {
        return sendEditMessage(MQConstant.GROUP_DISMISS_MESSAGE,true, groupGuid, null, null, userId, null, content);
    }

    public static Observable<Boolean> sendEditGroupNoteMessage(String groupGuid,
                                                               String groupNote,
                                                               String content) {
        return sendEditMessage(MQConstant.GROUP_EDIT_NOTE_MESSAGE,false, groupGuid, null, groupNote, 0, null, content);
    }

    private static Observable<Boolean> sendEditMessage(byte messageType, boolean excludeUser, String groupGuid,
                                                       String groupName,
                                                       String groupNote,
                                                       long userId,
                                                       String userName,
                                                       String content) {
        GroupEditMessageBean message = new GroupEditMessageBean();
        message.setGroupGuid(groupGuid);
        message.setGroupName(groupName);
        message.setGroupNote(groupNote);
        message.setUserId(userId);
        message.setUserName(userName);
        message.setContentType(MQConstant.CHAT_MESSAGE_CONTENT_TYPE_WELCOME_TIP);
        message.setContent(content);

        byte[] userIdBytes = null;
        if (excludeUser) {
            userIdBytes = ByteUtils.intToBytes((int) userId);
        } else {
            userIdBytes = new byte[]{0};
        }
        byte[] groupIdBytes = groupGuid.getBytes();
        byte[] contentBytes = MessageReceive.convertToBytes(message);
        byte[] messageBytes = new byte[3 + userIdBytes.length + groupIdBytes.length + contentBytes.length];

        messageBytes[0] = messageType;
        messageBytes[1] = (byte) userIdBytes.length;
        messageBytes[2] = (byte) groupIdBytes.length;
        ByteUtils.setPositionBytes(3, userIdBytes, messageBytes);
        ByteUtils.setPositionBytes(3 + userIdBytes.length, groupIdBytes, messageBytes);
        ByteUtils.setPositionBytes(3 + userIdBytes.length + groupIdBytes.length, contentBytes, messageBytes);

        return Observable.just(messageBytes)
                .subscribeOn(Schedulers.io())
                .map(new Function<byte[], Boolean>() {
                    @Override
                    public Boolean apply(byte[] bytes) throws Exception {
                        MQManager.sendMessage(MessageReceive.GROUP_QUEUE_NAME, bytes);
                        return true;
                    }
                });
    }
}
