package com.restropos.systemshop.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemimage.service.ImageService;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.RegisterDto;
import com.restropos.systemshop.entity.Image;
import com.restropos.systemshop.entity.user.Admin;
import com.restropos.systemshop.entity.Workspace;
import com.restropos.systemshop.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class WorkspaceService {
    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ImageService imageService;

    public boolean checkWorkspaceNameExists(String businessName) {
        return workspaceRepository.existsWorkspaceByBusinessName(businessName);
    }

    public boolean checkWorkspaceDomainExists(String businessDomain) {
        return workspaceRepository.existsById(businessDomain);
    }

    public boolean checkWorkspaceDomainValid(String businessDomain) {
        return !checkWorkspaceDomainExists(businessDomain);
    }

    public ResponseEntity<ResponseMessage> registerNewWorkspace(RegisterDto registerDto, MultipartFile file) throws NotFoundException, IOException {
        var workspaceDto = registerDto.getWorkspace();
        var systemUserDto = registerDto.getSystemUser();

        if (systemUserService.checkSystemUserExists(systemUserDto.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, CustomResponseMessage.EMAIL_ALREADY_USED), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (checkWorkspaceNameExists(workspaceDto.getBusinessName())) {
            return new ResponseEntity<>(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,CustomResponseMessage.WORKSPACE_NAME_ALREADY_USED), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (checkWorkspaceDomainExists(workspaceDto.getBusinessDomain())) {
            return new ResponseEntity<>(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,CustomResponseMessage.WORKSPACE_DOMAIN_ALREADY_USED), HttpStatus.INTERNAL_SERVER_ERROR);
        } else if(ObjectUtils.isEmpty(file)){
            return new ResponseEntity<>(new ResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR,CustomResponseMessage.IMAGE_COULD_NOT_SAVED),HttpStatus.INTERNAL_SERVER_ERROR);
        } else {

            Image image = imageService.saveForBusiness(file);
            Workspace workspace = Workspace.builder()
                    .businessName(workspaceDto.getBusinessName())
                    .businessDomain(workspaceDto.getBusinessDomain())
                    .image(image)
                    .build();

            Admin admin = new Admin(systemUserDto.getEmail(), systemUserDto.getPassword(), systemUserDto.getFirstName(), systemUserDto.getLastName(), true, workspace,roleService.getRole(UserTypes.ADMIN.getName()));

            systemUserService.save(admin);

            return new ResponseEntity<>(new ResponseMessage(HttpStatus.OK,CustomResponseMessage.WORKSPACE_CREATED), HttpStatus.OK);
        }
    }

    public List<String> getAllWorkspaces() {
        return workspaceRepository.findAll().stream().map(Workspace::getBusinessDomain).toList();
    }

    public Workspace getWorkspace(String businessName) throws NotFoundException {
        return workspaceRepository.findWorkspaceByBusinessName(businessName).orElseThrow(()->new NotFoundException(CustomResponseMessage.WORKSPACE_COULD_NOT_FOUND));
    }
}
