package com.restropos.systemmenu.repository;

import com.restropos.systemmenu.entity.ProductSubmodifier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductSubmodifierRepository extends JpaRepository<ProductSubmodifier,Long> {
    Optional<ProductSubmodifier> findProductSubmodifierByProductSubmodifierNameAndPrice(String productSubmodifierName,Double price);
}
