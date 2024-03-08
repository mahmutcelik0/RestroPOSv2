package com.restropos.systemshop.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemshop.dto.WorkspaceDto;
import com.restropos.systemshop.entity.user.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceDtoPopulator extends AbstractPopulator<Workspace, WorkspaceDto> {
    @Autowired
    private ImageDtoPopulator imageDtoPopulator;
    @Override
    protected WorkspaceDto populate(Workspace workspace, WorkspaceDto workspaceDto) {
        return new WorkspaceDto(workspace.getBusinessName(),workspace.getBusinessDomain(),imageDtoPopulator.populate(workspace.getImage()));
    }

    @Override
    public WorkspaceDto generateTarget() {
        return new WorkspaceDto();
    }
}
