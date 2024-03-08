package com.restropos.systemshop.facade;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemimage.service.ImageService;
import com.restropos.systemshop.dto.RegisterDto;
import com.restropos.systemshop.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class WorkspaceFacade {
    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageService imageService;

    public ResponseEntity<ResponseMessage> registerNewWorkspace(RegisterDto registerDto, MultipartFile multipartFile) throws NotFoundException, IOException {
        registerDto.getSystemUser().setPassword(passwordEncoder.encode(registerDto.getSystemUser().getPassword()));
        return workspaceService.registerNewWorkspace(registerDto,multipartFile);
    }

    public boolean checkWorkspaceDomainValid(String businessDomain) {
        return workspaceService.checkWorkspaceDomainValid(businessDomain);
    }

    public boolean checkWorkspaceExists(String businessDomain) {
        return !checkWorkspaceDomainValid(businessDomain);
    }

    public void rollbackImage(RegisterDto registerDto, MultipartFile image) throws NotFoundException, IOException {
        imageService.deleteForBusiness(workspaceService.getWorkspace(registerDto.getWorkspace().getBusinessName()).getImage().getImageName());
    }
}
