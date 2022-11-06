package com.ashishbhagat.springbatchaws.Repository;

import com.ashishbhagat.springbatchaws.Entity.Book;

import java.util.List;

public interface BookDao {

    List<Book> getAllBooks();
}
