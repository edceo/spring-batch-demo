package com.edceo.springbatchdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
@Slf4j
public class BatchController {

    private final JobLauncher jobLauncher;
    private final @Qualifier("importMoviesJob") Job importMoviesJob;
    private final @Qualifier("exportMoviesJob") Job exportMoviesJob;

    @PostMapping("/import-movies")
    public void csvToDb() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startingAt:", System.currentTimeMillis())
                .addString("inputFilePath", "files/movies.csv")
                .toJobParameters();
        try {
            jobLauncher.run(importMoviesJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            log.error("Error running job", e);
        }
    }

    @PostMapping("/export-movies")
    public void dbToCsv() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startingAt:", System.currentTimeMillis())
                .addString("outputFilePath", "files/output-movies.csv")
                .toJobParameters();
        try {
            jobLauncher.run(exportMoviesJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            log.error("Error running job", e);
        }
    }
}
