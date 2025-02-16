package com.edceo.springbatchdemo.processor;

import com.edceo.springbatchdemo.dto.MovieDTO;
import com.edceo.springbatchdemo.model.Movie;
import org.springframework.batch.item.ItemProcessor;

public class MovieItemProcessor implements ItemProcessor<MovieDTO, Movie> {
    @Override
    public Movie process(MovieDTO item) {
        return mapToMovie(item);
    }

    private Movie mapToMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setId(movieDTO.getId());
        movie.setName(movieDTO.getName());
        movie.setDate(movieDTO.getDate());
        movie.setTagline(movieDTO.getTagline());
        movie.setDescription(movieDTO.getDescription());
        movie.setDuration(movieDTO.getMinute());
        movie.setRating(movieDTO.getRating());
        return movie;
    }
}
