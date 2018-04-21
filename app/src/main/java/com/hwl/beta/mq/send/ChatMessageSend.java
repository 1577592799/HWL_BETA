package com.hwl.beta.mq.send;

import android.util.Log;

import com.google.gson.Gson;
import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.MQManager;
import com.hwl.beta.mq.bean.ChatFriendRequestBean;
import com.hwl.beta.mq.bean.ChatGroupMessageBean;
import com.hwl.beta.mq.bean.ChatUserMessageBean;
import com.hwl.beta.mq.receive.MessageReceive;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.ByteUtils;

import java.io.IOException;
import java.util.Date;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/2.
 */

public class ChatMessageSend {
    //组消息格式：byte[]={chat-message-type,chat-send-user-id-length(byte),chat-group-guid-lenght(byte),chat-send-user-id(byte[]),chat-group-guid(byte[]),chat-message-content(byte[])}

    public static Observable<Boolean> sendChatFriendRequestMessage(long friendId, String content) {
        final ChatFriendRequestBean message = new ChatFriendRequestBean();
        message.setFromUserId(UserSP.getUserId());
        message.setFromUserSymbol(UserSP.getUserSymbol());
        message.setFromUserName(UserSP.getUserShowName());
        message.setFromUserHeadImage(UserSP.getUserHeadImage());
        message.setToUserId(friendId);
        message.setContentType(MQConstant.CHAT_MESSAGE_CONTENT_TYPE_WORD);
        message.setContent(content);
        message.setSendTime(new Date());

        return Observable.just(message)
                .subscribeOn(Schedulers.io())
                .map(new Function<ChatFriendRequestBean, Boolean>() {
                    @Override
                    public Boolean apply(ChatFriendRequestBean bean) throws Exception {
                        MQManager.sendMessage(MessageReceive.getMessageQueueName(bean.getToUserId()),
                                ByteUtils.mergeToStart(MQConstant.CHAT_FRIEND_REQUEST,
                                        MessageReceive.convertToBytes(bean)));
                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Boolean> sendChatUserMessage(long toUserId, int contentType, String content) {
        return sendChatUserMessage(toUserId, contentType, content, null, null, content.length(), 0);
    }

    public static Observable<Boolean> sendChatUserMessage(long toUserId,
                                                          int contentType,
                                                          String content,
                                                          String previewUrl,
                                                          String originalUrl,
                                                          long size,
                                                          long playTime) {
        ChatUserMessageBean message = new ChatUserMessageBean();
        message.setFromUserId(UserSP.getUserId());
        message.setFromUserName(UserSP.getUserShowName());
        message.setFromUserHeadImage(UserSP.getUserHeadImage());
        message.setToUserId(toUserId);
        message.setContentType(contentType);
        message.setContent(content);
        message.setPreviewUrl(previewUrl);
        message.setOriginalUrl(originalUrl);
        message.setSize(size);
        message.setPlayTime(playTime);
        message.setSendTime(new Date());


        return Observable.just(message)
                .subscribeOn(Schedulers.io())
                .map(new Function<ChatUserMessageBean, Boolean>() {
                    @Override
                    public Boolean apply(ChatUserMessageBean bean) throws Exception {
                        MQManager.sendMessage(MessageReceive.getMessageQueueName(bean.getToUserId()),
                                ByteUtils.mergeToStart(MQConstant.CHAT_USER_MESSAGE,
                                        MessageReceive.convertToBytes(bean)));
                        return true;
                    }
                });
    }

    public static Observable<Boolean> sendChatGroupMessage(String groupGuid, String groupName, int contentType, String content) {
        return sendChatGroupMessage(groupGuid, groupName, contentType, content, null, null, 0, 0, content.length(), 0);
    }

    public static Observable<Boolean> sendChatGroupMessage(String groupGuid,
                                                           String groupName,
                                                           int contentType,
                                                           String content,
                                                           String previewUrl,
                                                           String originalUrl,
                                                           int imageWidth,
                                                           int imageHeigth,
                                                           long size,
                                                           long playTime) {
        ChatGroupMessageBean message = new ChatGroupMessageBean();
        message.setGroupGuid(groupGuid);
        message.setGroupName(groupName);
//        message.setGroupImage(groupImage);
        NetUserInfo user = UserSP.getUserInfo();
        message.setFromUserId(user.getId());
        message.setFromUserName(user.getShowName());
        message.setFromUserHeadImage(user.getHeadImage());
        message.setContentType(contentType);
        message.setContent(content);
        message.setPreviewUrl(previewUrl);
        message.setOriginalUrl(originalUrl);
        message.setImageWidth(imageWidth);
        message.setImageHeight(imageHeigth);
        message.setSize(size);
        message.setPlayTime(playTime);
        message.setSendTime(new Date());

        byte[] userIdBytes = ByteUtils.intToBytes((int) message.getFromUserId());
        byte[] groupIdBytes = groupGuid.getBytes();
        byte[] contentBytes = MessageReceive.convertToBytes(message);
        byte[] messageBytes = new byte[3 + userIdBytes.length + groupIdBytes.length + contentBytes.length];

        messageBytes[0] = MQConstant.CHAT_GROUP_MESSAGE;
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
