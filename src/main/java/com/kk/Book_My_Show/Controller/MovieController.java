package com.kk.Book_My_Show.Controller;

import com.kk.Book_My_Show.Exception.MovieNotFound;
import com.kk.Book_My_Show.RequestDto.MovieEntryDto;
import com.kk.Book_My_Show.Services.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    MovieService movieService;

    @PostMapping("/add")
    public String addMovie(@RequestBody MovieEntryDto movieEntryDto){

        return movieService.addMovie(movieEntryDto);
    }

    @GetMapping("/movieNamewithTheMaximumNumberOfShows")
    public String movieNamewithTheMaximumNumberOfShows() {

        return movieService.movieNamewithTheMaximumNumberOfShows();
    }

    @GetMapping("/{totalCollectionByParticularMovie}")
    public int totalCollectionByParticularMovie(@PathVariable("totalCollectionByParticularMovie") Integer movieId) throws MovieNotFound {

        return movieService.totalCollectionByParticularMovie(movieId);
    }

    @GetMapping("/getMovie/{movieId}")
    public String checkMovieStatus(@PathVariable String movieId) throws MovieNotFound {

        int movieId1  = Integer.parseInt(movieId);
        return movieService.checkMovieStatus(movieId1);
    }
}
