// IBookAidlInterface.aidl
package com.example.ipclibrary;

//必须手动引如使用到的参数类的全路径，即使在同一个包下也需要
import com.example.ipclibrary.bean.Book;

// Declare any non-default types here with import statements

interface IBookAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    /**
    *   定义通信接口，需要标注参数流向（输入（in），输出（out），输入输出（inout））
    */
     void addBook(in Book book);


     List<Book> getBookList();
}
