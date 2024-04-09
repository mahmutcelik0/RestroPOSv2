package com.restropos.systemmenu.dto;

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
    private String tableName;
    private WorkspaceDto workspaceDto;
}
