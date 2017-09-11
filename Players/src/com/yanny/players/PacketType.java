package com.yanny.players;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.util.HashMap;

enum PacketType {
    PING((short)            0x0000, PingPacket.class),
    HANDSHAKE((short)       0x0001, HandshakePacket.class),
    LOCATION((short)        0x0002, LocationPacket.class),
    ;

    @NotNull private static final HashMap<Short, PacketType> MAP = new HashMap<>();

    final short type;
    @NotNull private final Class<? extends Packet> packetClass;

    static {
        for (PacketType packetType : values()) {
            MAP.put(packetType.type, packetType);
        }
    }

    PacketType(short type, @NotNull Class<? extends Packet> packetClass) {
        this.type = type;
        this.packetClass = packetClass;
    }

    @NotNull
    public Packet createPacket(@NotNull Player player, @NotNull DataInputStream inputStream) {
        try {
            return packetClass.getDeclaredConstructor(Player.class, DataInputStream.class).newInstance(player, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    @Nullable
    public static PacketType fromValue(short type) {
        return MAP.get(type);
    }
}
