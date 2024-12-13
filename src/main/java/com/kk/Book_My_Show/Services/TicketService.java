package com.kk.Book_My_Show.Services;

import com.kk.Book_My_Show.Exception.NoUserFoundException;
import com.kk.Book_My_Show.Exception.ShowNotFound;
import com.kk.Book_My_Show.Models.*;
import com.kk.Book_My_Show.Repository.MovieRepository;
import com.kk.Book_My_Show.Repository.ShowRepository;
import com.kk.Book_My_Show.Repository.TicketRepository;
import com.kk.Book_My_Show.Repository.UserRepository;
import com.kk.Book_My_Show.RequestDto.TicketRequestDto;
import com.kk.Book_My_Show.ResponseDto.TicketResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class TicketService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private JavaMailSender emailSender;

    public String cancelTicket(Integer ticketId) throws Exception {
        // Ticket Validation
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);
        if (!ticketOptional.isPresent()) {
            throw new Exception("Ticket not Found...");
        }

        Ticket ticket = ticketOptional.get();
        String bookedSeats = ticket.getBookedSeats();

        Show show = ticket.getShow();

        // Split booked seats and convert to list
        String[] bookedSeatsSplitted = bookedSeats.split(",\\s*"); // Split using regex to handle optional whitespace
        List<String> ticketBookedSeats = Arrays.asList(bookedSeatsSplitted);

        List<ShowSeat> showSeatList = show.getShowSeatList();
        for (ShowSeat showSeat : showSeatList) {
            String seatNo = showSeat.getSeatNo();
            if (ticketBookedSeats.contains(seatNo)) {
                showSeat.setAvailable(true); // Mark seat as available
            }
        }

        // Remove ticket from user and show's ticket lists
        User user = ticket.getUser();
        List<Ticket> ticketList = user.getTicketList();
        ticketList.removeIf(ticket1 -> ticket1.getId().equals(ticketId));
        user.setTicketList(ticketList);
        userRepository.save(user);

        List<Ticket> ticketList1 = show.getTicketList();
        ticketList1.removeIf(ticket1 -> ticket1.getId().equals(ticketId));
        show.setTicketList(ticketList1);
        showRepository.save(show);

        ticketRepository.deleteById(ticketId); // Delete ticket

        return "Ticket has been cancelled successfully...";
    }

    public TicketResponseDto bookTicket(TicketRequestDto ticketRequestDto) throws NoUserFoundException, ShowNotFound, Exception {
        // User Validation
        int userId = ticketRequestDto.getUserId();
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new NoUserFoundException("User Id is incorrect");
        }

        // Show Validation
        int showId = ticketRequestDto.getShowId();
        Optional<Show> showOptional = showRepository.findById(showId);
        if (!showOptional.isPresent()) {
            throw new ShowNotFound("Show is not found");
        }
        Show show = showOptional.get();

        // Validation for requested seats availability
        if (!validateShowAvailability(show, ticketRequestDto.getRequestedSeats())) {
            throw new Exception("Requested Seats entered are not available");
        }

        Ticket ticket = new Ticket();
        int totalPrice = calculateTotalPrice(show, ticketRequestDto.getRequestedSeats());
        ticket.setTotalTicketsPrice(totalPrice); // Setting total cost of tickets

        // Update box office collection
        Movie movie = show.getMovie();
        movie.setBoxOfficeCollection(movie.getBoxOfficeCollection() + totalPrice);
        movieRepository.save(movie);

        // Convert booked seats to string
        String bookedSeats = convertListToString(ticketRequestDto.getRequestedSeats());
        ticket.setBookedSeats(bookedSeats);

        // Set bidirectional mapping
        ticket.setUser(userOptional.get());
        ticket.setShow(show);
        ticket = ticketRepository.save(ticket);

        userOptional.get().getTicketList().add(ticket);
        userRepository.save(userOptional.get());

        show.getTicketList().add(ticket);
        showRepository.save(show);

        // Send confirmation email
        sendConfirmationEmail(userOptional.get(), show, bookedSeats);

        return createTicketResponseDto(show, ticket);
    }

    private boolean validateShowAvailability(Show show, List<String> requestedSeats) {
        List<ShowSeat> showSeatList = show.getShowSeatList();

        for (ShowSeat showSeat : showSeatList) {
            String seatNo = showSeat.getSeatNo();
            if (requestedSeats.contains(seatNo)) {
                if (!showSeat.isAvailable()) {
                    System.out.println("Seat " + seatNo + " is unavailable.");
                    return false; // Seat is not available
                }
            }
        }
        return true;
    }



    private int calculateTotalPrice(Show show, List<String> requestedSeats) {
        int totalPrice = 0;

        List<ShowSeat> showSeatList = show.getShowSeatList();

        for (ShowSeat showSeat : showSeatList) {
            if (requestedSeats.contains(showSeat.getSeatNo())) {
                totalPrice += showSeat.getPrice();
                showSeat.setAvailable(false); // Mark seat as unavailable
            }
        }

        return totalPrice;
    }





    private void sendConfirmationEmail(User user, Show show, String bookedSeats) {
        SimpleMailMessage simpleMessageMail = new SimpleMailMessage();
        String body = "Hi " + user.getName() + " ! \n" +
                "You have successfully booked a ticket. Please find the following details:\n" +
                "Booked seat numbers: " + bookedSeats + "\n" +
                "Movie Name: " + show.getMovie().getMovieName() + "\n" +
                "Show Date: " + show.getDate() + "\n" +
                "Show Time: " + show.getTime() + "\n" +
                "Enjoy the Show !!!";

        simpleMessageMail.setSubject("Show Ticket Confirmation Mail");
        simpleMessageMail.setFrom("kanhaiya.kk20598@gmail.com");
        simpleMessageMail.setText(body);
        simpleMessageMail.setTo(user.getEmail());

        emailSender.send(simpleMessageMail);
    }


    String convertListToString(List<String> seats){

        String result = "";
        for(String seatNo : seats){
            result = result + seatNo+", ";
        }
        return result;
    }

    private TicketResponseDto createTicketResponseDto(Show show,Ticket ticket){

        TicketResponseDto ticketResponseDto = TicketResponseDto.builder()
                .bookedSeats(ticket.getBookedSeats())
                .location(show.getTheater().getLocation())
                .theaterName(show.getTheater().getName())
                .movieName(show.getMovie().getMovieName())
                .showDate(show.getDate())
                .showTime(show.getTime())
                .totalPrice(ticket.getTotalTicketsPrice())
                .build();

        return ticketResponseDto;
    }



}
