package com.restropos.systemshop.repository;

import com.restropos.systemshop.entity.user.BasicUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BasicUserRepository extends JpaRepository<BasicUser,Long> {
    @Query("select b from BasicUser as b where b.email = ?1")
    Optional<BasicUser> findBasicUserByEmail(String email);

    @Query("select b from BasicUser as b where b.role.roleName = ?1")
    List<BasicUser> getAllStaffsByRole(String roleName);
}
