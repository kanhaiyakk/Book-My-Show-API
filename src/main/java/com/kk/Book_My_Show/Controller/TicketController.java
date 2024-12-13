package com.kk.Book_My_Show.Controller;

import com.kk.Book_My_Show.RequestDto.TicketRequestDto;
import com.kk.Book_My_Show.ResponseDto.TicketResponseDto;
import com.kk.Book_My_Show.Services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private TicketService ticketService;


    @PostMapping("/book-ticket")
    public ResponseEntity<TicketResponseDto> bookTicket(@RequestBody TicketRequestDto ticketRequestDto){

        try{
            TicketResponseDto response =  ticketService.bookTicket(ticketRequestDto);
            response.setResponseMessage("Ticket booked successfully");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            TicketResponseDto ticketResponseDto = new TicketResponseDto();
            ticketResponseDto.setResponseMessage(e.getMessage());
            return new ResponseEntity<>(ticketResponseDto,HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{deleteTicket}")
    public String cancelTicket(@PathVariable("deleteTicket") Integer ticketId) throws Exception {

        ticketService.cancelTicket(ticketId);
        return "Ticket has been successfully cancelled";
    }


}
