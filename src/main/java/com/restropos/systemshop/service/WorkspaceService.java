package com.restropos.systemshop.service;

import com.restropos.systemshop.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceService {
    @Autowired
    private WorkspaceRepository workspaceRepository;

    public boolean checkWorkspaceExists(String businessName) {
        return workspaceRepository.existsById(businessName);
    }

    public boolean checkWorkspaceDomainExists(String businessDomain) {
        return workspaceRepository.existsWorkspaceByBusinessDomain(businessDomain);
    }

}
