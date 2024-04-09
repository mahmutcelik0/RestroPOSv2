package com.restropos.systemmenu.repository;

import com.restropos.systemmenu.entity.WorkspaceTable;
import com.restropos.systemshop.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceTableRepository extends JpaRepository<WorkspaceTable,String> {
    List<WorkspaceTable> findAllByWorkspace(Workspace workspace);

    boolean existsWorkspaceTableByTableNameAndWorkspace(String tableName, Workspace workspace);

    Optional<WorkspaceTable> findWorkspaceTableByTableNameAndWorkspace(String tableName, Workspace workspace);
}
