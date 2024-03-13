package com.restropos.systemshop.repository;

import com.restropos.systemshop.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface WorkspaceRepository extends JpaRepository<Workspace,String> {
    boolean existsWorkspaceByBusinessName(String businessName);

    Optional<Workspace> findWorkspaceByBusinessName(String businessName);
}
