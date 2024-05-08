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
        if (ObjectUtils.isEmpty(workspace.getImage())) {
            return new WorkspaceDto(workspace.getBusinessName(), workspace.getBusinessDomain(), null, 0, 0.0);
        }
        return new WorkspaceDto(workspace.getBusinessName(), workspace.getBusinessDomain(), imageDtoPopulator.populate(workspace.getImage()), workspace.getTotalReviewCount() == null ? 0 : workspace.getTotalReviewCount(), workspace.getMeanOfWorkspaceStar() == null ? 0.0:workspace.getMeanOfWorkspaceStar());
    }

    @Override
    public WorkspaceDto generateTarget() {
        return new WorkspaceDto();
    }
}
