package com.restropos.systemmenu.service;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.security.SecurityProvideService;
import com.restropos.systemimage.service.ImageService;
import com.restropos.systemmenu.dto.WorkspaceTableDto;
import com.restropos.systemmenu.entity.WorkspaceTable;
import com.restropos.systemmenu.populator.WorkspaceTableDtoPopulator;
import com.restropos.systemmenu.repository.WorkspaceTableRepository;
import com.restropos.systemshop.entity.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class WorkspaceTableService {
    @Autowired
    private WorkspaceTableRepository workspaceTableRepository;
    @Autowired
    private SecurityProvideService securityProvideService;
    @Autowired
    private WorkspaceTableDtoPopulator workspaceTableDtoPopulator;
    @Autowired
    private ImageService imageService;

    public List<WorkspaceTableDto> getAllTablesOfWorkspace() throws NotFoundException {
        Workspace workspace = securityProvideService.getWorkspace();

        return workspaceTableDtoPopulator.populateAll(workspaceTableRepository.findAllByWorkspace(workspace));
    }

    public ResponseEntity<WorkspaceTableDto> addNewTable(String tableName, String origin) throws NotFoundException, AlreadyUsedException, IOException {
        Workspace workspace = securityProvideService.getWorkspace();
        WorkspaceTable workspaceTable = null;
        if (workspaceTableRepository.existsWorkspaceTableByTableNameAndWorkspace(tableName, workspace))
            throw new AlreadyUsedException(CustomResponseMessage.WORKSPACE_TABLE_ALREADY_EXISTS);
        try {
            workspaceTable = WorkspaceTable.builder()
                    .tableId(workspace.getBusinessDomain() + "-" + tableName)
                    .tableName(tableName)
                    .workspace(workspace)
                    .image(imageService.generateQrForTable( workspace.getBusinessDomain() + "-" + tableName,origin)).build();
            var generatedTable = workspaceTableRepository.save(workspaceTable);

            return ResponseEntity.ok(workspaceTableDtoPopulator.populate(generatedTable));
        } catch (Exception ex) {
            imageService.deleteForTable(workspaceTable.getImage().getImageName());
            throw new RuntimeException(CustomResponseMessage.WORKSPACE_TABLE_COULD_NOT_CREATED);
        }
    }

    public ResponseEntity<ResponseMessage> deleteTable(String tableName) throws NotFoundException {
        Workspace workspace = securityProvideService.getWorkspace();
        Optional<WorkspaceTable> workspaceTable = workspaceTableRepository.findWorkspaceTableByTableNameAndWorkspace(tableName, workspace);
        if (workspaceTable.isEmpty()) throw new NotFoundException(CustomResponseMessage.WORKSPACE_TABLE_DOES_NOT_EXIST);
        workspaceTableRepository.delete(workspaceTable.get());
        return ResponseEntity.ok(new ResponseMessage(HttpStatus.OK, CustomResponseMessage.WORKSPACE_TABLE_REMOVED_SUCCESSFULLY));
    }

    public ResponseEntity<WorkspaceTableDto> getTableById(String tableId) throws NotFoundException {
        Optional<WorkspaceTable> workspaceTable = workspaceTableRepository.findWorkspaceTableByTableId(tableId);
        if(workspaceTable.isEmpty()) throw new NotFoundException(CustomResponseMessage.WORKSPACE_TABLE_DOES_NOT_EXIST);
        return ResponseEntity.ok(workspaceTableDtoPopulator.populate(workspaceTable.get()));
    }

    public WorkspaceTable getWorkspaceTableById(String tableId) throws NotFoundException {
        Optional<WorkspaceTable> workspaceTable = workspaceTableRepository.findWorkspaceTableByTableId(tableId);
        if(workspaceTable.isEmpty()) throw new NotFoundException(CustomResponseMessage.WORKSPACE_TABLE_DOES_NOT_EXIST);
        return workspaceTable.get();
    }
}
