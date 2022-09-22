package com.ll.exam.sb_batch_exam;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SbBatchExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbBatchExamApplication.class, args);
	}

}
