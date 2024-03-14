package com.restropos.systemshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private WorkspaceDto workspace;
    private ImageDto image;
    private String categoryTitle;
}
