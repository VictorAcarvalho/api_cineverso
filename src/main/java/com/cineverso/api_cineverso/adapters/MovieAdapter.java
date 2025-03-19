package com.cineverso.api_cineverso.adapters;

import com.cineverso.api_cineverso.models.Movie.CreateMovieRequest;
import com.cineverso.api_cineverso.models.Movie.Movie;
import com.cineverso.api_cineverso.models.Movie.MovieGenres;
import com.cineverso.api_cineverso.models.Movie.MovieType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MovieAdapter {

    public Movie handleMovieRequest(CreateMovieRequest createMovieRequest){
        return Movie.builder()
                .title(createMovieRequest.title())
                .movieGenres(movieGenreFromString(createMovieRequest.movieGenre()))
                .movieType(movieTypeFromString(createMovieRequest.movieType()))
                .description(createMovieRequest.description())
                .studio(createMovieRequest.studio())
                .director(createMovieRequest.director())
                .createdAt(LocalDateTime.now())
                .cast(createMovieRequest.cast())
                .build();
    }

    private  MovieGenres movieGenreFromString(String genre){
        try {
            return MovieGenres.valueOf(genre);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid movie genre: " + genre );
        }
    }

    private MovieType movieTypeFromString(String type){
        try {
            return MovieType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid movie type: " + type );
        }
    }

}
