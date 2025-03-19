package com.cineverso.api_cineverso.controllers;

import com.cineverso.api_cineverso.models.Movie.CreateMovieRequest;
import com.cineverso.api_cineverso.models.Movie.Movie;
import com.cineverso.api_cineverso.models.Movie.MovieGenres;
import com.cineverso.api_cineverso.services.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> saveMovie(@RequestBody CreateMovieRequest createMovieRequest){
        log.info("[] - start saving movie: {}", createMovieRequest.title() );

        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.saveMovie(createMovieRequest));
    }

    @PutMapping({"/{movieId}"})
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Movie> updateMovieById(@RequestBody CreateMovieRequest createMovieRequest , @PathVariable("movieId") String moveId){
        log.info("[{] - start updating movie: {}", createMovieRequest.title());
        return  ResponseEntity.ok(movieService.updateMovie(moveId,createMovieRequest));
    }

    @GetMapping({"/{movieId}"})
    public ResponseEntity<Movie> getMovie(@PathVariable("movieId") String moveId){

        return ResponseEntity.ok(movieService.getMovieById(moveId));
    }


    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String director,
            @RequestParam(required = false) MovieGenres genre,
            @RequestParam(required = false) String actor
    ) {
        return ResponseEntity.ok(movieService.findWithFilters(title, director, genre, actor));
    }

    @GetMapping()
    public  ResponseEntity<List<Movie>> getAllMovies(){
        return ResponseEntity.ok(movieService.findAllMovies());
    }

}
