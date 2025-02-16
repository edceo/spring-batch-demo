package com.edceo.springbatchdemo.writer;

import com.edceo.springbatchdemo.model.Movie;
import com.edceo.springbatchdemo.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class MovieWriter implements ItemWriter<Movie> {

    private final MovieRepository movieRepository;

    @Transactional
    @Override
    public void write(Chunk<? extends Movie> chunk) {
        movieRepository.saveAllAndFlush(chunk.getItems());
    }
}
