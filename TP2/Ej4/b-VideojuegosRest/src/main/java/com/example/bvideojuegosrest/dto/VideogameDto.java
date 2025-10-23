package com.example.bvideojuegosrest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideogameDto {
    private Long id;
    private String title;
    private String description;
    private String img;
    private float price;
    private short stock;
    private Date releaseDate;
    private String developerName;
    private String genreName;


}
