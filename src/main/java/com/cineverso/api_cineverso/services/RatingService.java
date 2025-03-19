package com.cineverso.api_cineverso.services;

import com.cineverso.api_cineverso.models.Movie.Movie;
import com.cineverso.api_cineverso.models.Movie.Rating;
import com.cineverso.api_cineverso.models.User.User;
import com.cineverso.api_cineverso.repositories.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating saveRating(Rating rating){
      return ratingRepository.save(rating);
    }

    public Optional<Rating> findARatingForMovieByUserID(User userId, Movie movieId ){
        return ratingRepository.findByUserIdAndMovieId(userId, movieId);
    }

    public Optional<List<Rating>> findRatingsByMovieId(Movie movie){
        return ratingRepository.findByMovieId(movie);
    }
}
