package com.yuyan.test;

import droid.message.Handler;
import droid.message.Looper;
import droid.message.Message;
import droid.util.Log;
import org.junit.jupiter.api.Test;

public class LooperTest {
    private static final String TAG = "LooperTest";

    @Test
    void createNewMessage() {
        Message message = new Message();
        message.what = 10;
        System.out.println("message.what = " + message.what);
        Log.d(TAG, "message.what = " + message.what);
    }

    @Test
    void looperMessage() {
        Looper.prepareMainLooper();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d(TAG, "handleMessage: msg.what = " + msg.what);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    Message message = new Message();
                    message.what = i + 1000;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Looper.loop();
        try {
            throw new Exception("looperMessage looper stop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
