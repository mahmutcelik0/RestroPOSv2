package com.restropos.systemshop.repository;

import com.restropos.systemshop.entity.user.BasicUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BasicUserRepository extends JpaRepository<BasicUser,Long> {
    @Query("select b from BasicUser as b where b.email = ?1")
    Optional<BasicUser> findBasicUserByEmail(String email);

}
