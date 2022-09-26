package com.ll.exam.sb_batch_exam;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // Spring Data JPA에서 시간 컬럼들(ex.createdAt)에 자동으로 값을 넣어주는 어노테이션
@SpringBootApplication
@EnableBatchProcessing
public class SbBatchExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(SbBatchExamApplication.class, args);
	}

}
