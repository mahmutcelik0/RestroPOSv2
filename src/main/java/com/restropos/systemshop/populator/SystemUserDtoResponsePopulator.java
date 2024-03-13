package com.restropos.systemshop.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemshop.dto.SystemUserDtoResponse;
import com.restropos.systemshop.entity.user.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SystemUserDtoResponsePopulator extends AbstractPopulator<SystemUser, SystemUserDtoResponse> {
    @Autowired
    private WorkspaceDtoPopulator workspaceDtoPopulator;

    @Override
    protected SystemUserDtoResponse populate(SystemUser systemUser, SystemUserDtoResponse systemUserDtoResponse) {
       systemUserDtoResponse.setId(systemUser.getId());
       systemUserDtoResponse.setRole(systemUser.getRole().getRoleName());
       systemUserDtoResponse.setWorkspaceDto(workspaceDtoPopulator.populate(systemUser.getWorkspace()));
       systemUserDtoResponse.setEmail(systemUser.getEmail());
       systemUserDtoResponse.setFirstName(systemUser.getFirstName());
       systemUserDtoResponse.setLastName(systemUser.getLastName());
        return systemUserDtoResponse;
    }

    @Override
    public SystemUserDtoResponse generateTarget() {
        return new SystemUserDtoResponse();
    }
}
