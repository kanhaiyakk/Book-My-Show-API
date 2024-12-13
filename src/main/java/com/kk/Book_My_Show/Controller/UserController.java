package com.kk.Book_My_Show.Controller;

import com.kk.Book_My_Show.Models.Ticket;
import com.kk.Book_My_Show.Models.User;
import com.kk.Book_My_Show.RequestDto.AddUserDto;
import com.kk.Book_My_Show.ResponseDto.UserResponseDto;
import com.kk.Book_My_Show.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/add")
    public String addUser(@RequestBody AddUserDto user){

        try{
            String result = userService.addUser(user);
            return result;
        }catch (Exception e){
            return e.getMessage();
        }
    }

    //Get oldest User Object by age
    @GetMapping("/getOlderUser")
    public UserResponseDto getOldestUser(){

        try{
            UserResponseDto userResponseDto = userService.getOldestUser();

            userResponseDto.setStatusCode("200");
            userResponseDto.setStatusMessage("SUCCESS");
            return userResponseDto;

        }catch (Exception e){
            UserResponseDto responseDto = new UserResponseDto();
            responseDto.setStatusCode("500");
            responseDto.setStatusMessage("Failure");
            return responseDto;
        }
    }

    @GetMapping("/findUsersGreaterThanAAge")
    public List<User> getAllUsers(@RequestParam("age")Integer age){

        return userService.getAllUserGreaterThan(age);
    }
    @GetMapping("/getAllTicketsBookedByPerson")
    public List<Ticket> getAllTicketsBookedByPerson(@PathVariable Integer userId) throws Exception {

        List<Ticket> ticketList = userService.getAllTicketsBookedByPerson(userId);
        return ticketList;
    }

}
