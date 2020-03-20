package com.example.ipclibrary.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

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
}
