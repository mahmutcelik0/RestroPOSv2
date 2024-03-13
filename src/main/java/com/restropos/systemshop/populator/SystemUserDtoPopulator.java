package com.restropos.systemshop.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemshop.dto.SystemUserDto;
import com.restropos.systemshop.entity.user.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SystemUserDtoPopulator extends AbstractPopulator<SystemUser, SystemUserDto> {
    @Autowired
    private WorkspaceDtoPopulator workspaceDtoPopulator;

    @Override
    protected SystemUserDto populate(SystemUser systemUser, SystemUserDto systemUserDto) {
        systemUserDto.setFirstName(systemUser.getFirstName());
        systemUserDto.setLastName(systemUser.getLastName());
        systemUserDto.setEmail(systemUser.getEmail());
        systemUserDto.setWorkspaceDto(workspaceDtoPopulator.populate(systemUser.getWorkspace()));
        systemUserDto.setRole(systemUser.getRole().getRoleName());
        return systemUserDto;
    }

    @Override
    public SystemUserDto generateTarget() {
        return new SystemUserDto();
    }
}