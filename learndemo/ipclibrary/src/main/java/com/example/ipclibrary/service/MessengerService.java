package com.example.ipclibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ipclibrary.Config;

public class MessengerService extends Service {

    private Handler target = new MyHandler();
    static class MyHandler extends Handler  {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg != null && msg.arg1 == Config.MSG_ID_CLIENT && msg.getData() != null) {
                String messageData = msg.getData().getString(Config.MSG_CONTENT);
                Log.i("TAG", "get message from client ,"+ messageData);
                //回复消息
                Message replyMsg = Message.obtain();
                replyMsg.arg1 = Config.MSG_ID_SERVER;
                Bundle bundle = new Bundle();
                bundle.putString(Config.MSG_CONTENT, "收到你的消息了");
                replyMsg.setData(bundle);

                try {
                    msg.replyTo.send(replyMsg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * Messenger 实现了对aidl的封装，不需要用户去定义aidl接口
     * 一对多，只支持传输Bundle支持的数据类型，不支持RPC
     */
    private Messenger messenger = new Messenger(target);


    public MessengerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
