package com.restropos.systemshop.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.security.SecurityProvideService;
import com.restropos.systemimage.constants.FolderEnum;
import com.restropos.systemimage.service.ImageService;
import com.restropos.systemshop.dto.CategoryDto;
import com.restropos.systemshop.entity.Category;
import com.restropos.systemshop.entity.Workspace;
import com.restropos.systemshop.populator.CategoryDtoPopulator;
import com.restropos.systemshop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private ImageService imageService;

    @Autowired
    private CategoryDtoPopulator categoryDtoPopulator;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SecurityProvideService securityProvideService;

    public ResponseEntity<CategoryDto> addNewCategory(MultipartFile image, String categoryTitle) throws NotFoundException, IOException, AlreadyUsedException {
        Workspace workspace = securityProvideService.getWorkspace();
        if (categoryRepository.getCategoryByCategoryTitleAndWorkspace(categoryTitle,workspace).isPresent())
            throw new AlreadyUsedException(CustomResponseMessage.CATEGORY_TITLE_ALREADY_USED);

        Category category = Category.builder()
                .categoryTitle(categoryTitle)
                .image(imageService.save(image, FolderEnum.CATEGORIES))
                .workspace(workspace)
                .build();
        return ResponseEntity.ok(categoryDtoPopulator.populate(categoryRepository.save(category)));

    }

    public List<CategoryDto> getAllCategories() throws NotFoundException {
        return categoryDtoPopulator.populateAll(categoryRepository.findAllByWorkspace(securityProvideService.getWorkspace()));
    }

    public ResponseEntity<ResponseMessage> deleteCategory(String categoryTitle) throws NotFoundException, IOException {
        Optional<Category> category = categoryRepository.getCategoryByCategoryTitleAndWorkspace(categoryTitle, securityProvideService.getWorkspace());
        categoryRepository.delete(category.orElseThrow(() -> new NotFoundException(CustomResponseMessage.CATEGORY_TITLE_NOT_FOUND)));
        imageService.delete(category.get().getImage().getImageName(),FolderEnum.CATEGORIES);
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.CATEGORY_DELETED_SUCCESSFULLY));
    }
}
