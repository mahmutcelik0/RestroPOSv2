package com.restropos.systemshop.populator;

import com.restropos.systemcore.populator.AbstractPopulator;
import com.restropos.systemshop.dto.BasicUserDto;
import com.restropos.systemshop.entity.user.BasicUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BasicUserDtoPopulator extends AbstractPopulator<BasicUser, BasicUserDto> {
    @Autowired
    private WorkspaceDtoPopulator workspaceDtoPopulator;

    @Override
    protected BasicUserDto populate(BasicUser basicUser, BasicUserDto basicUserDto) {
        basicUserDto.setEmail(basicUser.getEmail());
        basicUserDto.setDeviceName(basicUser.getDeviceName());
        basicUserDto.setWorkspaceDto(workspaceDtoPopulator.populate(basicUser.getWorkspace()));
        return basicUserDto;
    }

    @Override
    public BasicUserDto generateTarget() {
        return new BasicUserDto();
    }
}
