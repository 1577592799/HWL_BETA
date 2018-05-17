package com.hwl.beta.mq.receive;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hwl.beta.mq.MQManager;
import com.hwl.beta.sp.UserSP;
import com.hwl.beta.utils.ByteUtils;
import com.hwl.beta.utils.NetworkUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/3/31.
 */

public class MessageReceive {
    public final static String TAG = "MQMessageReceive";
    public final static String GROUP_QUEUE_NAME = "group-queue";
    public final static int INTERVAL_TIME_SECONDS = 10;
    public final static int MAX_MESSAGE_COUNT = 2;//指定当消息达到多少时，需要提示用户处理
    private static int position = 0;
    private static Thread receiveThread = null;
    private static Observable intervalObservable = null;
    private static boolean isClose = false;
//    private static IMessageReadSpeed messageReadSpeed;
//
//    public static void setMessageReadSpeed(IMessageReadSpeed messageReadSpeedListener) {
//        messageReadSpeed = messageReadSpeedListener;
//    }

    public static String getMessageQueueName(long userId) {
        if (userId <= 0) return "user-none-queue";
        return String.format("user-%s-queue", userId);
    }

    public static <T> byte[] convertToBytes(T t) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        return gson.toJson(t).getBytes();
    }

//    /*
//    * 绑定到队列后，获取队列的消息数量
//    * */
//    public static int bindUserQueue() {
//        try {
//            return MQManager.bindQueue(getMessageQueueName(UserSP.getUserId()));
//        } catch (IOException e) {
//            Log.d(TAG, e.getMessage());
//            return 0;
//        }
//    }

    public static void start() {
        receiveThread = new Thread() {
            @Override
            public void run() {
                try {
                    String queueName = getMessageQueueName(UserSP.getUserId());
                    MQManager.bindQueue(queueName);
                    MQManager.receiveMessage(queueName, new MQManager.IReceiveMessageCallBack() {
                        @Override
                        public void onSuccess(byte[] messageContent) {
                            if (messageContent != null && messageContent.length > 0) {
                                MessageForward mf = new MessageForward();
                                mf.setMessageType(messageContent[0]);
                                mf.setBody(ByteUtils.splitBodyBytes(messageContent));
                                mf.process();

                                //String bodyJson = new String(bodyBytes);
                                //Log.d(TAG, messageType + " 当前的消息内容为：" + bodyJson);
                            }

//                    position++;
//                    if (messageReadSpeed != null) {
//                        messageReadSpeed.Process(position);
//                    }
                        }

                        @Override
                        public void onFaild(String exceptionInfo) {
                            Log.d(TAG, "消息接收错误-1：" + exceptionInfo);
                            MQManager.closeMQConnection();
                            checkConnection();
                        }
                    });
                } catch (Exception e) {
                    Log.d(TAG, "消息接收错误-2：" + e.getMessage());
                    MQManager.closeMQConnection();
                    checkConnection();
                }
            }
        };
        receiveThread.start();
    }

    private static void restartReceive() {
        isClose = false;
        if (receiveThread != null) {
            receiveThread = null;
        }
        start();
    }

    public static void stop() {
        if (receiveThread != null) {
            receiveThread.interrupt();
            receiveThread = null;
            MQManager.closeMQConnection();
            isClose = true;
        }
    }

    public static void stopCheck() {
        intervalObservable = null;
    }

    public static void checkConnection() {
        if (isClose) return;
        if (intervalObservable != null) return;
        intervalObservable = Observable.interval(INTERVAL_TIME_SECONDS, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io());

        intervalObservable.subscribe(new Observer() {
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(Object o) {
                Log.d(TAG, "onNext MQ连接循环检测中...");
                if (NetworkUtils.isConnected()) {
                    restartReceive();
                    disposable.dispose();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });

//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        if(NetworkUtils.isConnected()){
//                            receiveThread.start();
//                        }
//                    }
//                })

    }
}
