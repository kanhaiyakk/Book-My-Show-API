package com.kk.Book_My_Show.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kk.Book_My_Show.Enums.SeatType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "theater_seats")
public class TheaterSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String seatNo;

    @Enumerated(value = EnumType.STRING)
    private SeatType seatType;

    @ManyToOne
    @JoinColumn
    @JsonBackReference
    private Theater theater;
}
