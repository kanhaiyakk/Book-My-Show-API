package com.kk.Book_My_Show.Controller;

import com.kk.Book_My_Show.Models.Theater;
import com.kk.Book_My_Show.RequestDto.TheaterEntryDto;
import com.kk.Book_My_Show.RequestDto.TheaterSeatsEntryDto;
import com.kk.Book_My_Show.Services.TheaterServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/theater")
public class TheaterController {

    @Autowired
    TheaterServices theaterServices;

    @PostMapping("/add")
    public String addTheater(@RequestBody TheaterEntryDto theaterEntryDto){

        return theaterServices.addTheater(theaterEntryDto);
    }

    @PostMapping("/addTheaterSeats")
    public String addTheaterSeats(@RequestBody TheaterSeatsEntryDto entryDto){

        return theaterServices.addTheaterSeats(entryDto);

    }

    @GetMapping("/showTime")
    public List<Theater> listOfTheatersShowingParticularTime(@RequestParam LocalTime showTime) {

        List<Theater> theaters = theaterServices.listOfTheatersShowingParticularTime(showTime);

        return theaters;
    }

}
