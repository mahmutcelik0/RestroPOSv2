package com.restropos.systemshop.api;

import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemshop.dto.CategoryDto;
import com.restropos.systemshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryApi {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories() throws NotFoundException {
        return categoryService.getAllCategories();
    }
    @PostMapping
    public ResponseEntity<CategoryDto> addNewCategory(@RequestPart MultipartFile image, @RequestPart String categoryTitle) throws AlreadyUsedException, NotFoundException, IOException {
        return categoryService.addNewCategory(image,categoryTitle);
    }

    @DeleteMapping("/{categoryTitle}")
    public ResponseEntity<ResponseMessage> deleteCategory(@PathVariable String categoryTitle) throws NotFoundException, IOException {
        return categoryService.deleteCategory(categoryTitle);
    }
}
