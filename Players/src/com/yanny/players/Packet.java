package com.yanny.players;

import com.yanny.interfaces.ServerInterface;
import com.yanny.utils.Log;
import com.yanny.utils.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

abstract class Packet {
    @NotNull private final Player player;
    @NotNull private final DataInputStream inputStream;

    Packet(@NotNull Player player, @NotNull DataInputStream inputStream) {
        this.player = player;
        this.inputStream = inputStream;
    }

    abstract boolean parse() throws IOException;

    abstract boolean execute(@NotNull ServerInterface server);

    abstract byte[] toByte(@NotNull ServerInterface server, @NotNull Object... args) throws IOException;

    void close() throws IOException {
        inputStream.close();
    }

    @NotNull
    DataInputStream getInputStream() {
        return inputStream;
    }

    @NotNull
    Player getPlayer() {
        return player;
    }

    @Nullable
    static Packet create(@NotNull Player player, @NotNull byte[] data) throws IOException {
        if (data.length <= 1) {
            Log.log(Module.PACKET).warning("Invalid packet size");
            return null;
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        DataInputStream inputStream = new DataInputStream(bis);
        short type;

        try {
            type = inputStream.readShort();
        } catch (IOException e) {
            Log.log(Module.PACKET).warning("Failed read packet type");
            e.printStackTrace();
            inputStream.close();
            return null;
        }

        PacketType packetType = PacketType.fromValue(type);

        if (packetType != null) {
            return packetType.createPacket(player, inputStream);
        } else {
            Log.log(Module.PACKET).warning("Invalid packet type");
            inputStream.close();
            return null;
        }
    }
}
