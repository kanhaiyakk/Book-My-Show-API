package com.kk.Book_My_Show.Services;

import com.kk.Book_My_Show.Enums.SeatType;
import com.kk.Book_My_Show.Exception.MovieNotFound;
import com.kk.Book_My_Show.Exception.ShowNotFound;
import com.kk.Book_My_Show.Exception.TheaterNotFound;
import com.kk.Book_My_Show.Models.*;
import com.kk.Book_My_Show.Repository.MovieRepository;
import com.kk.Book_My_Show.Repository.ShowRepository;
import com.kk.Book_My_Show.Repository.TheaterRepository;
import com.kk.Book_My_Show.RequestDto.AddShowDto;
import com.kk.Book_My_Show.RequestDto.ShowSeatsDto;
import com.kk.Book_My_Show.Transformers.ShowTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private ShowRepository showRepository;

    public String addShow(AddShowDto showDto)throws TheaterNotFound, MovieNotFound {

        Show show = ShowTransformer.convertDtoToEntity(showDto);
        //Set the movie and theater entity
        Optional<Movie> movieOptional = movieRepository.findById(showDto.getMovieId());

        if(!movieOptional.isPresent()){
            throw new MovieNotFound("Movie is not found");
        }
        Optional<Theater> theaterOptional = theaterRepository.findById(showDto.getTheaterId());

        if(!theaterOptional.isPresent()){
            throw new TheaterNotFound("Theater is not found");
        }

        Movie movie = movieOptional.get();
        Theater theater = theaterOptional.get();


        //Setting the foreign
        show.setMovie(movie);
        show.setTheater(theater);

        show = showRepository.save(show);

        movie.getShowList().add(show);
        movieRepository.save(movie);

        theater.getShowList().add(show);

        theaterRepository.save(theater);

        return "Show has been added and showId is "+show.getId();
    }

    public String associateShowSeats(ShowSeatsDto showSeatsDto)throws ShowNotFound {

        Optional<Show> optionalShow = showRepository.findById(showSeatsDto.getShowId());

        //Validation check
        if(!optionalShow.isPresent()){
            throw new ShowNotFound("Show Id incorrect!!");
        }

        //Valid Show Now
        Show show = optionalShow.get();



        //We need to theaterSeats
        Theater theater = show.getTheater();

        List<TheaterSeat> theaterSeatList = theater.getTheaterSeatList();

        //Each seat needs to be added in the ?????? -->

        List<ShowSeat> showSeatList = show.getShowSeatList();

        for(TheaterSeat theaterSeat : theaterSeatList){

            ShowSeat showSeat = new ShowSeat();

            showSeat.setSeatNo(theaterSeat.getSeatNo());
            showSeat.setSeatType(theaterSeat.getSeatType());

            if(showSeat.getSeatType().equals(SeatType.CLASSIC))
                showSeat.setPrice(showSeatsDto.getPriceForClassicSeats());
            else
                showSeat.setPrice(showSeatsDto.getPriceForPremiumSeats());

            //Foreign key mapping
            showSeat.setShow(show);
            showSeat.setAvailable(true);
            showSeat.setFoodAttached(false);

            showSeatList.add(showSeat);
        }
        showRepository.save(show);
        //Child will automatically get saved.....

        return "Show seats has been successfully added";

    }

    public String getMovieName(AddShowDto showDto){

        Integer movieId = showRepository.getMostShowedMovie(showDto.getShowDate());

        Movie movie = movieRepository.findById(movieId).get();

        return movie.getMovieName();


    }

    public LocalTime getShowtime(Integer movieId, Integer theaterId) {
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);
        Optional<Theater> optionalTheater = theaterRepository.findById(theaterId);

        if (!optionalMovie.isPresent()) {
            throw new RuntimeException("Movie not found with ID: " + movieId);
        }
        if (!optionalTheater.isPresent()) {
            throw new RuntimeException("Theater not found with ID: " + theaterId);
        }

        Movie movie = optionalMovie.get();
        Theater theater = optionalTheater.get();
        List<Show> showList = theater.getShowList();

        if (showList.isEmpty()) {
            throw new RuntimeException("No shows available for theater ID: " + theaterId);
        }

        LocalTime ans = null;
        for (Show show : showList) {
            if (show.getMovie().equals(movie)) { // Use equals to compare objects
                ans = show.getTime();
                break; // Exit loop once found
            }
        }

        if (ans == null) {
            throw new RuntimeException("No show found for movie ID: " + movieId + " in theater ID: " + theaterId);
        }

        return ans;
    }

}
