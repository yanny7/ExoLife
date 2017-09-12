package com.yanny.players;

import com.yanny.interfaces.*;
import org.jetbrains.annotations.NotNull;

import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Players implements PlayersInterface, ConnectListener, DisconnectListener, DataListener {
    @NotNull private final ServerInterface server;
    @NotNull private final Map<Socket, Player> players;
    @NotNull private final Connection connection;

    public Players(@NotNull ServerInterface server) throws SQLException {
        this.server = server;
        players = new HashMap<>();

        connection = DriverManager.getConnection("jdbc:sqlite:players.db");
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS players (playerName VARCHAR(127), universe BIGINT, " +
                "gcx BIGINT, gcy BIGINT, gcz BIGINT, galaxy INTEGER, scx BIGINT, scy BIGINT, scz BIGINT, star INTEGER)");
        statement.close();

        CommunicationInterface communication = server.getCommunication();
        communication.setConnectListener(this);
        communication.setDisconnectListener(this);
        communication.setDataListener(this);
    }

    @Override
    public void connect(@NotNull Socket socket) {
        try {
            players.put(socket, new Player(socket, server, connection));
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
