package com.mavericks.bot;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mavericks.bot.BotCommander.BotReply;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity implements TextWatcher {
    private final static int PIVOT_LEFT = 'l';
    private final static int PIVOT_RIGHT = 'r';
    private final static int TURN_LEFT = 'c';
    private final static int TURN_RIGHT = 'a';
    private final static int GO_FORWARD = 'f';
    private final static int GO_BACKWARD = 'b';
    private final static int BACK_RIGHT = 'd';
    private final static int BACK_LEFT = 'e';
    private final static int STOP_MOVING = 's';

    private String mIpAddress = "192.168.1.133";
    private static final String IP_ADDR_PATTERN = 
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    //private BotCommander mBotCommander;
    private final Handler mHandler = new Handler();

    private BotReply mReplyHandler = new BotReply() {
        @Override
        public void receiveReply(final int c) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mStatus.setText(getReply(c));
                }
            });
        }
    };

    private EditText mAddress;
    private TextView mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //StrictMode.setThreadPolicy(
        //        new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        mAddress = (EditText) findViewById(R.id.ip_address);
        mAddress.setText(mIpAddress);
        mAddress.addTextChangedListener(this);
        mStatus = (TextView) findViewById(R.id.status);
    }

    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.pivot_left) {
            sendMessage(mIpAddress, PIVOT_LEFT);
        } else if (id == R.id.pivot_right) {
            sendMessage(mIpAddress, PIVOT_RIGHT);
        } else if (id == R.id.turn_left) {
            sendMessage(mIpAddress, TURN_LEFT);
        } else if (id == R.id.turn_right) {
            sendMessage(mIpAddress, TURN_RIGHT);
        } else if (id == R.id.go_forward) {
            sendMessage(mIpAddress, GO_FORWARD);
        } else if (id == R.id.go_backward) {
            sendMessage(mIpAddress, GO_BACKWARD);
        } else if (id == R.id.stop_moving) {
            sendMessage(mIpAddress, STOP_MOVING);
        } else if (id == R.id.back_left) {
            sendMessage(mIpAddress, BACK_LEFT);
        } else if (id == R.id.back_right) {
            sendMessage(mIpAddress, BACK_RIGHT);
        }
    }

    private void sendMessage(final String address, final int message) {
        /*
        if (mLastMessage != null && !mLastMessage.isCancelled()) {
            mLastMessage.cancel(true);
        }
        mLastMessage = new ArduinoBot(address, message, mReplyHandler);
        mLastMessage.execute();
        */
        new BotCommander(address, message, mReplyHandler).start();
        //new BotCommander(address, message, mReplyHandler);
        //new BotCommander(address, message, mReplyHandler);
        //new BotCommander(address, message, mReplyHandler);
        //new BotCommander(address, message, mReplyHandler);
        //mBotCommander.setCommand(message);
        //if (!mBotCommander.isAlive()) mBotCommander.start();

        mStatus.setText(getStatus(message));
    }

    private String getStatus(int message) {
        switch (message) {
            case PIVOT_LEFT: return getString(R.string.command_sent) + getString(R.string.pivot_left);
            case PIVOT_RIGHT: return getString(R.string.command_sent) + getString(R.string.pivot_right);
            case TURN_LEFT: return getString(R.string.command_sent) + getString(R.string.turn_left);
            case TURN_RIGHT: return getString(R.string.command_sent) + getString(R.string.turn_right);
            case BACK_LEFT: return getString(R.string.command_sent) + getString(R.string.back_left);
            case BACK_RIGHT: return getString(R.string.command_sent) + getString(R.string.back_right);
            case GO_FORWARD: return getString(R.string.command_sent) + getString(R.string.go_forward);
            case GO_BACKWARD: return getString(R.string.command_sent) + getString(R.string.go_backward);
            case STOP_MOVING: return getString(R.string.command_sent) + getString(R.string.stop_moving);
        }
        return "";
    }

    private String getReply(int message) {
        switch (message) {
            case PIVOT_LEFT: return getString(R.string.reply_received) + getString(R.string.pivot_left);
            case PIVOT_RIGHT: return getString(R.string.reply_received) + getString(R.string.pivot_right);
            case TURN_LEFT: return getString(R.string.reply_received) + getString(R.string.turn_left);
            case TURN_RIGHT: return getString(R.string.reply_received) + getString(R.string.turn_right);
            case BACK_LEFT: return getString(R.string.reply_received) + getString(R.string.back_left);
            case BACK_RIGHT: return getString(R.string.reply_received) + getString(R.string.back_right);
            case GO_FORWARD: return getString(R.string.reply_received) + getString(R.string.go_forward);
            case GO_BACKWARD: return getString(R.string.reply_received) + getString(R.string.go_backward);
            case STOP_MOVING: return getString(R.string.reply_received) + getString(R.string.stop_moving);
        }
        return "";
    }

    @Override
    public void afterTextChanged(Editable s) {
        mIpAddress = s.toString();
        if (!validate(mIpAddress)) {
            mAddress.setError(getString(R.string.ip_error));
        } else {
            mAddress.setError(null);
            //mBotCommander.setAddress(mIpAddress);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    
    public static boolean validate(final String ip) {
        final Pattern pattern = Pattern.compile(IP_ADDR_PATTERN);
        final Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
  }
}
