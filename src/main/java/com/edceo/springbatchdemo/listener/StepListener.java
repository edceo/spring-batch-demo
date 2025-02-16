package com.edceo.springbatchdemo.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

@Slf4j
public class StepListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("StepListener.beforeStep {} time: {}", stepExecution.getStepName(), stepExecution.getStartTime());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("StepListener.afterStep {} time: {}", stepExecution.getStepName(), stepExecution.getEndTime());
        return stepExecution.getExitStatus();
    }
}
