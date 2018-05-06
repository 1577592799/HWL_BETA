package com.hwl.beta.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.hwl.beta.HWLApp;

/**
 * Created by Administrator on 2018/2/5.
 */

public class MessageCountSP {
    private static final String MESSAGECOUNTPREFERENCE = "com.hwl.beta.message.count";
    private static final String FRIENDREQUESTCOUNT = "friendrequestcount";
    private static final String CHATMESSAGECOUNT = "chatmessagecount";
    private static final String NEARCIRCLEMESSAGECOUNT = "nearcirclemessagecount";


    private static SharedPreferences getSP() {
        return HWLApp.getContext().getSharedPreferences(MESSAGECOUNTPREFERENCE, Context.MODE_PRIVATE);
    }

    public static int getFriendRequestCount() {
        return getSP().getInt(FRIENDREQUESTCOUNT, 0);
    }

    public static String getFriendRequestCountDesc() {
        int count = getFriendRequestCount();
        if (count <= 0) return "0";
        return count > 99 ? "99+" : count + "";
    }

    public static void setFriendRequestCount(int count) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(FRIENDREQUESTCOUNT, count);
        editor.commit();
    }

    public static String getChatMessageCountDesc() {
        int count = getChatMessageCount();
        if (count <= 0) return "0";
        return count > 99 ? "99+" : count + "";
    }

    public static int getChatMessageCount() {
        return getSP().getInt(CHATMESSAGECOUNT, 0);
    }

    public static void setChatMessageCount(int count) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(CHATMESSAGECOUNT, count);
        editor.commit();
    }

    public static void setNearCircleMessageCountAuto() {
        int count = getNearCircleMessageCount();
        count++;
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(NEARCIRCLEMESSAGECOUNT, count);
        editor.commit();
    }

    public static int getNearCircleMessageCount() {
        return getSP().getInt(NEARCIRCLEMESSAGECOUNT, 0);
    }
}
