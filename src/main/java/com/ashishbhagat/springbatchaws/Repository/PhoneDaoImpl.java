package com.ashishbhagat.springbatchaws.Repository;

import com.ashishbhagat.springbatchaws.Entity.Phone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhoneDaoImpl implements PhoneDao {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    private String query = "select * from phone";
    @Override
    public List<Phone> getAllPhones() {
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Phone.class));
    }
}
