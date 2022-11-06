package com.ashishbhagat.springbatchaws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.ashishbhagat.springbatchaws.SpringbatchAwsApplication;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@Slf4j
public class StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;


    Logger logger = LoggerFactory.getLogger(SpringbatchAwsApplication.class);

    public byte[] downloadFile(String fileName) {
        S3Object s3Object = s3Client.getObject(bucketName, fileName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()));
        try {
            // Get all the csv headers
            String line = reader.readLine();
            String[] headers = line.split(",");

            // Get number of columns and print headers
            int length = headers.length;
            for (String header : headers) {
                System.out.print(header + "   ");
            }

            while((line = reader.readLine()) != null) {
                System.out.println();

                // get and print the next line (row)
                String[] row = line.split(",");
                for (String value : row) {
                    System.out.print(value + "   ");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

/*
    public String uploadFile(MultipartFile file) {
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return "File uploaded : " + fileName;
    }
*/

}
