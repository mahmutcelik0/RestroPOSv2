package com.restropos.systemmenu.repository;

import com.restropos.systemmenu.entity.Product;
import com.restropos.systemmenu.entity.GenericId;
import com.restropos.systemshop.entity.Workspace;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, GenericId> {
    List<Product> findAllByWorkspaceBusinessDomainOrderByProductName(String businessDomain);

    boolean existsByProductNameAndWorkspace(String productName, Workspace workspace);

    @Transactional
    @Modifying
    void deleteProductByProductNameAndWorkspace(String productName,Workspace workspace);

    Optional<Product> getProductByProductNameAndWorkspace(String productName,Workspace workspace);

}

