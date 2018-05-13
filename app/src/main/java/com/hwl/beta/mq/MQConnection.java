package com.hwl.beta.mq;

import com.rabbitmq.client.BlockedListener;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;

/**
 * Created by Administrator on 2018/2/3.
 */

public class MQConnection {

    private IConnectionStatus connectionStatus;
    private ConnectionFactory factory;
    private Connection currConnection;

    public MQConnection(ConnectionFactory factory, IConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
        this.factory = factory;
    }

    public boolean isConnectionOpen() {
        if (currConnection == null) return false;
        return currConnection.isOpen();
    }

    public Connection getConnection() {
        if (currConnection == null) {
            try {
                currConnection = factory.newConnection();
                connectionStatus.onConnectionSuccess(currConnection);
                bindConnectionEvent();
            } catch (Exception e) {
                currConnection = null;
                connectionStatus.onBuildConnError(e.getMessage());
            }
        }
        return currConnection;
    }

    public Channel getChannel() {
        if (currConnection == null) {
            currConnection = getConnection();
        } else if (!currConnection.isOpen()) {
            connectionStatus.onBuildConnError("MQ连接已经断开");
            return null;
        }

        Channel channel = null;
        try {
            channel = currConnection.createChannel();
        } catch (Exception e) {
            connectionStatus.onBuildChannelError(e.getMessage());
        }
        return channel;
    }

    private void bindConnectionEvent() {
        if (currConnection != null) {
            currConnection.addShutdownListener(new ShutdownListener() {
                @Override
                public void shutdownCompleted(ShutdownSignalException cause) {
                    if (connectionStatus != null) {
                        connectionStatus.onDisconnected(cause.getMessage());
                    }
                }
            });

            currConnection.addBlockedListener(new BlockedListener() {
                @Override
                public void handleBlocked(String reason) {
                    connectionStatus.onBlocked(reason);
                }

                @Override
                public void handleUnblocked() {

                }
            });

            currConnection.removeBlockedListener(new BlockedListener() {
                @Override
                public void handleBlocked(String reason) {
                    connectionStatus.onBlocked(reason);
                }

                @Override
                public void handleUnblocked() throws IOException {

                }
            });
        }
    }

    public void closeChannel(Channel channel) {
        if (channel != null) {
            if (channel.isOpen()) {
                try {
                    channel.close();
                } catch (Exception e) {
                } finally {
                    channel = null;
                }
            } else {
                channel = null;
            }
        }
    }

    public void closeConnection() {
        if (currConnection != null) {
            if (currConnection.isOpen()) {
                try {
                    currConnection.close();
                } catch (IOException e) {
                } finally {
                    currConnection = null;
                }
            } else {
                currConnection = null;
            }
        }
    }
}
