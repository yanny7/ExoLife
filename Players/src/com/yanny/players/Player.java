package com.yanny.players;

import com.yanny.interfaces.ServerInterface;
import com.yanny.utils.Location;
import com.yanny.utils.Log;
import com.yanny.utils.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

class Player {
    @NotNull private final ServerInterface server;
    @NotNull private final Socket socket;
    @NotNull private final UUID uuid;
    @NotNull private PlayerState state = PlayerState.UNINITIALIZED;

    @Nullable private String playerName = null;
    @Nullable private Location location;
    private int clientVersion = -1;

    Player(@NotNull Socket socket, @NotNull ServerInterface server) {
        this.socket = socket;
        this.server = server;
        this.uuid = UUID.nameUUIDFromBytes(socket.toString().getBytes());
    }

    void receive(@NotNull byte[] data) {
        Packet packet;

        try {
            packet = Packet.create(this, data);
        } catch (IOException e) {
            Log.log(Module.PACKET).warning("Failed create packet");
            e.printStackTrace();
            return;
        }

        if (packet != null) {
            boolean result;

            try {
                result = packet.parse();
            } catch (IOException e) {
                Log.log(Module.PACKET).warning("Invalid packet");
                e.printStackTrace();
                try {
                    packet.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return;
            }

            if (!result) {
                Log.log(Module.PACKET).warning("Failed parse packet");
                try {
                    packet.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            result = packet.execute(server);

            if (!result) {
                Log.log(Module.PACKET).warning("Failed execute packet");
                try {
                    packet.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            try {
                packet.close();
            } catch (IOException e) {
                Log.log(Module.PACKET).warning("Failed close packet resources");
                e.printStackTrace();
            }
        }
    }

    void setClientVersion(int clientVersion) {
        this.clientVersion = clientVersion;
    }

    void setPlayerName(@NotNull String playerName) {
        this.playerName = playerName;
    }

    void setState(@NotNull PlayerState state) {
        if (this.state != state) {
            Log.log(Module.PLAYERS).info(String.format("Changed state of %s: %s -> %s", uuid, this.state, state));
            this.state = state;
        }
    }

    @NotNull
    PlayerState getState() {
        return state;
    }

    void setLocation(@NotNull Location location) {
        this.location = location;
    }

    @NotNull
    UUID getUUID() {
        return uuid;
    }

    @Nullable
    public String getPlayerName() {
        return playerName;
    }

    @NotNull
    Socket getSocket() {
        return socket;
    }
}
