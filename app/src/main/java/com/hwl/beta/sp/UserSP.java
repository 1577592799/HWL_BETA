package com.hwl.beta.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.hwl.beta.HWLApp;
import com.hwl.beta.net.user.NetUserInfo;
import com.hwl.beta.utils.StringUtils;

/**
 * Created by Administrator on 2018/1/16.
 */

public class UserSP {
    private static final String USERPREFERENCE = "com.hwl.beta.userinfo";
    private static final String USER_ID = "Id";
    private static final String USER_EMAIL = "Email";
    private static final String USER_SYMBOL = "Symbol";
    private static final String USER_MOBILE = "Mobile";
    private static final String USER_TOKEN = "Token";
    private static final String USER_NAME = "Name";
    private static final String USER_HEADIMAGE = "HeadImage";
    private static final String USER_CIRCLEBACKIMAGE = "CircleBackImage";
    private static final String USER_USERSEX = "UserSex";
    private static final String USER_LIFENOTES = "LifeNotes";
    private static final String USER_FRIENDCOUNT = "FriendCount";

    private static SharedPreferences getSP() {
        return HWLApp.getContext().getSharedPreferences(USERPREFERENCE, Context.MODE_PRIVATE);
    }

    public static long getUserId() {
        return getSP().getLong(USER_ID, 0);
    }

    public static String getUserHeadImage() {
        return getSP().getString(USER_HEADIMAGE, null);
    }

    public static String getUserName() {
        return getSP().getString(USER_NAME, null);
    }

    public static String getUserShowName() {
        String showName = getUserName();
        if (StringUtils.isBlank(showName)) {
            showName = getUserSymbol();
        }
        return showName;
    }

    public static String getLifeNotes() {
        return getSP().getString(USER_LIFENOTES, null);
    }

    public static int getUserSex() {
        return getSP().getInt(USER_USERSEX, 2);
    }

    public static int getFriendCount() {
        return getSP().getInt(USER_FRIENDCOUNT, 0);
    }

    public static String getUserToken() {
        return getSP().getString(USER_TOKEN, null);
    }

    public static void setUserHeadImage(String headImage) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putString(USER_HEADIMAGE, headImage);
        editor.commit();
    }

    public static void setUserSymbol(String symbol) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putString(USER_SYMBOL, symbol);
        editor.commit();
    }

    public static String getUserSymbol() {
        return getSP().getString(USER_SYMBOL, null);
    }

    public static void setUserName(String userName) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public static void setUserLifeNotes(String lifeNotes) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putString(USER_LIFENOTES, lifeNotes);
        editor.commit();
    }

    public static void setUserSex(int sex) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(USER_USERSEX, sex);
        editor.commit();
    }

    public static void setFriendCount(int friendCount) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(USER_FRIENDCOUNT, friendCount);
        editor.commit();
    }

    public static void deleteOneFriendCount() {
        int count = getFriendCount();
        if (count <= 0) return;
        setFriendCount(count - 1);
    }

    public static void setUserInfo(NetUserInfo user) {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.putLong(USER_ID, user.getId());
        editor.putInt(USER_USERSEX, user.getUserSex());
        editor.putInt(USER_FRIENDCOUNT, user.getFriendCount());

        editor.putString(USER_SYMBOL, user.getSymbol());
        editor.putString(USER_EMAIL, user.getEmail());
        editor.putString(USER_MOBILE, user.getMobile());
        editor.putString(USER_NAME, user.getName());
        editor.putString(USER_TOKEN, user.getToken());
        editor.putString(USER_HEADIMAGE, user.getHeadImage());
        editor.putString(USER_CIRCLEBACKIMAGE, user.getCircleBackImage());
        editor.putString(USER_LIFENOTES, user.getLifeNotes());
        editor.commit();
    }

    public static NetUserInfo getUserInfo() {
        final SharedPreferences prefs = getSP();
        NetUserInfo user = new NetUserInfo();

        user.setId(prefs.getLong(USER_ID, 0));
        if (user.getId() <= 0) return null;
        user.setToken(prefs.getString(USER_TOKEN, null));
        if (StringUtils.isBlank(user.getToken())) {
            return null;
        }

        user.setEmail(prefs.getString(USER_EMAIL, null));
        user.setMobile(prefs.getString(USER_MOBILE, null));
        if (StringUtils.isBlank(user.getEmail()) && StringUtils.isBlank(user.getMobile())) {
            return null;
        }

        user.setSymbol(prefs.getString(USER_SYMBOL, null));
        user.setUserSex(prefs.getInt(USER_USERSEX, -1));
        user.setName(prefs.getString(USER_NAME, null));
        user.setHeadImage(prefs.getString(USER_HEADIMAGE, null));
        user.setCircleBackImage(prefs.getString(USER_CIRCLEBACKIMAGE, null));
        user.setLifeNotes(prefs.getString(USER_LIFENOTES, null));
        return user;
    }

    public static void clearUserInfo() {
        final SharedPreferences.Editor editor = getSP().edit();
        editor.clear();
        editor.commit();
    }
}
