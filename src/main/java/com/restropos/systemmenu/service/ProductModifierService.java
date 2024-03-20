package com.restropos.systemmenu.service;

import com.restropos.systemmenu.entity.ProductModifier;
import com.restropos.systemmenu.repository.ProductModifierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductModifierService {
    @Autowired
    private ProductModifierRepository productModifierRepository;

    public void save(ProductModifier productModifier) {
        productModifierRepository.save(productModifier);
    }
}
