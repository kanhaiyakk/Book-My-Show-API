package com.kk.Book_My_Show.RequestDto;

import lombok.Data;

@Data
public class ShowSeatsDto {

    private int showId;
    private int priceForClassicSeats;
    private int priceForPremiumSeats;

}
