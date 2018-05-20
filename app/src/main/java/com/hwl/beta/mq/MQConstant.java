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
    public final static byte GROUP_EDIT_NOTE_MESSAGE = 6;
    public final static byte GROUP_EDIT_NAME_MESSAGE = 7;
    public final static byte GROUP_EDIT_USER_NAME_MESSAGE = 8;
    public final static byte GROUP_EXIT_USER_MESSAGE = 9;
    public final static byte GROUP_DISMISS_MESSAGE = 10;
    public final static byte FRIEND_DELETE_MESSAGE = 11;
    public final static byte GROUP_USERS_ADD_MESSAGE = 12;
    public final static byte NEAR_CIRCLE_LIKE_MESSAGE = 13;
    public final static byte NEAR_CIRCLE_COMMENT_MESSAGE = 14;
    public final static byte CIRCLE_LIKE_MESSAGE = 15;
    public final static byte CIRCLE_COMMENT_MESSAGE = 16;
    public final static byte USER_LOGOUT_MESSAGE = 17;
    public final static byte USER_REJECT_CHAT_MESSAGE = 18;

    public final static int CHAT_RECORD_TYPE_USER = 1;
    public final static int CHAT_RECORD_TYPE_GROUP = 2;

    public final static int CHAT_MESSAGE_CONTENT_TYPE_WORD = 1;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_IMAGE = 2;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_SOUND = 3;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_VIDEO = 4;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_REJECT = 5;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_REJECT_COZY = 6;
    public final static int CHAT_MESSAGE_CONTENT_TYPE_WELCOME_TIP = 7;

    public final static int CHAT_SEND_SENDING = 1;
    public final static int CHAT_SEND_SUCCESS = 2;
    public final static int CHAT_SEND_FAILD = 3;

    public final static int CIRCLE_MESSAGE_ACTION_ADD = 1;
    public final static int CIRCLE_MESSAGE_ACTION_DELETE = 2;
}
