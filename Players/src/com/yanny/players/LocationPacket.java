package com.yanny.players;

import com.yanny.interfaces.ServerInterface;
import com.yanny.utils.Location;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

class LocationPacket extends Packet {

    LocationPacket(@NotNull Player player, @NotNull DataInputStream inputStream) {
        super(player, inputStream);
    }

    @Override
    boolean parse() throws IOException {
        return false; // only send
    }

    @Override
    boolean execute(@NotNull ServerInterface server) {
        return false; // only send
    }

    @Override
    byte[] toByte(@NotNull ServerInterface server, @NotNull Object... args) throws IOException {
        Location location = (Location) args[0];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);
        byte[] data = location.toByte();

        dos.writeShort(Short.BYTES + Location.BYTES); // length
        dos.writeShort(PacketType.LOCATION.type);
        dos.write(data);

        byte[] output = outputStream.toByteArray();

        dos.close();
        return output;
    }
}
