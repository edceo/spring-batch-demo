package com.edceo.springbatchdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieDTO {

    private Long id;

    private String name;

    private String date;

    private String tagline;

    private String description;

    private Integer minute;

    private Double rating;
}
