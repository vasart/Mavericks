package com.mavericks.bot;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ArduinoBot extends AsyncTask<Void, Void, Integer> {
    private final static int PORT = 6789;
    private String mAddress;
    private int mMessage;
    private BotReply mReply;

    public interface BotReply {
        public void receiveReply(int c);
    }

    public ArduinoBot(String address, int message, BotReply reply) {
        super();

        mAddress = address;
        mMessage = message;
        mReply = reply;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        int c = -1;
        while (c != mMessage) {
            try {
                final InetAddress dstAddress = InetAddress.getByName(mAddress);
                final Socket socket = new Socket(dstAddress, PORT);

                final OutputStream out = socket.getOutputStream(); //new DataOutputStream(socket.getOutputStream());
                final InputStream in = socket.getInputStream(); //new DataInputStream(socket.getInputStream());
                out.write(mMessage);
                out.flush();
                //final PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                //final char m = (char) mMessage;
                //out.print(new char[] {m, m, m, m ,m});
                //out.flush();
                socket.shutdownOutput();
                Log.i("SND", new String(new char[] { (char) mMessage }));
                //final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                c = in.read();
                //c = dataInputStream.read();
                Log.i("RCV", new String(new char[] { (char) c }));
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isCancelled()) break;
        }
        return c;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if (mReply != null) mReply.receiveReply(result.intValue());
    }
}
