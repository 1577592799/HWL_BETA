package com.hwl.beta.ui.common;

import android.app.Activity;
import android.util.Log;

import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;
import com.hwl.beta.HWLApp;
import com.hwl.beta.sp.AppInstallStatus;
import com.hwl.beta.utils.AppUtils;

public class OpenInstallManage {

    static final String EFFECT_POINT_ID = "hwl-user-install";
    private static AppWakeUpAdapter wakeUpAdapter = null;

    public static void init() {
        if (AppUtils.isMainProcess()) {
            OpenInstall.init(HWLApp.getContext());
        }
    }

    public static void reportRegister() {
        //用户注册成功后调用
        OpenInstall.reportRegister();
    }

    public static void addPoint() {
        OpenInstall.reportEffectPoint(EFFECT_POINT_ID, 1);
    }

    private static void loadWakeUpAdapter() {
        if (wakeUpAdapter != null) return;
        wakeUpAdapter = new AppWakeUpAdapter() {
            @Override
            public void onWakeUp(AppData appData) {
                //获取渠道数据
                String channelCode = appData.getChannel();
                //获取绑定数据
                String bindData = appData.getData();
                Log.d("OpenInstall", "getWakeUp : wakeupData = " + appData.toString());
            }
        };
    }

    public static void getWakeUp(Activity activity) {
        loadWakeUpAdapter();
        //获取唤醒参数
        OpenInstall.getWakeUp(activity.getIntent(), wakeUpAdapter);
    }

    public static void clearWakeUp() {
        wakeUpAdapter = null;
    }

    public static void getInstall() {
        if (AppInstallStatus.isFrist()) {
            //获取OpenInstall安装数据
            OpenInstall.getInstall(new AppInstallAdapter() {
                @Override
                public void onInstall(AppData appData) {
                    //获取渠道数据
                    String channelCode = appData.getChannel();
                    //获取自定义数据
                    String bindData = appData.getData();
                    Log.d("OpenInstall", "getInstall : installData = " + appData.toString());
                }
            });
        }
    }
}
