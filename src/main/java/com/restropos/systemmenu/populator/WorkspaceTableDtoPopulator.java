package com.restropos.systemmenu.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemmenu.dto.WorkspaceTableDto;
import com.restropos.systemmenu.entity.WorkspaceTable;
import com.restropos.systemshop.populator.ImageDtoPopulator;
import com.restropos.systemshop.populator.WorkspaceDtoPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceTableDtoPopulator extends AbstractPopulator<WorkspaceTable, WorkspaceTableDto> {
    @Autowired
    private WorkspaceDtoPopulator workspaceDtoPopulator;
    @Autowired
    private ImageDtoPopulator imageDtoPopulator;

    @Override
    protected WorkspaceTableDto populate(WorkspaceTable workspaceTable, WorkspaceTableDto workspaceTableDto) {
        workspaceTableDto.setWorkspaceDto(workspaceDtoPopulator.populate(workspaceTable.getWorkspace()));
        workspaceTableDto.setTableName(workspaceTable.getTableName());
        workspaceTableDto.setImageDto(imageDtoPopulator.populate(workspaceTable.getImage()));
        return workspaceTableDto;
    }

    @Override
    public WorkspaceTableDto generateTarget() {
        return new WorkspaceTableDto();
    }
}