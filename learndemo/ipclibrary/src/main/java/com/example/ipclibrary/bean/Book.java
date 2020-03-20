package com.example.ipclibrary.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * aidl支持传输的数据除了基本类型外，还有String和CharSequence，其它的类必须实现parcelable接口，
 * 支持list和map集合，但是集合中的类元素也必须实现parcelable接口
 */
public class Book implements Parcelable {
    public String name;
    public int id;

    public Book() {
    }

    public Book(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    protected Book(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
