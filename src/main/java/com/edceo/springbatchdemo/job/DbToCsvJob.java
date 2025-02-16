package com.edceo.springbatchdemo.job;

import com.edceo.springbatchdemo.dto.MovieDTO;
import com.edceo.springbatchdemo.listener.StepListener;
import com.edceo.springbatchdemo.model.Movie;
import com.edceo.springbatchdemo.processor.MovieDTOItemProcessor;
import com.edceo.springbatchdemo.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class DbToCsvJob {

    public static final int PAGE_SIZE = 100;
    public static final int MAX_ITEM_COUNT = 200;

    @Value("${app.output.file}")
    private FileSystemResource fileSystemResource;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final MovieRepository movieRepository;

    @Bean
    @StepScope
    public FlatFileItemWriter<MovieDTO> movieCsvWriter() {
        return new FlatFileItemWriterBuilder<MovieDTO>()
                .name("movieItemWriter")
                .resource(fileSystemResource)
                .delimited()
                .delimiter(",")
                .names("id", "name", "date", "tagline", "description", "minute", "rating")
                .build();
    }

    @Bean
    public ItemProcessor<Movie, MovieDTO> movieDtoItemProcessor() {
        return new MovieDTOItemProcessor();
    }

    @Bean
    public RepositoryItemReader<Movie> movieDbReader() {
        return new RepositoryItemReaderBuilder<Movie>()
                .name("movieItemReader")
                .repository(movieRepository)
                .methodName("findAll")
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .pageSize(PAGE_SIZE)
                .maxItemCount(MAX_ITEM_COUNT)
                .saveState(false)
                .build();
    }

    @Bean
    public Step exportMoviesStep() {
        return new StepBuilder("read-db-and-write-csv", jobRepository)
                .<Movie, MovieDTO>chunk(PAGE_SIZE, platformTransactionManager)
                .reader(movieDbReader())
                .processor(movieDtoItemProcessor())
                .writer(movieCsvWriter())
                .listener(new StepListener())
                .faultTolerant()
                .build();
    }

    @Bean
    public Job exportMoviesJob() {
        return new JobBuilder("exportMoviesJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(exportMoviesStep())
                .build();
    }
}
