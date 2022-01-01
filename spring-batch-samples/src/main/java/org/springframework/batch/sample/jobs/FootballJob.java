package org.springframework.batch.sample.jobs;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

/**
 * This class is the equivalent Java configuration of `src/main/resources/jobs/footballJob.xml`.
 */
@Configuration
@EnableBatchProcessing
public class FootballJob {

    @Bean
    public Job job(JobBuilderFactory jobs, StepBuilderFactory steps) {
        return jobs.get("job")
                .start(steps.get("step")
                        .tasklet((contribution, chunkContext) -> {
                            System.out.println("hello world");
                            return RepeatStatus.FINISHED;
                        })
                        .build())
                .build();
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("/org/springframework/batch/core/schema-hsqldb.sql")
                .addScript("/business-schema-hsqldb.sql")
                .generateUniqueName(true)
                .build();
    }

}