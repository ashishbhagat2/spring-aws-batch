package com.ashishbhagat.springbatchaws.batch;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ashishbhagat.springbatchaws.Entity.Book;
import com.ashishbhagat.springbatchaws.Repository.BookDao;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;


@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("mysqlNamedJdbcTemplate") NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("mysqldb") DataSource dataSource;


    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private BookDao bookDao;

    private static final String QUERY_INSERT_BOOK = "INSERT " +
            "INTO Book(id, name, author) " +
            "VALUES (?, ?, ?)";



    @Bean
    public FlatFileItemReader<Book> reader() {
        S3Object s3Object = s3Client.getObject(bucketName, "books.csv");
        S3ObjectInputStream inputStream = s3Object.getObjectContent();

        FlatFileItemReader<Book> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new InputStreamResource(inputStream));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Book> lineMapper() {
        DefaultLineMapper<Book> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "name", "author");

        BeanWrapperFieldSetMapper<Book> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Book.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }

    @Bean
    public ItemWriter<Book> writer() {
        JdbcBatchItemWriter<Book> databaseItemWriter = new JdbcBatchItemWriter<>();
        databaseItemWriter.setDataSource(dataSource);
        databaseItemWriter.setJdbcTemplate(jdbcTemplate);

        databaseItemWriter.setSql(QUERY_INSERT_BOOK);

        ItemPreparedStatementSetter<Book> valueSetter =
                new BookPreparedStatementSetter();
        databaseItemWriter.setItemPreparedStatementSetter(valueSetter);

        ItemSqlParameterSourceProvider<Book> paramProvider =
                new BeanPropertyItemSqlParameterSourceProvider<>();
        databaseItemWriter.setItemSqlParameterSourceProvider(paramProvider);
        return databaseItemWriter;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("csv-step").<Book, Book>chunk(10)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importBooks")
                .incrementer(new RunIdIncrementer())
                .flow(step1()).end().build();

    }
}
