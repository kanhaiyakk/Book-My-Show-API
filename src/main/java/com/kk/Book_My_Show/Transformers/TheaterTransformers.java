package com.kk.Book_My_Show.Transformers;

import com.kk.Book_My_Show.Models.Theater;
import com.kk.Book_My_Show.RequestDto.TheaterEntryDto;

public class TheaterTransformers {

    public static Theater convertDtoToEntity(TheaterEntryDto theaterEntryDto){

        Theater theater = Theater.builder().location(theaterEntryDto.getLocation())
                .name(theaterEntryDto.getName()).build();

        return theater;
    }

}
