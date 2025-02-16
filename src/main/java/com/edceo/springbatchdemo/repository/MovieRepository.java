package com.edceo.springbatchdemo.repository;

import com.edceo.springbatchdemo.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
