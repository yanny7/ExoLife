package com.yanny.communication;

import com.yanny.interfaces.CommunicationInterface;
import com.yanny.interfaces.ConnectListener;
import com.yanny.interfaces.DataListener;
import com.yanny.interfaces.DisconnectListener;
import com.yanny.utils.Log;
import com.yanny.utils.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Communication implements CommunicationInterface {
    private static final int PORT = 34543;

    @Nullable private ServerSocket serverSocket;
    @Nullable private DataListener dataListener;
    @Nullable private ConnectListener connectListener;
    @Nullable private DisconnectListener disconnectListener;
    @NotNull private final Map<Socket, ConnectionHandler> sockets;

    public Communication() {
        sockets = new HashMap<>();
    }

    @Override
    public int getConnections() {
        return sockets.size();
    }

    public boolean start() {
        try {
            serverSocket = new ServerSocket(PORT);
            RouterThread routerThread = new RouterThread();
            new Thread(routerThread).start();
            return true;
        } catch (IOException e) {
            //e.printStackTrace();
            Log.log(Module.COMM).severe( "Main loop failed to start: " + e);
            return false;
        }
    }

    public void stop() {
        if (serverSocket != null) {
            try {
                for (Socket socket : sockets.keySet()) {
                    socket.close();
                }
                serverSocket.close();
            } catch (IOException e) {
                //e.printStackTrace();
                Log.log(Module.COMM).info( "Socket close failed");
            }
        }
    }

    @Override
    public void setConnectListener(@NotNull ConnectListener listener) {
        connectListener = listener;
    }

    @Override
    public void setDisconnectListener(@NotNull DisconnectListener listener) {
        disconnectListener = listener;
    }

    @Override
    public void setDataListener(@NotNull DataListener listener) {
        dataListener = listener;
    }

    public boolean sendData(@NotNull byte[] data, @NotNull Socket socket) {
        return sockets.get(socket).sendData(data);
    }

    class RouterThread implements Runnable {
        @Override
        public void run() {
            if (serverSocket == null) {
                Log.log(Module.COMM).severe( "ServerSocket is NULL");
                return;
            }

            Log.log(Module.COMM).info( "Main loop started");

            while (true) {
                Socket clientSocket;

                try {
                    clientSocket = serverSocket.accept();
                    Log.log(Module.COMM).info( "New client accepted: " + clientSocket);
                } catch (IOException e) {
                    Log.log(Module.COMM).info( "Main loop finished with status: " + e);
                    return;
                }

                if ((dataListener != null) && (connectListener != null) && (disconnectListener != null)) {
                    ConnectionHandler connectionHandler = new ConnectionHandler(clientSocket, dataListener, connectListener, disconnectListener, sockets);
                    new Thread(connectionHandler).start();
                    sockets.put(clientSocket, connectionHandler);
                } else {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        //e.printStackTrace();
                        Log.log(Module.COMM).info( "Client close failed: " + e);
                    }
                    Log.log(Module.COMM).severe( "Client rejected: DataListener is NULL: " + clientSocket);
                }
            }
        }
    }
}
