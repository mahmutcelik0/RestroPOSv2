package com.restropos.systemmenu.api;

import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemmenu.dto.WorkspaceTableDto;
import com.restropos.systemmenu.service.WorkspaceTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tables")
public class WorkspaceTableApi {
    @Autowired
    private WorkspaceTableService workspaceTableService;

    @GetMapping
    public List<WorkspaceTableDto> getAllTablesOfWorkspace() throws NotFoundException {
        return workspaceTableService.getAllTablesOfWorkspace();
    }

    @PostMapping
    public ResponseEntity<WorkspaceTableDto> addNewTable(@RequestParam String tableName) throws NotFoundException, AlreadyUsedException, IOException {
        return workspaceTableService.addNewTable(tableName);
    }

    @DeleteMapping
    public ResponseEntity<ResponseMessage> deleteTable(@RequestParam String tableName) throws NotFoundException {
        return workspaceTableService.deleteTable(tableName);
    }
}
