package com.restropos.systemshop.repository;

import com.restropos.systemshop.entity.user.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WorkspaceRepository extends JpaRepository<Workspace,String> {
    boolean existsWorkspaceByBusinessName(String businessName);
}
