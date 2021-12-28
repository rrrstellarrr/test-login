package com.member.test.repository;

import com.member.test.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Modifying
    @Query("update User u set u.password=:password, u.email=:email where u.id = :id")
    int editByUser(@Param("id") Long id, @Param("password") String password, @Param("email") String email);
}
