package com.restropos.systemshop.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemshop.dto.CategoryDto;
import com.restropos.systemshop.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoPopulator extends AbstractPopulator<Category, CategoryDto> {
    @Autowired
    private WorkspaceDtoPopulator workspaceDtoPopulator;

    @Autowired
    private ImageDtoPopulator imageDtoPopulator;

    @Override
    protected CategoryDto populate(Category category, CategoryDto categoryDto) {
        return new CategoryDto(workspaceDtoPopulator.populate(category.getWorkspace()),imageDtoPopulator.populate(category.getImage()),category.getCategoryTitle());
    }

    @Override
    public CategoryDto generateTarget() {
        return new CategoryDto();
    }
}
