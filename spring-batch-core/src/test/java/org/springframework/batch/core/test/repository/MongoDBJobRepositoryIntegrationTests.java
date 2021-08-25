/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.core.test.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClientFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.MongoJobExplorerFactoryBean;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MongoJobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Mahmoud Ben Hassine
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class MongoDBJobRepositoryIntegrationTests {

	// TODO find the best way to externalize and manage image versions
	private static final DockerImageName MONGODB_IMAGE = DockerImageName.parse("mongo:5.0.2");

	@ClassRule
	public static MongoDBContainer mongodb = new MongoDBContainer(MONGODB_IMAGE);

	@Autowired
	private JobLauncher jobLauncher;
	@Autowired
	private Job job;
	
	@Before
	public void setUp() {
		// execute script to create meta-data collections
	}

	@Test
	public void testJobExecution() throws Exception {
		// given
		JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
		
		// when
		JobExecution jobExecution = this.jobLauncher.run(this.job, jobParameters);

		// then
		Assert.assertNotNull(jobExecution);
		Assert.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}

	@Configuration
	@EnableBatchProcessing
	static class TestConfiguration {
		private static final String DATABASE_NAME = "test";

		@Bean
		public JobRepository jobRepository(MongoClient mongoClient, MongoTransactionManager transactionManager) throws Exception {
			MongoJobRepositoryFactoryBean jobRepositoryFactoryBean = new MongoJobRepositoryFactoryBean();
			jobRepositoryFactoryBean.setMongoClient(mongoClient);
			jobRepositoryFactoryBean.setDatabaseName(DATABASE_NAME);
			jobRepositoryFactoryBean.setTransactionManager(transactionManager);
			jobRepositoryFactoryBean.afterPropertiesSet();
			return jobRepositoryFactoryBean.getObject();
		}

		@Bean
		public JobExplorer jobExplorer(MongoClient mongoClient, MongoTransactionManager transactionManager) throws Exception {
			MongoJobExplorerFactoryBean jobExplorerFactoryBean = new MongoJobExplorerFactoryBean();
			jobExplorerFactoryBean.setMongoClient(mongoClient);
			jobExplorerFactoryBean.setDatabaseName(DATABASE_NAME);
			jobExplorerFactoryBean.setTransactionManager(transactionManager);
			jobExplorerFactoryBean.afterPropertiesSet();
			return jobExplorerFactoryBean.getObject();
		}

		@Bean
		public MongoClient mongoClient() {
			MongoClientFactory mongoClientFactory = new MongoClientFactory();
			// how to create mongo client?
			return null;
		}

		@Bean
		public MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient) {
			return new SimpleMongoClientDatabaseFactory(mongoClient, DATABASE_NAME);

		}

		@Bean
		public MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
			MongoTransactionManager mongoTransactionManager = new MongoTransactionManager();
			mongoTransactionManager.setDbFactory(mongoDatabaseFactory);
			mongoTransactionManager.afterPropertiesSet();
			return mongoTransactionManager;
		}

		@Bean
		public Job job(JobRepository jobRepository, MongoTransactionManager transactionManager) {
			return new JobBuilder("job", jobRepository)
					.start(new StepBuilder("step", jobRepository)
							.tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED, transactionManager)
							.build())
					.build();
		}

	}
}
