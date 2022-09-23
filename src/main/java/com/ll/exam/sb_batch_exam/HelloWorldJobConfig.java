package com.ll.exam.sb_batch_exam;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HelloWorldJobConfig {
    private final JobBuilderFactory jobBuilderFactory; // job 생성 x 빌더 o

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job helloWorldJob() {
        return jobBuilderFactory.get("helloWorldJob")// Bean으로 찾기 때문에 job 이름은 유니크해야됨
                .incrementer(new RunIdIncrementer()) // 강제로 매번 다른 ID를 실행시에 파라미터로 부여
                .start(helloWorldStep1())
                .next(helloWorldStep2()) // 스텝 추가
                .build();
    }

    @Bean
    @JobScope // 특정 Job이 실행될 때 생성 -> 메모리 낭비 X
    public Step helloWorldStep1() {
        return stepBuilderFactory.get("helloWorldStep1")
                .tasklet(helloWorldStep1Tasklet())
                .build();
    }

    @Bean
    @StepScope // 특정 Step이 실행될 때 생성 -> 메모리 낭비 X
    public Tasklet helloWorldStep1Tasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("헬로월드 테스클릿 1");

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @JobScope
    public Step helloWorldStep2() {
        return stepBuilderFactory.get("helloWorldStep2")
                .tasklet(helloWorldStep2Tasklet())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet helloWorldStep2Tasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("헬로월드 테스클릿 2");

            return RepeatStatus.FINISHED;
        };
    }
}