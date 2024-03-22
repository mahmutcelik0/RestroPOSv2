package com.restropos.systemmenu.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemmenu.dto.FeaturedGroupsDto;
import com.restropos.systemmenu.entity.FeaturedGroups;
import com.restropos.systemshop.populator.WorkspaceDtoPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeaturedGroupsDtoPopulator extends AbstractPopulator<FeaturedGroups, FeaturedGroupsDto> {
    @Autowired
    private WorkspaceDtoPopulator workspaceDtoPopulator;

    @Autowired
    private ProductDtoPopulator productDtoPopulator;

    @Override
    protected FeaturedGroupsDto populate(FeaturedGroups featuredGroups, FeaturedGroupsDto featuredGroupsDto) {
        featuredGroupsDto.setGroupName(featuredGroups.getGroupName());
        featuredGroupsDto.setWorkspaceDto(workspaceDtoPopulator.populate(featuredGroups.getWorkspace()));
        featuredGroupsDto.setProducts(productDtoPopulator.populateAll(featuredGroups.getProductSet().stream().toList()));
        return featuredGroupsDto;
    }

    @Override
    public FeaturedGroupsDto generateTarget() {
        return new FeaturedGroupsDto();
    }
}
