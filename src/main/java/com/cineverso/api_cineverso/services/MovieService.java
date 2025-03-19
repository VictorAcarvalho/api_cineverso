package com.cineverso.api_cineverso.services;

import com.cineverso.api_cineverso.adapters.MovieAdapter;
import com.cineverso.api_cineverso.exceptions.BadRequestException;
import com.cineverso.api_cineverso.exceptions.DefaultException;
import com.cineverso.api_cineverso.models.Movie.CreateMovieRequest;
import com.cineverso.api_cineverso.models.Movie.Movie;
import com.cineverso.api_cineverso.models.Movie.MovieGenres;
import com.cineverso.api_cineverso.models.StepsLabels;
import com.cineverso.api_cineverso.repositories.MovieRepository;
import com.cineverso.api_cineverso.repositories.RatingRepository;
import com.cineverso.api_cineverso.specifications.MovieSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MovieService {

    private  final MovieRepository movieRepository;

    private final RatingRepository ratingRepository;

    private final MovieAdapter movieAdapter;

    public MovieService(MovieRepository movieRepository, RatingRepository ratingRepository, MovieAdapter movieAdapter) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.movieAdapter = movieAdapter;
    }

    public String saveMovie(CreateMovieRequest createMovieRequest){
        var movieExists = movieRepository.existsByTitle(createMovieRequest.title());
        if(movieExists){
            throw new BadRequestException("movie already been registred");
        }

        var handledMovie = movieAdapter.handleMovieRequest(createMovieRequest);

        try {
            movieRepository.save(handledMovie);
        } catch (Exception e) {
            log.error("Error trying create a movie: {}", e.getCause());
            throw new DefaultException("Something was wrong, please try again later");
        }
        return "Movie been saved";
    }

    public Movie updateMovie(String movieId, CreateMovieRequest createMovieRequest){

        var foundedMovie = getMovieById(movieId);
        BeanUtils.copyProperties(createMovieRequest, foundedMovie);
        foundedMovie.setUpdatedAt(LocalDateTime.now());

        try {
            movieRepository.save(foundedMovie);
        } catch (Exception e) {
            log.error("[{}] A error ocurred on updating movie: {}", StepsLabels.UPDATE_MOVIE ,e.getMessage());
            throw new RuntimeException("Something was wrong");
        }

        return foundedMovie;
    }

    public Movie getMovieById(String movieId){
        log.info(" get a movie by id: {}", movieId);
        return movieRepository.findById(UUID.fromString(movieId))
                .orElseThrow(()->new EntityNotFoundException("movie not found"));
    }

    public List<Movie> findWithFilters(String title, String director, MovieGenres genre, String actor) {
        return movieRepository.findAll(MovieSpecifications.withFilters(title, director, genre, actor));
    }

    public  List<Movie> findAllMovies(){
        return movieRepository.findAll();
    }
}
