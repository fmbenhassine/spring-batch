/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.batch.sample;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.Printer;
import org.springframework.format.support.FormattingConversionService;

@Configuration
@EnableBatchProcessing
public class MyJob {

	@Bean
	public ItemReader<Person> itemReader() {
		Person foo = new Person("foo", LocalDate.now());
		Person bar = new Person("bar", LocalDate.now());
		return new ListItemReader<>(Arrays.asList(foo, bar));
	}

	@Bean
	public ItemWriter<Person> itemWriter() {
		FormattingConversionService conversionService = new FormattingConversionService();
		conversionService.addPrinter(new Printer<LocalDate>() {
			@Override
			public String print(LocalDate localDate, Locale locale) {
				return DateTimeFormatter.ofPattern("yyyyMMdd", locale).format(localDate);
			}
		});

		return new FlatFileItemWriterBuilder<Person>()
				.name("personsWriter")
				.resource(new FileSystemResource("persons.csv"))
				.delimited()
				.formattingConversionService(conversionService)
				.names("name", "birthDate")
				.build();
	}

	@Bean
	public Job job(JobBuilderFactory jobs, StepBuilderFactory steps) {
		return jobs.get("job")
				.start(steps.get("step")
						.<Person, Person>chunk(5)
						.reader(itemReader())
						.writer(itemWriter())
						.build())
				.build();
	}

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new AnnotationConfigApplicationContext(MyJob.class);
		JobLauncher jobLauncher = context.getBean(JobLauncher.class);
		Job job = context.getBean(Job.class);
		jobLauncher.run(job, new JobParameters());
	}

	static class Person {

		private String name;
		private LocalDate birthDate;

		public Person() {
		}

		public Person(String name, LocalDate birthDate) {
			this.name = name;
			this.birthDate = birthDate;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public LocalDate getBirthDate() {
			return birthDate;
		}

		public void setBirthDate(LocalDate birthDate) {
			this.birthDate = birthDate;
		}

		@Override
		public String toString() {
			return "Person{" +
					"name='" + name + '\'' +
					", birthDate=" + birthDate +
					'}';
		}
	}

}
