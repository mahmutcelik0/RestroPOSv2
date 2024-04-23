package com.restropos.systemmenu.repository;

import com.restropos.systemmenu.entity.Product;
import com.restropos.systemmenu.entity.GenericId;
import com.restropos.systemshop.entity.Workspace;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, GenericId> {
    List<Product> findAllByWorkspaceBusinessDomainOrderByProductName(String businessDomain);

    boolean existsByProductNameAndWorkspace(String productName, Workspace workspace);

    @Transactional
    @Modifying
    void deleteProductByProductNameAndWorkspace(String productName, Workspace workspace);

    Optional<Product> getProductByProductNameAndWorkspace(String productName, Workspace workspace);

    @Query("select p from Product as p where p.productName = ?1 and p.workspace.businessDomain = ?2")
    Optional<Product> getProductByProductNameAndBusinessDomain(String productName, String businessDomain);
}

