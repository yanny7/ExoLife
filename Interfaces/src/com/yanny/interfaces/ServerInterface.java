package com.yanny.interfaces;

public interface ServerInterface {
    CommunicationInterface getCommunication();
    PlayersInterface getPlayers();
    GameInterface getGame();

    boolean start();
    void stop();

    int getVersion();
    int getMinClientVersion();
}
