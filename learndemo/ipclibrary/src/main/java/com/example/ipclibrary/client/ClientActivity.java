package com.example.ipclibrary.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.example.ipclibrary.Config;
import com.example.ipclibrary.IBookAidlInterface;
import com.example.ipclibrary.R;
import com.example.ipclibrary.bean.Book;
import com.example.ipclibrary.service.RemoteService;

import java.util.List;

public class ClientActivity extends AppCompatActivity {

    private IBookAidlInterface iBookAidlInterface;
    private boolean connected;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接服务时，得IBookAidlInterface的实例
            iBookAidlInterface = IBookAidlInterface.Stub.asInterface(service);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        //绑定service
        bindService();
        bindMessengerService();
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    iBookAidlInterface.addBook(new Book("book4", 4));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<Book> books = iBookAidlInterface.getBookList();
                    for (Book book : books) {
                        Log.i("TAG", "get book list: " + book);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsgByMessenger("我是客户端");
            }
        });
    }

    private void bindService(){
        //这里使用action来启动远程service，方便把service移动到新的工程,
        Log.i("TAG", getPackageName());
        Intent intent = new Intent();
        intent.setAction("com.example.ipclibrary.service");
        intent.setPackage("com.example.learndemo");//service所在应用的包名, 而不是模块名
        this.getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);

//        Intent intent = new Intent(this, RemoteService.class);
//        this.getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }



    /**----------------------------------------Messenger-----------------------------------------------*/
    private Messenger mServiceMessenger;
    Handler target = new MyHandler();
    Messenger mClientMessenger  = new Messenger(target);
    static class MyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg != null && msg.getData() != null && msg.arg1 == Config.MSG_ID_SERVER){
                String serverMsg = msg.getData().getString(Config.MSG_CONTENT);
                Log.i("TAG", "服务端回消息："+ serverMsg);
            }
        }
    }

    ServiceConnection mMessengerServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mServiceMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceMessenger = null;
        }
    };

    private void bindMessengerService() {
        Intent intent = new Intent("com.example.ipclibrary.messengerservice");
        intent.setPackage("com.example.learndemo");
        bindService(intent, mMessengerServiceConn, Context.BIND_AUTO_CREATE);
    }

    private void sendMsgByMessenger(String msg) {
        Message message = Message.obtain();
        message.arg1 = Config.MSG_ID_CLIENT;
        Bundle bundle = new Bundle();
        bundle.putString(Config.MSG_CONTENT, msg);
        message.setData(bundle);
        message.replyTo = mClientMessenger;//指定客户端对象，一个服务端可以有多个不同客户端对象

        //把消息发送给服务端
        try {
            mServiceMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
