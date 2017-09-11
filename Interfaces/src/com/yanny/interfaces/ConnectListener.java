package com.yanny.interfaces;

import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public interface ConnectListener {
    void connect(@NotNull Socket socket);
}
