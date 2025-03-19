package com.cineverso.api_cineverso.models.Movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "movie_id")
    private UUID movieId;

    @Column(unique = true)
    private String title;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String director;

    @ElementCollection
    @Column(name = "cast_member")
    private List<String> cast;

    @Column(name = "average_rating")
    @Builder.Default
    private Double averageRating = 0.0;

    @Enumerated(EnumType.STRING)
    private MovieType movieType;

   @Enumerated(EnumType.STRING)
    private  MovieGenres movieGenres;

   private String studio;

   @Builder.Default
   private String picture = "https://pic.onlinewebfonts.com/thumbnails/icons_52049.svg";
}
