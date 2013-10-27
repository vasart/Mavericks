package com.mavericks.bot;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class BotConnection {
    private final static int PORT = 6789;

    private static BotConnection mInstance;
    private Socket mWriteSocket;
    private Socket mReadSocket;
    private String mAddress;
    private InetAddress dstAddress = null;

    public static BotConnection getInstance(String address) {
        if (mInstance == null) mInstance = new BotConnection();
        if (!address.equals(mInstance.mAddress)) {
            try {
                mInstance.dstAddress = InetAddress.getByName(address);
                mInstance.mAddress = address;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } 
        }
        return mInstance;
    }

    private BotConnection() {}

    public Socket getReadSocket() {
        if (mReadSocket == null || !mReadSocket.isConnected() || mReadSocket.isClosed() || !mReadSocket.isBound() || mReadSocket.isInputShutdown()) {
            mReadSocket = getNewSocket();
        }
        return getNewSocket();
    }

    public Socket getWriteSocket() {
        if (mWriteSocket == null || !mWriteSocket.isConnected() || mWriteSocket.isClosed() || !mWriteSocket.isBound() || mWriteSocket.isOutputShutdown()) {
            mWriteSocket = getNewSocket();
        }
        return mWriteSocket;
    }

    private Socket getNewSocket() {
        try {
            final Socket socket = new Socket(dstAddress, PORT);
            socket.setKeepAlive(true);
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void renewSocket() {
        mWriteSocket = getNewSocket();
    }
}
