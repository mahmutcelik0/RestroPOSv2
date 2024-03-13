package com.restropos.systemshop.api;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.WrongCredentialsException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.dto.SystemUserDto;
import com.restropos.systemshop.dto.SystemUserDtoResponse;
import com.restropos.systemshop.facade.UserFacade;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staffs")
public class StaffApi {
    @Autowired
    private UserFacade userFacade;

    @GetMapping("/getByType")
    public List<SystemUserDtoResponse> getStaffByRole(@RequestParam String userType) {
        return userFacade.getStaffByRole(UserTypes.valueOf(userType));
    }

    @GetMapping
    public List<SystemUserDtoResponse> getAllStaffsExceptAdmin(){
        return userFacade.getAllStaffsExceptAdmin();
    }

    @PostMapping
    public ResponseEntity<SystemUserDtoResponse> addNewStaff(@RequestBody SystemUserDto systemUserDto) throws NotFoundException {
        return userFacade.addNewStaff(systemUserDto);
    }

    @PutMapping("/{email}")
    public ResponseEntity<SystemUserDtoResponse> updateStaff(@RequestBody SystemUserDto systemUserDto,@PathVariable @Email String email) throws NotFoundException {
        return userFacade.updateStaff(systemUserDto,email);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<ResponseMessage> deleteStaff(@PathVariable String email) {
        return userFacade.deleteStaff(email);
    }

}