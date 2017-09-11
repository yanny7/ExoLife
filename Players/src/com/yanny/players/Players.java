package com.yanny.players;

import com.yanny.interfaces.*;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Players implements PlayersInterface, ConnectListener, DisconnectListener, DataListener {
    @NotNull private final ServerInterface server;
    @NotNull private final Map<Socket, Player> players;

    public Players(@NotNull ServerInterface server) {
        this.server = server;
        players = new HashMap<>();

        CommunicationInterface communication = server.getCommunication();
        communication.setConnectListener(this);
        communication.setDisconnectListener(this);
        communication.setDataListener(this);
    }

    @Override
    public void connect(@NotNull Socket socket) {
        players.put(socket, new Player(socket, server));
    }

    @Override
    public void disconnect(@NotNull Socket socket) {
        players.remove(socket);
    }

    @Override
    public void receive(@NotNull byte[] data, @NotNull Socket socket) {
        players.get(socket).receive(data);
    }

    @Override
    public int getPlayersCount() {
        return players.size();
    }
}
