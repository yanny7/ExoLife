package com.yanny.server;

import com.sun.istack.internal.NotNull;
import com.yanny.communication.Communication;
import com.yanny.game.Game;
import com.yanny.interfaces.CommunicationInterface;
import com.yanny.interfaces.GameInterface;
import com.yanny.interfaces.PlayersInterface;
import com.yanny.interfaces.ServerInterface;
import com.yanny.players.Players;

public class Server implements ServerInterface {
    @NotNull private final CommunicationInterface communication;
    @NotNull private final PlayersInterface players;
    @NotNull private final GameInterface game;

    public Server() {
        communication = new Communication();
        players = new Players(this);
        game = new Game();
    }

    @Override
    public CommunicationInterface getCommunication() {
        return communication;
    }

    @Override
    public PlayersInterface getPlayers() {
        return players;
    }

    @Override
    public GameInterface getGame() {
        return game;
    }

    @Override
    public boolean start() {
        return communication.start();
    }

    @Override
    public void stop() {
        communication.stop();
    }

    @Override
    public int getVersion() {
        return 0; //TODO get from another place
    }

    @Override
    public int getMinClientVersion() {
        return 1;
    }
}
