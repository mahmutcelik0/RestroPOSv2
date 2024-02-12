package com.restropos.systemshop.repository;

import com.restropos.systemshop.entity.user.SystemUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SystemUserRepository extends JpaRepository<SystemUser,Long> {
    @Query("select s from SystemUser as s where s.email = ?1")
    Optional<SystemUser> findSystemUserByEmail(String email);

}
