package com.kk.Book_My_Show.RequestDto;

import com.kk.Book_My_Show.Enums.Genre;
import com.kk.Book_My_Show.Enums.Language;
import lombok.Data;

import java.util.Date;

@Data
public class MovieEntryDto {

    private String movieName;
    private double duration;
    private double rating;
    private Date releaseDate;
    private int boxOfficeCollection;
    private Genre genre;
    private Language language;
}
