package com.ashishbhagat.springbatchaws.batch;

import com.ashishbhagat.springbatchaws.Entity.Book;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.context.annotation.Configuration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookPreparedStatementSetter implements ItemPreparedStatementSetter<Book> {

    @Override
    public void setValues(Book book, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, book.getId());
        preparedStatement.setString(2, book.getName());
        preparedStatement.setString(3, book.getAuthor());
    }
}
