package com.kk.Book_My_Show.Controller;

import com.kk.Book_My_Show.RequestDto.AddShowDto;
import com.kk.Book_My_Show.RequestDto.ShowSeatsDto;
import com.kk.Book_My_Show.Services.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/show")
public class ShowController {

    @Autowired
    private ShowService showService;

    @PostMapping("/add")
    public String addShow(@RequestBody AddShowDto addShowDto){
        try{
            return showService.addShow(addShowDto);
        }catch (Exception e){
            return e.getMessage();
        }

    }

    @PostMapping("/associate-seats")
    public String associateSeats(@RequestBody ShowSeatsDto showSeatsDto){

        try{
            return showService.associateShowSeats(showSeatsDto);
        }catch (Exception e){
            return e.getMessage();
        }

    }

    @GetMapping("/most-recommended-movie-name")
    public String getMovieName(AddShowDto addShowDto){

        return showService.getMovieName(addShowDto);
    }

    @GetMapping("/{theaterId}/{movieId}")
    public ResponseEntity<?> getShowtime(@PathVariable Integer movieId, @PathVariable Integer theaterId) {
        try {
            LocalTime showtime = showService.getShowtime(movieId, theaterId);
            return ResponseEntity.ok(showtime); // Return 200 OK with the showtime
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Return 404 Not Found with error message
        }
    }


}
