package com.ashishbhagat.springbatchaws.Repository;

import com.ashishbhagat.springbatchaws.Entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDaoImpl implements  BookDao{

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    private String query = "select * from book";

    @Override
    public List<Book> getAllBooks() {
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Book.class));
    }
}
