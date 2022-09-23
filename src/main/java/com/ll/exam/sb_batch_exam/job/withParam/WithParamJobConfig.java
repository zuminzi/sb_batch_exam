package com.ll.exam.app_2022_09_22.job.withParam;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WithParamJobConfig {
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job withParamJob() {
        return jobBuilderFactory.get("withParamJob")
                .start(withParamStep1())
                .build();
    }

    private Step withParamStep1() {
        return stepBuilderFactory.get("withParamStep1")
                .tasklet(withParamStep1Tasklet())
                .build();
    }

    private Tasklet withParamStep1Tasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("WithParam 테스클릿 1");

            return RepeatStatus.FINISHED;
        };
    }
}
