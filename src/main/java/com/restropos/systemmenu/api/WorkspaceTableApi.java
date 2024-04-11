package com.restropos.systemmenu.api;

import com.restropos.systemcore.exception.AlreadyUsedException;
import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemcore.security.SecurityProvideService;
import com.restropos.systemcore.utils.LogUtil;
import com.restropos.systemimage.service.ImageService;
import com.restropos.systemmenu.dto.WorkspaceTableDto;
import com.restropos.systemmenu.service.WorkspaceTableService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
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
    public ResponseEntity<WorkspaceTableDto> addNewTable(@RequestParam String tableName,HttpServletRequest request) throws NotFoundException, AlreadyUsedException, IOException {
        LogUtil.printLog("ORIGIN 1:"+request.getHeader("Origin"),WorkspaceTableApi.class);
        LogUtil.printLog("ORIGIN 1:"+request.getHeader("origin"),WorkspaceTableApi.class);
        return workspaceTableService.addNewTable(tableName,request.getHeader("Origin"));
    }

    @DeleteMapping
    public ResponseEntity<ResponseMessage> deleteTable(@RequestParam String tableName) throws NotFoundException {
        return workspaceTableService.deleteTable(tableName);
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<WorkspaceTableDto> getTableById(@PathVariable String tableId) throws NotFoundException {
        return workspaceTableService.getTableById(tableId);
    }

}
