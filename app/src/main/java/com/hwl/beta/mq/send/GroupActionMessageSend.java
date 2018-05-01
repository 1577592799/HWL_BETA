package com.hwl.beta.mq.send;

import com.hwl.beta.mq.MQConstant;
import com.hwl.beta.mq.MQManager;
import com.hwl.beta.mq.bean.GroupCreateMessageBean;
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
}
