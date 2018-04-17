package com.hwl.beta.mq.receive;

/**
 * Created by Administrator on 2018/2/5.
 */

public interface IMessageProcess<T> {
    void execute(byte messageType, T message);
}
