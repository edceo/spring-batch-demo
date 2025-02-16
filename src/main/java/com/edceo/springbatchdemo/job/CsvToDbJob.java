package com.edceo.springbatchdemo.job;

import com.edceo.springbatchdemo.dto.MovieDTO;
import com.edceo.springbatchdemo.listener.StepListener;
import com.edceo.springbatchdemo.model.Movie;
import com.edceo.springbatchdemo.processor.MovieItemProcessor;
import com.edceo.springbatchdemo.writer.MovieJDBCWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class CsvToDbJob {

    public static final int CHUNK_SIZE = 100;

    @Value("${app.input.file}")
    private FileSystemResource fileSystemResource;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    @StepScope
    public FlatFileItemReader<MovieDTO> movieCsvReader() {
        return new FlatFileItemReaderBuilder<MovieDTO>()
                .name("movieItemReader")
                .linesToSkip(1) // skip header
                .resource(fileSystemResource)
                .delimited()
                .names("id", "name", "date", "tagline", "description", "minute", "rating")
                .targetType(MovieDTO.class)
                .build();
    }

    @Bean
    public ItemProcessor<MovieDTO, Movie> movieItemProcessor() {
        return new MovieItemProcessor();
    }

    @Bean
    public ItemWriter<Movie> movieDbWriter() {
        return new MovieJDBCWriter(jdbcTemplate);
    }

    @Bean
    public Step importMoviesStep() {
        return new StepBuilder("read-csv-and-write-to-db", jobRepository)
                .<MovieDTO, Movie>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(movieCsvReader())
                .processor(movieItemProcessor())
                .writer(movieDbWriter())
                .listener(new StepListener())
//                .listener(new ReadListener()) // example for annotation based listener
                .faultTolerant()
                .build();
    }

    @Bean
    public Job importMoviesJob() {
        return new JobBuilder("importMoviesJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(importMoviesStep())
                .build();
    }
}
