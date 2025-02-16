package com.edceo.springbatchdemo.writer;

import com.edceo.springbatchdemo.model.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@RequiredArgsConstructor
public class MovieJDBCWriter implements ItemWriter<Movie> {

    private final JdbcTemplate jdbcTemplate;

    private void write(Movie movie) {
        try {
            jdbcTemplate.update("INSERT INTO movies (id, name, date, tagline, description, duration, rating) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    movie.getId(),
                    movie.getName(),
                    movie.getDate(),
                    movie.getTagline(),
                    movie.getDescription(),
                    movie.getDuration(),
                    movie.getRating());
        } catch (Exception e) {
            log.error("Error writing movie: {}", movie, e);
        }
    }

    @Override
    public void write(Chunk<? extends Movie> chunk) {
        for (Movie movie : chunk.getItems()) {
            write(movie);
        }
    }
}
