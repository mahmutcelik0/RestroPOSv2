package com.restropos.systemshop.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemshop.dto.SystemUserDto;
import com.restropos.systemshop.entity.SystemUser;
import org.springframework.stereotype.Component;

@Component
public class SystemUserDtoPopulator extends AbstractPopulator<SystemUser, SystemUserDto> {
    @Override
    protected SystemUserDto populate(SystemUser systemUser, SystemUserDto systemUserDto) {
        systemUserDto.setFirstName(systemUser.getFirstName());
        systemUserDto.setLastName(systemUser.getLastName());
        systemUserDto.setEmail(systemUser.getEmail());

        return systemUserDto;
    }

    @Override
    public SystemUserDto generateTarget() {
        return new SystemUserDto();
    }
}