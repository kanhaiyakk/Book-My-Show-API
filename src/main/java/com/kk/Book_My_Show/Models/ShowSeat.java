package com.kk.Book_My_Show.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kk.Book_My_Show.Enums.SeatType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "show_seat")
public class ShowSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String seatNo;

    @Enumerated(value = EnumType.STRING)
    private SeatType seatType;

    private int price; //Price stored for each seat..

    private boolean isAvailable;

    private boolean isFoodAttached;

    @ManyToOne
    @JoinColumn
    @JsonBackReference
    private Show show;

}
