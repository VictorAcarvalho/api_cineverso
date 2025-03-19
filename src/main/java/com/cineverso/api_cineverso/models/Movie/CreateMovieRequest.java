package com.cineverso.api_cineverso.models.Movie;

import java.util.List;

public record CreateMovieRequest(String title,
                                 String description,
                                 String director,
                                 List<String> cast ,
                                 String movieGenre ,
                                 String studio,
                                 String movieType) {
}
