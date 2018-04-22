package com.hwl.beta.mq;

import android.util.Log;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * Created by Administrator on 2018/2/3.
 */

public class MQManager {

    public final static String HWL_DEFAULT_EXCHANGE = "hwl.amq.direct";
    public final static String HWL_EXCHANGE_MODEL = "direct";

    private static MQConnection mqconn;
    private static Channel channel;
    private static IConnectionStatus connStatus;

    private MQManager() {
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
     */
    private static synchronized void initConnection() {
        if (mqconn == null) {
            mqconn = new MQConnection(defaultConnectionFactory(), connStatus);
            if (mqconn == null) return;
            if (getChannel() == null) return;
            try {
                getChannel().exchangeDeclare(HWL_DEFAULT_EXCHANGE, HWL_EXCHANGE_MODEL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static Connection buildConnection() {
//        if (mqconn == null) {
//            initConnection();
//        }
//        return mqconn.getConnection();
//    }

    /*
    * 绑定到队列后，获取队列的消息数量
    * */
    public static int bindQueue(String queueName) throws IOException {
        if (getChannel() == null) return 0;
        AMQP.Queue.DeclareOk declareOk = getChannel().queueDeclare(queueName, true, false, false, null);
        getChannel().queueBind(queueName, HWL_DEFAULT_EXCHANGE, queueName);
        return declareOk.getMessageCount();
    }

    public static void sendMessage(String queueName, byte[] messageBodyBytes) throws IOException {
        if (getChannel() == null) return;
        getChannel().queueDeclare(queueName, true, false, false, null);
        channel.basicPublish(HWL_DEFAULT_EXCHANGE, queueName, null, messageBodyBytes);
        Log.d("MQManager-sendMessage", "当前发送的消息为: " + queueName + ",消息长度为 " + messageBodyBytes.length);
    }

    public static void receiveMessage(String queueName, final IReceiveMessageCallBack callBack) throws IOException {
        if (getChannel() == null) return;

        boolean autoAck = false;
        getChannel().queueDeclare(queueName, true, false, false, null);
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

    /*
    * 一个客户端可以有多个数据通道，这里先只实例化一个
    * */
    public static synchronized Channel getChannel() {
        if (mqconn == null) {
            initConnection();
        }
        if (channel == null) {
            channel = mqconn.getChannel();
        }
        return channel;
    }

    public static void closeMQConnection() {
        if (mqconn != null) {
            mqconn.closeChannel(channel);
            mqconn.closeConnection();
            mqconn = null;
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
