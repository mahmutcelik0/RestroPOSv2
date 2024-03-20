package com.restropos.systemmenu.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemmenu.entity.ProductSubmodifier;
import com.restropos.systemmenu.repository.ProductSubmodifierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductSubmodifierService {
    @Autowired
    private ProductSubmodifierRepository productSubmodifierRepository;


    public ProductSubmodifier getProductSubmodifier(String productSubmodifierName, Double price) throws NotFoundException {
        var productSubmodifier = productSubmodifierRepository.findProductSubmodifierByProductSubmodifierNameAndPrice(productSubmodifierName,price);
        if(productSubmodifier.isEmpty()){
            return productSubmodifierRepository.save(new ProductSubmodifier(productSubmodifierName,price));
        }
        return productSubmodifier.orElseThrow(()->new NotFoundException(CustomResponseMessage.PRODUCT_NOT_FOUND));
    }
}
