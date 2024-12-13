package com.kk.Book_My_Show.Transformers;

import com.kk.Book_My_Show.Models.User;
import com.kk.Book_My_Show.ResponseDto.UserResponseDto;

public class UserTransformer {

//    public static User convertDtoToEntity(AddUserDto userDto){
//
//        User userObj = User.builder().age(userDto.getAge()).email(userDto.getEmailId()).mobNo(userDto.getMobNo())
//                .name(userDto.getName()).build();
//        return userObj;
//    }

    public static UserResponseDto convertEntityToDto(User user){

        UserResponseDto userResponseDto = UserResponseDto.builder().age(user.getAge()).name(user.getName())
                .mobNo(user.getMobNo()).build();
        return userResponseDto;
    }


}
