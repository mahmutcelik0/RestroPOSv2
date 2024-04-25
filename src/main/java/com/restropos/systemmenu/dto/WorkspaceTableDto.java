package com.restropos.systemmenu.dto;

import com.restropos.systemshop.dto.ImageDto;
import com.restropos.systemshop.dto.WorkspaceDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceTableDto {
    private String tableId;
    private String tableName;
    private WorkspaceDto workspaceDto;
    private ImageDto imageDto;
}
