package com.yanny.players;

import com.yanny.interfaces.ServerInterface;
import com.yanny.utils.Log;
import com.yanny.utils.Module;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class PingPacket extends Packet {

    public PingPacket(@NotNull Player player, @NotNull DataInputStream inputStream) {
        super(player, inputStream);
    }

    @Override
    public boolean parse() throws IOException {
        DataInputStream inputStream = getInputStream();
        byte value = inputStream.readByte();

        if (value != 1) {
            Log.log(Module.PACKET).warning("Invalid PING packet format");
            return false;
        }

        return true;
    }

    @Override
    public boolean execute(@NotNull ServerInterface server) {
        Log.log(Module.PACKET).info("PING packet");
        byte[] output;

        try {
            output = toByte(server);
        } catch (IOException e) {
            return false;
        }

        server.getCommunication().sendData(output, getPlayer().getSocket());
        return true;
    }

    @Override
    public byte[] toByte(@NotNull ServerInterface server, @NotNull Object... args) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);

        dos.writeShort(Short.BYTES + Byte.BYTES); // length
        dos.writeShort(PacketType.PING.type);
        dos.writeByte(1); // payload

        byte[] output = outputStream.toByteArray();

        dos.close();
        return output;
    }
}
