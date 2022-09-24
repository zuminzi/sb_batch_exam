package com.ll.exam.sb_batch_exam.job.withParam;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WithParamJobConfig {
    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job withParamJob(Step withParamStep1) {
        return jobBuilderFactory.get("withParamJob")
                .start(withParamStep1)
                .build();
    }
    @Bean
    @JobScope
    public Step withParamStep1(Tasklet withParamStep1Tasklet) {
        return stepBuilderFactory.get("withParamStep1")
                .tasklet(withParamStep1Tasklet)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet withParamStep1Tasklet(
            @Value("#{jobParameters['name']}") String name,
            @Value("#{jobParameters['age']}") int age
    ) {

        return (contribution, chunkContext) -> {
            System.out.println("WithParam 테스클릿 1, %s, %d".formatted(name, age));

            return RepeatStatus.FINISHED;
        };
    }
}
