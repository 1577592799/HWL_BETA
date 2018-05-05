package com.hwl.beta.mq.receive;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.bean.ChatFriendRequestBean;
import com.hwl.beta.mq.bean.ChatGroupMessageBean;
import com.hwl.beta.mq.bean.ChatUserMessageBean;
import com.hwl.beta.mq.bean.FriendRequestBean;
import com.hwl.beta.mq.bean.GroupCreateMessageBean;
import com.hwl.beta.mq.bean.GroupEditMessageBean;
import com.hwl.beta.utils.ByteUtils;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/2/5.
 */

public class MessageForward {

    private byte messageType;
    private byte[] bodyBytes;
    public static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    public void setMessageType(byte messageType) {
        this.messageType = messageType;
    }

    public void setBody(byte[] bodyBytes) {
        this.bodyBytes = bodyBytes;
    }

    public void process() {
        IMessageProcess messageProcess = null;
        String bodyJson = null;
        switch (messageType) {
            case MQConstant.FRIEND_REQUEST:
                bodyJson = new String(bodyBytes);
                messageProcess = MessageProcess.getFriendRequestProcess();
                if (messageProcess != null && StringUtils.isNotBlank(bodyJson)) {
                    messageProcess.execute(messageType, gson.fromJson(bodyJson, FriendRequestBean.class));
                }
                break;
            case MQConstant.CHAT_FRIEND_REQUEST:
                bodyJson = new String(bodyBytes);
                messageProcess = MessageProcess.getChatFriendRequestProcess();
                if (messageProcess != null && StringUtils.isNotBlank(bodyJson)) {
                    messageProcess.execute(messageType, gson.fromJson(bodyJson, ChatFriendRequestBean.class));
                }
                break;
            case MQConstant.CHAT_GROUP_MESSAGE:
                int fromUserIdLength = bodyBytes[0];
                int groupIdLength = bodyBytes[1];
                byte[] msgBytes = ByteUtils.getPositionBytes(2 + fromUserIdLength + groupIdLength, 0, bodyBytes);
                bodyJson = new String(msgBytes);
                messageProcess = MessageProcess.getChatGroupMessageProcess();
                if (messageProcess != null && StringUtils.isNotBlank(bodyJson)) {
                    messageProcess.execute(messageType, gson.fromJson(bodyJson, ChatGroupMessageBean.class));
                }
                break;
            case MQConstant.CHAT_USER_MESSAGE:
                bodyJson = new String(bodyBytes);
                messageProcess = MessageProcess.getChatUserMessageProcess();
                if (messageProcess != null && StringUtils.isNotBlank(bodyJson)) {
                    messageProcess.execute(messageType, gson.fromJson(bodyJson, ChatUserMessageBean.class));
                }
                break;
            case MQConstant.GROUP_CREATE_MESSAGE:
                int buildUserIdLength = bodyBytes[0];
                int groupCreateIdLength = bodyBytes[1];
                byte[] msgGroupBytes = ByteUtils.getPositionBytes(2 + buildUserIdLength + groupCreateIdLength, 0, bodyBytes);
                bodyJson = new String(msgGroupBytes);
                messageProcess = MessageProcess.getGroupCreateMessageProcess();
                if (messageProcess != null && StringUtils.isNotBlank(bodyJson)) {
                    messageProcess.execute(messageType, gson.fromJson(bodyJson, GroupCreateMessageBean.class));
                }
                break;
            case MQConstant.GROUP_EDIT_NAME_MESSAGE:
            case MQConstant.GROUP_EDIT_NOTE_MESSAGE:
            case MQConstant.GROUP_EDIT_USER_NAME_MESSAGE:
            case MQConstant.GROUP_EXIT_USER_MESSAGE:
            case MQConstant.GROUP_DISMISS_MESSAGE:
                int buildUserIdLength2 = bodyBytes[0];
                int groupCreateIdLength2 = bodyBytes[1];
                byte[] msgGroupBytes2 = ByteUtils.getPositionBytes(2 + buildUserIdLength2 + groupCreateIdLength2, 0, bodyBytes);
                bodyJson = new String(msgGroupBytes2);
                messageProcess = MessageProcess.getGroupEditMessageProcess();
                if (messageProcess != null && StringUtils.isNotBlank(bodyJson)) {
                    messageProcess.execute(messageType, gson.fromJson(bodyJson, GroupEditMessageBean.class));
                }
                break;
        }
//        if (messageProcess != null && !StringUtils.isBlank(bodyJson)) {
//            messageProcess.execute(messageType, bodyJson);
//        }
    }

}
