package com.kk.Book_My_Show.Services;

import com.kk.Book_My_Show.Exception.MovieNotFound;
import com.kk.Book_My_Show.Models.Movie;
import com.kk.Book_My_Show.Repository.MovieRepository;
import com.kk.Book_My_Show.Repository.ShowRepository;
import com.kk.Book_My_Show.RequestDto.MovieEntryDto;
import com.kk.Book_My_Show.Transformers.MovieTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    ShowRepository showRepository;

    private int HIT_COLLECTION_THRESHOLD = 100; // Example threshold of 100 million


    public String addMovie(MovieEntryDto movieEntryDto){

        Movie movie = MovieTransformer.convertDtoToEntity(movieEntryDto);

        movieRepository.save(movie);

        return "Movie added successfully";
    }

    public String movieNamewithTheMaximumNumberOfShows () {

        List<Movie> movieList = movieRepository.findAll();
        Movie ans = movieList.get(0);
        int max = 0;
        for (Movie movie : movieList) {

            if (movie.getShowList().size() > max) {
                ans = movie;
            }
        }
        return ans.getMovieName();
    }

    public int totalCollectionByParticularMovie (Integer movieId) throws MovieNotFound {

        Optional<Movie> optionalMovie = movieRepository.findById(movieId);

        if(!optionalMovie.isPresent()) {
            throw new MovieNotFound("Movie Not Found...");
        }

        Movie movie = optionalMovie.get();

//        List<Show> showList = movie.getShowList();
//        for(Show show : showList) {
//
//            List<Ticket> ticketList = show.getTicketList();
//            for(Ticket ticket : ticketList) {
//                totalCollection += ticket.getTotalTicketsPrice();
//            }
//        }

        int totalCollection = movie.getBoxOfficeCollection();

        return totalCollection;
    }

    public String checkMovieStatus(Integer movidId) throws MovieNotFound {

        Optional<Movie> optionalMovie = movieRepository.findById(movidId);

        if(!optionalMovie.isPresent()) {

            throw new MovieNotFound("Movie not found !!!");
        }

        Movie movie = optionalMovie.get();

        if(movie.getBoxOfficeCollection() >= HIT_COLLECTION_THRESHOLD) {

            return "Hit and the boxOfficeCollection is : " + movie.getBoxOfficeCollection();
        }

        else {
            return "Flop and the boxOfficeCollection is : " + movie.getBoxOfficeCollection() ;
        }
    }
}
