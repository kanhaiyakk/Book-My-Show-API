package com.kk.Book_My_Show.Repository;

import com.kk.Book_My_Show.Models.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheaterRepository extends JpaRepository<Theater, Integer> {
    Theater findByLocation(String location);
}
