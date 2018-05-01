package com.hwl.beta.mq;

/**
 * Created by Administrator on 2018/2/4.
 */

public class MQConstant {

    public final static byte FRIEND_REQUEST = 1;//添加好友的请求验证
    public final static byte CHAT_FRIEND_REQUEST = 2;//添加成功后的消息
    public final static byte CHAT_USER_MESSAGE = 3;
    public final static byte CHAT_GROUP_MESSAGE = 4;
    public final static byte GROUP_CREATE_MESSAGE = 5;

    public final static int CHAT_RECORD_TYPE_USER = 1;
    public final static int CHAT_RECORD_TYPE_GROUP = 2;

    public final static int CHAT_MESSAGE_CONTENT_TYPE_WORD = 1;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_IMAGE = 2;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_SOUND = 3;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_VIDEO = 4;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_WELCOME_TIP = 5;

    public final static int CHAT_SEND_SENDING = 1;
    public final static int CHAT_SEND_SUCCESS = 2;
    public final static int CHAT_SEND_FAILD = 3;
}
