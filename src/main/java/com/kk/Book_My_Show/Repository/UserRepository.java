package com.kk.Book_My_Show.Repository;

import com.kk.Book_My_Show.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "select * from users where age >= :value",nativeQuery = true)
    List<User> findUserWithAgeGreater(Integer value);
    //This is a custom function that you have defined
    //You need to write a query on top of this
}
