package com.restropos.systemshop.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemshop.dto.WorkspaceDto;
import com.restropos.systemshop.entity.user.Workspace;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceDtoPopulator extends AbstractPopulator<Workspace, WorkspaceDto> {
    @Override
    protected WorkspaceDto populate(Workspace workspace, WorkspaceDto workspaceDto) {
        return new WorkspaceDto(workspace.getBusinessName(),workspace.getBusinessDomain(),workspace.getBusinessLogo());
    }

    @Override
    public WorkspaceDto generateTarget() {
        return new WorkspaceDto();
    }
}
