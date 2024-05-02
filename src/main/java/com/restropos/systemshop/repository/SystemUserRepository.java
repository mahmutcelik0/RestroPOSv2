package com.restropos.systemshop.repository;

import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.entity.user.SystemUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
    @Query("select s from SystemUser as s where s.email = ?1")
    Optional<SystemUser> findSystemUserByEmail(String email);

    @Query("select s from SystemUser as s where s.role.roleName = ?1")
    List<SystemUser> getAllStaffsByRoleName(String roleName);

    @Transactional
    @Modifying
    void deleteSystemUserByEmail(String email);

    boolean existsSystemUserByEmail(String email);

    @Query("select s from SystemUser as s where s.role.roleName != ?1 and s.workspace.businessDomain= ?2")
    List<SystemUser> getAllStaffsExceptRole(String userType,String businessDomain);


}
