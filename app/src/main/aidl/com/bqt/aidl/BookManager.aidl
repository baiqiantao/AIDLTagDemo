package com.bqt.aidl;
import com.bqt.aidl.Book;

interface BookManager {
    Book addBookIn(in Book book);
    Book addBookOut(out Book book);
    Book addBookInout(inout Book book);
}