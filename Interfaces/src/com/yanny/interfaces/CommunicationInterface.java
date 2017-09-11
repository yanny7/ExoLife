package com.yanny.interfaces;

import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public interface CommunicationInterface {
    boolean start();
    void stop();
    void setConnectListener(@NotNull ConnectListener listener);
    void setDisconnectListener(@NotNull DisconnectListener listener);
    void setDataListener(@NotNull DataListener listener);
    int getConnections();
    boolean sendData(@NotNull byte[] data, @NotNull Socket socket);
}
