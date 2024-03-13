package com.restropos.systemshop.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemshop.dto.WorkspaceDto;
import com.restropos.systemshop.entity.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class WorkspaceDtoPopulator extends AbstractPopulator<Workspace, WorkspaceDto> {
    @Autowired
    private ImageDtoPopulator imageDtoPopulator;
    @Override
    protected WorkspaceDto populate(Workspace workspace, WorkspaceDto workspaceDto) {
        if(ObjectUtils.isEmpty(workspace.getImage())){
            return new WorkspaceDto(workspace.getBusinessName(),workspace.getBusinessDomain(),null);
        }
        return new WorkspaceDto(workspace.getBusinessName(),workspace.getBusinessDomain(),imageDtoPopulator.populate(workspace.getImage()));
    }

    @Override
    public WorkspaceDto generateTarget() {
        return new WorkspaceDto();
    }
}
