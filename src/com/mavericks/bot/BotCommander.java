package com.mavericks.bot;

import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class BotCommander extends Thread {
    private int mMessage;
    private BotReply mReply;
    private String mAddress;

    public interface BotReply {
        public void receiveReply(int c);
    }

    public BotCommander(String address, int message, BotReply reply) {
        super();

        mAddress = address;
        mMessage = message;
        mReply = reply;
    }

    public void setCommand(int command) {
        mMessage = command;
    }

    @Override
    public void run() {
        try {
            final Socket writeSocket = BotConnection.getInstance(mAddress).getWriteSocket();
            final OutputStream out = writeSocket.getOutputStream(); //new DataOutputStream(socket.getOutputStream());
            out.write(mMessage);
            out.flush();
            Log.i("SEND", new String(new char[] { (char) mMessage }));
            //final PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            //final char m = (char) mMessage;
            //out.print(new char[] {m, m, m, m ,m});
            //final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //final Socket readSocket = BotConnection.getInstance(mAddress).getReadSocket();
            //final InputStream in = readSocket.getInputStream(); //new DataInputStream(socket.getInputStream());
            //final int c = in.read();
            //c = dataInputStream.read();
            //Log.i("RCV", new String(new char[] { (char) c }));
            //if (mReply != null) mReply.receiveReply(c);
        } catch (Exception e) {
            BotConnection.getInstance(mAddress).renewSocket();
            e.printStackTrace();
        }
    }
}
