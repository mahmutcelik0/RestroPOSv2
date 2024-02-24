package com.restropos.systemshop.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.RegisterDto;
import com.restropos.systemshop.entity.Role;
import com.restropos.systemshop.entity.user.Admin;
import com.restropos.systemshop.entity.user.Workspace;
import com.restropos.systemshop.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceService {
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean checkWorkspaceNameExists(String businessName) {
        return workspaceRepository.existsWorkspaceByBusinessName(businessName);
    }

    public boolean checkWorkspaceDomainExists(String businessDomain) {
        return workspaceRepository.existsById(businessDomain);
    }

    public ResponseEntity<ResponseMessage> registerNewWorkspace(RegisterDto registerDto) {
        var workspaceDto = registerDto.getWorkspace();
        var systemUserDto = registerDto.getSystemUser();

        if (systemUserService.checkSystemUserExists(systemUserDto.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, CustomResponseMessage.EMAIL_ALREADY_USED), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (checkWorkspaceNameExists(workspaceDto.getBusinessName())) {
            return new ResponseEntity<>(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,CustomResponseMessage.WORKSPACE_NAME_ALREADY_USED), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (checkWorkspaceDomainExists(workspaceDto.getBusinessDomain())) {
            return new ResponseEntity<>(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,CustomResponseMessage.WORKSPACE_DOMAIN_ALREADY_USED), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            Workspace workspace = Workspace.builder()
                    .businessName(workspaceDto.getBusinessName())
                    .businessDomain(workspaceDto.getBusinessDomain())
                    .businessLogo(workspaceDto.getBusinessLogo())
                    .build();

            Admin admin = new Admin(systemUserDto.getEmail(), passwordEncoder.encode(systemUserDto.getPassword()), systemUserDto.getFirstName(), systemUserDto.getLastName(), true, workspace,roleService.getRole(UserTypes.ADMIN.getName()));

            systemUserService.save(admin);

            return new ResponseEntity<>(new ResponseMessage(HttpStatus.OK,CustomResponseMessage.WORKSPACE_CREATED), HttpStatus.OK);
        }
    }

}
