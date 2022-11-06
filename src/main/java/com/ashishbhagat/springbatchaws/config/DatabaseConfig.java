package com.ashishbhagat.springbatchaws.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.username}")
    private String mysqlUsername;
    @Value("${spring.datasource.password}")
    private String mysqlPassword;
    @Value("${spring.datasource.url}")
    private String mysqlUrl;

    @Value("${postgres.datasource.username}")
    private String postgresUsername;
    @Value("${postgres.datasource.password}")
    private String postgresPassword;
    @Value("${postgres.datasource.url}")
    private String postgresUrl;


    @Bean("mysqldb")
    @Primary
    public DataSource mysqlDataSource() {
        return DataSourceBuilder
                .create()
                .username(mysqlUsername)
                .password(mysqlPassword)
                .url(mysqlUrl)
                .build();
    }

    @Bean("mysqlJdbcTemplate")
    public JdbcTemplate getMysqlJdbcTemplate(@Qualifier("mysqldb") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean("mysqlNamedJdbcTemplate")
    public NamedParameterJdbcTemplate getNamedMysqlJdbcTemplate(@Qualifier("mysqldb") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean("postgresdb")
    public DataSource postgresDataSource() {
        return DataSourceBuilder
                .create()
                .username(postgresUsername)
                .password(postgresPassword)
                .url(postgresUrl)
                .build();
    }

    @Bean("postgresJdbcTemplate")
    public JdbcTemplate getPostgresJdbcTemplate(@Qualifier("postgresdb") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }



}
