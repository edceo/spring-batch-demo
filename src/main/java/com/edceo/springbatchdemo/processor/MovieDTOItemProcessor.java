package com.edceo.springbatchdemo.processor;

import com.edceo.springbatchdemo.dto.MovieDTO;
import com.edceo.springbatchdemo.model.Movie;
import org.springframework.batch.item.ItemProcessor;

public class MovieDTOItemProcessor implements ItemProcessor<Movie, MovieDTO> {
    @Override
    public MovieDTO process(Movie item) {
        return mapToMovie(item);
    }

    private MovieDTO mapToMovie(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(movie.getId());
        movieDTO.setName(movie.getName());
        movieDTO.setDate(movie.getDate());
        movieDTO.setTagline(movie.getTagline());
        movieDTO.setDescription(movie.getDescription());
        movieDTO.setMinute(movie.getDuration());
        movieDTO.setRating(movie.getRating());
        return movieDTO;
    }
}
