package com.restropos.systemmenu.repository;

import com.restropos.systemmenu.entity.Category;
import com.restropos.systemshop.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category as c where c.workspace = ?1 and c.categoryTitle = ?2")
    boolean categoryTitleIsExistInWorkspace(Workspace workspace, String categoryTitle);

    @Query("select c from Category as c where c.workspace = ?1")
    List<Category> findAllByWorkspace(Workspace workspace);

    @Query("select c from Category as c where c.categoryTitle = ?1 and c.workspace = ?2")
    Optional<Category> getCategoryByCategoryTitleAndWorkspace(String categoryTitle, Workspace workspace);

    boolean existsByCategoryTitleAndWorkspace(String categoryTitle, Workspace workspace);

    List<Category> findAllByWorkspaceBusinessDomain(String businessDomain);
}
