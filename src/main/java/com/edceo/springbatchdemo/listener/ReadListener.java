package com.edceo.springbatchdemo.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeRead;

@Slf4j
public class ReadListener {

    @BeforeRead
    public void beforeRead() {
        log.info("ReadListener.beforeRead");
    }

    @AfterRead
    public void afterRead(Object item) {
        log.info("ReadListener.afterRead: {}", item);
    }
}
