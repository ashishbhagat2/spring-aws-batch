package com.ashishbhagat.springbatchaws.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

  /*  @Bean
    public AmazonS3 s3Client() {
           return AmazonS3ClientBuilder.standard()
                .withCredentials(DefaultAWSCredentialsProviderChain.getInstance()).build();
    }*/

    @Bean
    public  AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new ProfileCredentialsProvider("default"))
                .withRegion("ap-south-1")
                .build();
    }

}