package com.cineverso.api_cineverso.services;

import com.cineverso.api_cineverso.adapters.MovieAdapter;
import com.cineverso.api_cineverso.exceptions.BadRequestException;
import com.cineverso.api_cineverso.exceptions.DefaultException;
import com.cineverso.api_cineverso.models.Movie.*;
import com.cineverso.api_cineverso.models.StepsLabels;
import com.cineverso.api_cineverso.repositories.MovieRepository;
import com.cineverso.api_cineverso.specifications.MovieSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class MovieService {

    private  final MovieRepository movieRepository;

    private final RatingService ratingService;

    private final MovieAdapter movieAdapter;

    private final UserService userService;

    public MovieService(MovieRepository movieRepository, RatingService ratingService, MovieAdapter movieAdapter, UserService userService) {
        this.movieRepository = movieRepository;
        this.ratingService = ratingService;
        this.movieAdapter = movieAdapter;
        this.userService = userService;
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

    private Movie updateMovieAverageRating(Movie movie){
        Optional<List<Rating>> ratings = ratingService.findRatingsByMovieId(movie);

        if (!ratings.isEmpty()) {
            double sum = 0;
            for (Rating r : ratings.get()) {
                sum += r.getScore();
            }
            double average = sum / ratings.get().size();

            log.info("[{}] updating movie average for movie: {}", StepsLabels.UPDATE_MOVIE, movie.getMovieId());
            movie.setAverageRating(average);
            movie.setUpdatedAt(LocalDateTime.now());
            movieRepository.save(movie);
        }
        return movie;
    }

    public Movie addRatingToMove(String movieId, RatingRequest ratingRequest){
        var user = userService.getUserByToken();
        Movie movie = getMovieById(movieId);
        Optional<Rating> existingRating = ratingService.findARatingForMovieByUserID(user, movie);

        if(existingRating.isPresent()){
            Rating rating = existingRating.get();
            rating.setScore(ratingRequest.score());
            ratingService.saveRating(rating);
        } else {
            Rating newRating = Rating.builder()
                    .userId(user)
                    .movieId(movie)
                    .score(ratingRequest.score())
                    .build();
            ratingService.saveRating(newRating);
        }

        var updatedMovie = updateMovieAverageRating(movie);

        return updatedMovie;
    }
}
