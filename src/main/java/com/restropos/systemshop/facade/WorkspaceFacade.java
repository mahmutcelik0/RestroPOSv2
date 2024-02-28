package com.restropos.systemshop.facade;

import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemshop.dto.RegisterDto;
import com.restropos.systemshop.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceFacade {
    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<ResponseMessage> registerNewWorkspace(RegisterDto registerDto) {
        registerDto.getSystemUser().setPassword(passwordEncoder.encode(registerDto.getSystemUser().getPassword()));
        return workspaceService.registerNewWorkspace(registerDto);
    }

    public boolean checkWorkspaceDomainValid(String businessDomain) {
        return workspaceService.checkWorkspaceDomainValid(businessDomain);
    }

    public boolean checkWorkspaceExists(String businessDomain) {
        return !checkWorkspaceDomainValid(businessDomain);
    }
}
