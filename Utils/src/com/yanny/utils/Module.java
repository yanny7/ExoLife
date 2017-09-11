package com.yanny.utils;

import org.jetbrains.annotations.NotNull;

public enum Module {
    COMM("COMM"),
    PLAYERS("PLRS"),
    PACKET("PCK"),
    ;

    @NotNull final String name;

    Module(@NotNull String name) {
        this.name = name;
    }
}
