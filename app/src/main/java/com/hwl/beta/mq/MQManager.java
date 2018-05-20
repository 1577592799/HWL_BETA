package com.hwl.beta.mq;

import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Administrator on 2018/2/3.
 */

public class MQManager {

    public final static String HWL_DEFAULT_EXCHANGE = "hwl.amq.direct";
    public final static String HWL_EXCHANGE_MODEL = "direct";

    private static MQConnection mqconn;
    private static Channel channel;
    private static IConnectionStatus connStatus;

    static {
        initConnection();
    }

    private static ConnectionFactory defaultConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.4");
        factory.setPort(5672);
        factory.setUsername("liyang");
        factory.setPassword("ya.li.4321");
        return factory;
    }

    /**
     * 一个客户端只能维护一个长连接
     * 初始化连接，并绑定默认通道
     */
    private static synchronized void initConnection() {
        if (mqconn == null || !mqconn.isConnectionOpen()) {
            mqconn = new MQConnection(defaultConnectionFactory(), connStatus);
        }
    }

    private static synchronized void createChannel() throws IOException {
        if (channel == null || !channel.isOpen()) {
            initConnection();
            channel = mqconn.getChannel();
            channel.exchangeDeclare(HWL_DEFAULT_EXCHANGE, HWL_EXCHANGE_MODEL);
        }
    }

    /*
     * 绑定到队列后，获取队列的消息数量
     * */
    public static int bindQueue(String queueName) throws Exception {
        createChannel();
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, HWL_DEFAULT_EXCHANGE, queueName);
        return declareOk.getMessageCount();
    }

    public static void sendMessage(String queueName, byte[] messageBodyBytes) throws Exception {
        createChannel();
        channel.queueDeclare(queueName, true, false, false, null);
        channel.basicPublish(HWL_DEFAULT_EXCHANGE, queueName, null, messageBodyBytes);
        Log.d("MQManager-sendMessage", "当前发送的消息为: " + queueName + ",消息长度为 " + messageBodyBytes.length);
    }

    public static void receiveMessage(String queueName, final IReceiveMessageCallBack callBack) throws Exception {
        createChannel();
        boolean autoAck = false;
        channel.queueDeclare(queueName, true, false, false, null);
        channel.basicConsume(queueName, autoAck, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    callBack.onSuccess(body);
                } catch (Exception e) {
                    callBack.onFaild(e.getMessage());
                }
            }
        });
    }

//    /*
//     * 一个客户端可以有多个数据通道，这里先只实例化一个
//     * */
//    public static synchronized Channel channel {
//        if (mqconn == null || !mqconn.isConnectionOpen()) {
//            initConnection();
//        }
//        if (channel == null || !channel.isOpen()) {
//            channel = mqconn.channel;
//        }
//        return channel;
//    }

    public static void closeMQConnection() {
        if (mqconn != null) {
            new Thread() {
                @Override
                public void run() {
                    mqconn.closeChannel(channel);
                    mqconn.closeConnection();
                    mqconn = null;
                }
            }.start();
        }
    }

    public static void registerConnectionStatusEvent(IConnectionStatus connectionStatus) {
        if (connStatus == null && connectionStatus != null) {
            connStatus = connectionStatus;
        }
    }

    public interface IReceiveMessageCallBack {

        void onSuccess(byte[] messageContent);

        void onFaild(String exceptionInfo);

    }

}
