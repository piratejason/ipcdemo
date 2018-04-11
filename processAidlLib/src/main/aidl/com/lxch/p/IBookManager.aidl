// IBookManager.aidl
package com.lxch.p;

// Declare any non-default types here with import statements
import com.lxch.p.Book;

interface IBookManager {

 int add(int a);

 List<Book> getBookList();
 void addBook(in Book book);
}
