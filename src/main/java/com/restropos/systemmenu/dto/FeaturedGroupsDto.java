package com.restropos.systemmenu.dto;

import com.restropos.systemshop.dto.WorkspaceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FeaturedGroupsDto {
    private String groupName;
    private WorkspaceDto workspaceDto;
    private List<ProductDto> products;
}
