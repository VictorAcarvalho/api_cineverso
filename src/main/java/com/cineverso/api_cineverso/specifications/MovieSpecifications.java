package com.cineverso.api_cineverso.specifications;

import com.cineverso.api_cineverso.models.Movie.Movie;
import com.cineverso.api_cineverso.models.Movie.MovieGenres;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MovieSpecifications {

    public static Specification<Movie> withFilters(String title, String director, MovieGenres genre, String actor) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                ));
            }

            if (director != null && !director.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("director")),
                        "%" + director.toLowerCase() + "%"
                ));
            }

            if (genre != null) {
                predicates.add(criteriaBuilder.equal(root.get("movieGenres"), genre));
            }

            if (actor != null && !actor.isEmpty()) {
                Join<Movie, String> castJoin = root.join("cast");
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(castJoin),
                        "%" + actor.toLowerCase() + "%"
                ));

                query.distinct(true);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}