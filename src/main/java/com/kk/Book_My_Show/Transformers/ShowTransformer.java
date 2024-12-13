package com.kk.Book_My_Show.Transformers;

import com.kk.Book_My_Show.Models.Show;
import com.kk.Book_My_Show.RequestDto.AddShowDto;

public class ShowTransformer {

    public static Show convertDtoToEntity(AddShowDto addShowDto){

        Show show = Show.builder().time(addShowDto.getShowStartTime())
                .date(addShowDto.getShowDate()).build();

        return show;

    }
}
