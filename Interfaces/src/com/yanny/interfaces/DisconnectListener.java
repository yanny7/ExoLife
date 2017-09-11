package com.yanny.interfaces;

import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public interface DisconnectListener {
    void disconnect(@NotNull Socket socket);
}
