package com.restropos.systemmenu.api;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.utils.JsonUtils;
import com.restropos.systemmenu.dto.ProductDto;
import com.restropos.systemmenu.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductApi {
    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts() throws NotFoundException {
        return productService.getAllProducts();
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ResponseMessage> addNewProduct(@RequestPart String productInformations, @RequestPart MultipartFile image) throws NotFoundException, IOException { //body part olup maplenecek
        return productService.addNewProduct(JsonUtils.productDtoToJson(productInformations), image);
    }

    @DeleteMapping("/{productName}")
    public ResponseEntity<ResponseMessage> deleteProduct(@PathVariable String productName) throws NotFoundException {
        return productService.deleteProduct(productName);
    }

    @GetMapping("/customer")
    public List<ProductDto> getAllProductsForCustomer(HttpServletRequest request) {
        return productService.getAllProductsForCustomer(request.getHeader("Origin"));
    }
}
