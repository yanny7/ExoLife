package com.yanny.communication;

import com.yanny.interfaces.ConnectListener;
import com.yanny.interfaces.DataListener;
import com.yanny.interfaces.DisconnectListener;
import com.yanny.utils.Log;
import com.yanny.utils.Module;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;

class ConnectionHandler implements Runnable {
    @NotNull private final Socket socket;
    @NotNull private final DataListener dataListener;
    @NotNull private final ConnectListener connectListener;
    @NotNull private final DisconnectListener disconnectListener;
    @NotNull private final Map<Socket, ConnectionHandler> sockets;
    @NotNull private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

    @Nullable private DataInputStream inputStream;
    @Nullable private OutputStream outputStream;

    ConnectionHandler(@NotNull Socket socket, @NotNull DataListener dataListener, @NotNull ConnectListener connectListener,
                      @NotNull DisconnectListener disconnectListener, @NotNull Map<Socket, ConnectionHandler> sockets) {
        this.socket = socket;
        this.dataListener = dataListener;
        this.connectListener = connectListener;
        this.disconnectListener = disconnectListener;
        this.sockets = sockets;

        try {
            outputStream = socket.getOutputStream();
            inputStream = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            //e.printStackTrace();
            Log.log(Module.COMM).info( "Failed to create DataInputStream: " + e);
        }
    }

    @Override
    public void run() {
        if (inputStream == null) {
            Log.log(Module.COMM).info( "Client closed: InputStream is NULL");
            closeResources();
            return;
        }

        connectListener.connect(socket);

        while(true) {
            try {
                short size = inputStream.readShort();
                byte[] data = new byte[size];
                int remaining = size;
                int transmitted;

                bos.reset();

                do {
                    transmitted = inputStream.read(data, 0, remaining);
                    bos.write(data, 0, transmitted);
                    remaining -= transmitted;
                } while (remaining > 0);

                data = bos.toByteArray();
                Log.log(Module.COMM).info( "Packet received: " + Arrays.toString(data));

                synchronized (dataListener) {
                    dataListener.receive(data, socket);
                }
            } catch (Exception e) {
                Log.log(Module.COMM).info( "Client closed: Data receive failed: " + e);
                //e.printStackTrace();
                break;
            }
        }

        disconnectListener.disconnect(socket);

        Log.log(Module.COMM).info( "Client closed: " + socket);
        closeResources();
    }

    boolean sendData(byte[] data) {
        if (outputStream == null) {
            Log.log(Module.COMM).info( "Failed to send data: OutputStream is NULL");
            return false;
        }

        try {
            outputStream.write(data);
        } catch (IOException e) {
            //e.printStackTrace();
            Log.log(Module.COMM).info( "Failed to send data: " + e);
            return false;
        }
        return true;
    }

    private void closeResources() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            //e.printStackTrace();
            Log.log(Module.COMM).info( "OutputStream close failed");
        }
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            //e.printStackTrace();
            Log.log(Module.COMM).info( "InputStream close failed");
        }
        try {
            bos.close();
        } catch (IOException e) {
            //e.printStackTrace();
            Log.log(Module.COMM).info( "ByteArrayOutputStream close failed");
        }

        try {
            socket.close();
        } catch (IOException e) {
            //e.printStackTrace();
            Log.log(Module.COMM).info( "Socket close failed");
        }

        sockets.remove(socket);
    }
}
