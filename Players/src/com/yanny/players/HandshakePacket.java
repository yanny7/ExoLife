package com.yanny.players;

import com.yanny.interfaces.ServerInterface;
import com.yanny.utils.Location;
import com.yanny.utils.Log;
import com.yanny.utils.Module;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class HandshakePacket extends Packet {
    private int clientVersion = -1;
    private String playerName = null;

    HandshakePacket(@NotNull Player player, @NotNull DataInputStream inputStream) {
        super(player, inputStream);
    }

    @Override
    public boolean parse() throws IOException {
        DataInputStream inputStream = getInputStream();
        byte length;
        byte[] data;

        clientVersion = inputStream.readInt();
        length = inputStream.readByte();
        data = new byte[length];

        int remaining = length;
        int transmitted;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        do {
            transmitted = inputStream.read(data, 0, remaining);
            bos.write(data, 0, transmitted);
            remaining -= transmitted;
        } while (remaining > 0);

        playerName = new String(bos.toByteArray());
        bos.close();
        return true;
    }

    @Override
    public boolean execute(@NotNull ServerInterface server) {
        Log.log(Module.PACKET).info(String.format("HANDSHAKE packet from %s: name %s, version: %d", getPlayer().getUUID(), playerName, clientVersion));
        byte[] output;
        boolean success = true;
        Player player = getPlayer();
        PlayerState playerState = getPlayer().getState();

        if (server.getMinClientVersion() < clientVersion) {
            success = false;
        }

        try {
            output = toByte(server, (byte) (success ? 1 : 0));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!server.getCommunication().sendData(output, player.getSocket())) {
            Log.log(Module.PACKET).warning("Cant send response");
        }

        if (success) {
            Location location = server.getGame().findRandomLocation(); //TODO stored or new location
            Packet locationPacket = new LocationPacket(player, getInputStream());
            byte[] locationData;

            try {
                locationData = locationPacket.toByte(server, location);
            } catch (IOException e) {
                Log.log(Module.PACKET).warning("Cant create location packet");
                e.printStackTrace();
                return false;
            }

            if (playerState == PlayerState.UNINITIALIZED) {
                player.setClientVersion(clientVersion);
                player.setPlayerName(playerName);
                player.setState(PlayerState.PLAY);
                player.setLocation(location);
            }

            if (!server.getCommunication().sendData(locationData, player.getSocket())) {
                Log.log(Module.PACKET).warning("Cant send location packet");
            }
        }
        return true;
    }

    @Override
    public byte[] toByte(@NotNull ServerInterface server, @NotNull Object... args) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(outputStream);
        byte success = (Byte) args[0];

        dos.writeShort(Short.BYTES + Integer.BYTES + Byte.BYTES); // length
        dos.writeShort(PacketType.HANDSHAKE.type);
        dos.writeInt(server.getVersion()); // payload
        dos.writeByte(success); // success=0/failure=1

        byte[] output = outputStream.toByteArray();

        dos.close();
        return output;
    }
}
