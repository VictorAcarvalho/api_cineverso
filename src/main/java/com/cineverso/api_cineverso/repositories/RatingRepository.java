package com.cineverso.api_cineverso.repositories;

import com.cineverso.api_cineverso.models.Movie.Movie;
import com.cineverso.api_cineverso.models.Movie.Rating;
import com.cineverso.api_cineverso.models.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
    Optional<Rating> findByUserIdAndMovieId(User userId, Movie movieId);
    Optional<List<Rating>> findByMovieId(Movie movieId);
}
