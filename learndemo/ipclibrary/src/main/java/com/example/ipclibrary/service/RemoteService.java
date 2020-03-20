package com.example.ipclibrary.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.ipclibrary.IBookAidlInterface;
import com.example.ipclibrary.bean.Book;

import java.util.ArrayList;
import java.util.List;

public class RemoteService extends Service {

    private List<Book>  bookList;


    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TAG", "onBind");
        return stub;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        bookList = new ArrayList<>();
        bookList.add(new Book("book1", 1));
        bookList.add(new Book("book2", 2));
        bookList.add(new Book("book3", 3));

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 需要实现stub 抽象类，并在绑定服务时，返回返回客户端
     */
    private IBookAidlInterface.Stub stub = new IBookAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }
    };
}
