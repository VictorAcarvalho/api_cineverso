package com.cineverso.api_cineverso.repositories;

import com.cineverso.api_cineverso.models.Movie.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
}
