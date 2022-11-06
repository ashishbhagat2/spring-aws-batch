package com.ashishbhagat.springbatchaws;

import com.ashishbhagat.springbatchaws.Entity.Book;
import com.ashishbhagat.springbatchaws.Entity.Phone;
import com.ashishbhagat.springbatchaws.Repository.BookDao;
import com.ashishbhagat.springbatchaws.Repository.PhoneDao;
import com.ashishbhagat.springbatchaws.service.StorageService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.batch.operations.JobRestartException;
import java.util.List;

@SpringBootApplication
@RestController
public class SpringbatchAwsApplication {

	@Autowired
	StorageService storageService;

	@Autowired
	BookDao bookDao;

	@Autowired
	PhoneDao phoneDao;

	@GetMapping("/download/{fileName}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName) {
		byte[] data = storageService.downloadFile(fileName);
		ByteArrayResource resource = new ByteArrayResource(data);
		return ResponseEntity
				.ok()
				.contentLength(data.length)
				.header("Content-type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
				.body(resource);
	}
	public static void main(String[] args) {
		SpringApplication.run(SpringbatchAwsApplication.class, args);
	}

	@GetMapping("/books")
	public ResponseEntity<List<Book>> getBooks() {
		return new ResponseEntity<>(bookDao.getAllBooks(), HttpStatus.OK);
	}

	@GetMapping("/phones")
	public ResponseEntity<List<Phone>> getPhones() {
		return new ResponseEntity<>(phoneDao.getAllPhones(), HttpStatus.OK);
	}

}
