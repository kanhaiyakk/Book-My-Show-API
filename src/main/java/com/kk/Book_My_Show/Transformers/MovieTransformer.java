package com.kk.Book_My_Show.Transformers;


import com.kk.Book_My_Show.Models.Movie;
import com.kk.Book_My_Show.RequestDto.MovieEntryDto;

public class MovieTransformer {

    public static Movie convertDtoToEntity(MovieEntryDto movieEntryDto) {

        return Movie.builder()
                .movieName(movieEntryDto.getMovieName())
                .duration(movieEntryDto.getDuration())
                .rating(movieEntryDto.getRating())
                .releaseDate(movieEntryDto.getReleaseDate())
                .boxOfficeCollection(movieEntryDto.getBoxOfficeCollection())
                .genre(movieEntryDto.getGenre())
                .language(movieEntryDto.getLanguage())
                .build();
    }
}

