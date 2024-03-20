package com.restropos.systemmenu.repository;

import com.restropos.systemmenu.entity.Product;
import com.restropos.systemshop.entity.Workspace;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByWorkspaceBusinessDomain(String businessDomain);


    boolean existsByProductNameAndWorkspace(String productName, Workspace workspace);

    @Transactional
    @Modifying
    void deleteProductByProductNameAndWorkspace(String productName,Workspace workspace);
}
