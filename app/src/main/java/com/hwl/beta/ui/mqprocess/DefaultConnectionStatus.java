package com.hwl.beta.ui.mqprocess;

import android.util.Log;

import com.hwl.beta.mq.IConnectionStatus;
import com.hwl.beta.mq.receive.MessageReceive;
import com.rabbitmq.client.Connection;

/**
 * Created by Administrator on 2018/4/1.
 */

public class DefaultConnectionStatus implements IConnectionStatus {
    private final static String TAG = "DefaultConnectionStatus";

    @Override
    public void onBuildConnError(String exceptionInfo) {
        Log.d(TAG, "onBuildConnError: " + exceptionInfo);
        MessageReceive.checkConnection();
    }

    @Override
    public void onBuildChannelError(String exceptionInfo) {
        Log.d(TAG, "onBuildChannelError: " + exceptionInfo);
        MessageReceive.checkConnection();
    }

    @Override
    public void onDisconnected(String exceptionInfo) {
        Log.d(TAG, "onDisconnected: " + exceptionInfo);
        MessageReceive.checkConnection();
    }

    @Override
    public void onBlocked(String exceptionInfo) {
        Log.d(TAG, "onBlocked: " + exceptionInfo);
        MessageReceive.checkConnection();
    }

    @Override
    public void onConnectionSuccess(Connection connection) {
        MessageReceive.stopCheck();
        Log.d(TAG, "onConnectionSuccess: " + connection.toString() + " 连接成功");
    }
}
