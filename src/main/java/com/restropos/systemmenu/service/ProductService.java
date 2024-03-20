package com.restropos.systemmenu.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.security.SecurityProvideService;
import com.restropos.systemimage.constants.FolderEnum;
import com.restropos.systemimage.service.ImageService;
import com.restropos.systemmenu.constants.ChoiceEnum;
import com.restropos.systemmenu.dto.ProductDto;
import com.restropos.systemmenu.entity.Product;
import com.restropos.systemmenu.entity.ProductModifier;
import com.restropos.systemmenu.populator.ProductDtoPopulator;
import com.restropos.systemmenu.repository.ProductRepository;
import com.restropos.systemshop.entity.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SecurityProvideService securityProvideService;

    @Autowired
    private ProductDtoPopulator productDtoPopulator;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductSubmodifierService productSubmodifierService;

    @Autowired
    private ProductModifierService productModifierService;

    public List<ProductDto> getAllProducts() throws NotFoundException {

        return productDtoPopulator.populateAll(productRepository.findAllByWorkspaceBusinessDomain(securityProvideService.getWorkspace().getBusinessDomain()));

    }

    public ResponseEntity<ResponseMessage> deleteProduct(String productName) throws NotFoundException {
        Workspace workspace = securityProvideService.getWorkspace();
        if (!productRepository.existsByProductNameAndWorkspace(productName,workspace)){
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,CustomResponseMessage.PRODUCT_NOT_FOUND));
        }
        productRepository.deleteProductByProductNameAndWorkspace(productName,workspace);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK,CustomResponseMessage.PRODUCT_REMOVED_SUCCESSFULLY));

    }

    public ResponseEntity<ResponseMessage> addNewProduct(ProductDto productDto, MultipartFile image) throws NotFoundException, IOException {
        Workspace workspace = securityProvideService.getWorkspace();
        if(productRepository.existsByProductNameAndWorkspace(productDto.getProductName(),workspace)){
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,CustomResponseMessage.PRODUCT_ALREADY_EXISTS));
        }else if(!categoryService.checkCategoryTitleExists(productDto.getCategoryTitle(),workspace)){
            return ResponseEntity.internalServerError().body(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,CustomResponseMessage.CATEGORY_TITLE_NOT_FOUND));
        }

        Product product = Product.builder()
                .productName(productDto.getProductName())
                .workspace(workspace)
                .productDescription(productDto.getProductDescription())
                .price(productDto.getPrice())
                .category(categoryService.getCategoryByTitle(productDto.getCategoryTitle(),workspace))
                .image(imageService.save(image, FolderEnum.PRODUCTS))
                .build();
        var savedProduct = productRepository.save(product);

        productDto.getProductModifiers().forEach(e->{
            ProductModifier productModifier = ProductModifier.builder()
                    .product(savedProduct)
                    .productModifierName(e.getProductModifierName())
                    .choice(ChoiceEnum.valueOf(e.getChoice()))
                    .isRequired(e.getIsRequired())
                    .productSubmodifierSet(e.getProductSubmodifierSet().stream().map(x -> {
                        try {
                            return productSubmodifierService.getProductSubmodifier(x.getProductSubmodifierName(),x.getPrice());
                        } catch (NotFoundException ex) {
                            throw new RuntimeException(ex);
                        }
                    }).collect(Collectors.toSet()))
                    .build();
            productModifierService.save(productModifier);
        });


        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK,CustomResponseMessage.PRODUCT_CREATED_SUCCESSFULLY));
    }
}