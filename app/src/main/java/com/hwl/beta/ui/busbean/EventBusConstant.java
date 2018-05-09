package com.hwl.beta.ui.busbean;

import android.content.Context;

import com.hwl.beta.ui.user.FragmentUser;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/4/1.
 */

public class EventBusConstant {

    public static final int EB_TYPE_FRIEND_REQUEST_UPDATE = 1;
    public static final int EB_TYPE_CHAT_MESSAGE_UPDATE = 2;
    public static final int EB_TYPE_USER_UPDATE = 3;
    public static final int EB_TYPE_USER_HEAD_UPDATE = 4;
    public static final int EB_TYPE_NETWORK_CONNECT_UPDATE = 5;
    public static final int EB_TYPE_NETWORK_BREAK_UPDATE = 6;
    public static final int EB_TYPE_NEAR_INFO_UPDATE = 7;
    public static final int EB_TYPE_CIRCLE_INFO_UPDATE = 10;
    public static final int EB_TYPE_CIRCLE_MESSAGE_UPDATE = 11;
    public static final int EB_TYPE_NEAR_CIRCLE_MESSAGE_UPDATE = 12;

    public static final int EB_TYPE_ACTINO_ADD = 20000;
    public static final int EB_TYPE_ACTINO_REMOVE = 20001;
    public static final int EB_TYPE_ACTINO_CLEAR = 20002;
    public static final int EB_TYPE_ACTINO_EXIT = 20003;
}
