package com.hwl.beta.mq;

import com.rabbitmq.client.Connection;

/**
 * Created by Administrator on 2018/4/1.
 */

public interface IConnectionStatus {
    void onBuildConnError(String exceptionInfo);//建立连接失败

    void onBuildChannelError(String exceptionInfo);//建立通道失败

    void onDisconnected(String exceptionInfo);//连接断开后

    void onBlocked(String exceptionInfo);

    void onConnectionSuccess(Connection connection);
}
