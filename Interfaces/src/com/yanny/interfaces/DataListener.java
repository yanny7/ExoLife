package com.yanny.interfaces;

import org.jetbrains.annotations.NotNull;

import java.net.Socket;

public interface DataListener {
    void receive(@NotNull byte[] data, @NotNull Socket socket);
}
