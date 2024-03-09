package com.restropos.systemshop.api;

import com.restropos.systemcore.exception.NotFoundException;
import com.restropos.systemcore.exception.WrongCredentialsException;
import com.restropos.systemcore.model.ResponseMessage;
import com.restropos.systemshop.dto.BasicUserDto;
import com.restropos.systemshop.dto.SystemUserDto;
import com.restropos.systemshop.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staffs")
public class StaffApi {
    @Autowired
    private UserFacade userFacade;

    @GetMapping("/kitchen")
    public List<BasicUserDto> getAllKitchenStaffs() {
        return userFacade.getAllKitchenStaffs();
    }

    @GetMapping("/cashDesk")
    public List<BasicUserDto> getAllCashDeskStaffs() {
        return userFacade.getAllCashDeskStaffs();
    }

    @GetMapping("/admin")
    public List<SystemUserDto> getAllAdminStaffs() {
        return userFacade.getAllAdminStaffs();
    }

    @GetMapping("/waiter")
    public List<SystemUserDto> getAllWaiterStaffs() {
        return userFacade.getAllWaiterStaffs();
    }

    @PostMapping("/kitchen")
    public ResponseEntity<ResponseMessage> addNewKitchenStaffs(@RequestBody BasicUserDto basicUserDto) throws WrongCredentialsException, NotFoundException {
        return userFacade.addNewKitchenAndCashDeskStaffs(basicUserDto);
    }

    @PostMapping("/cashDesk")
    public ResponseEntity<ResponseMessage> addNewCashDeskStaffs(@RequestBody BasicUserDto basicUserDto) throws WrongCredentialsException, NotFoundException {
        return userFacade.addNewKitchenAndCashDeskStaffs(basicUserDto);
    }

    @PostMapping("/admin")
    public ResponseEntity<ResponseMessage> addNewAdminStaffs(@RequestBody SystemUserDto systemUserDto) throws WrongCredentialsException, NotFoundException {
        return userFacade.addNewAdminAndWaiterStaffs(systemUserDto);
    }

    @PostMapping("/waiter")
    public ResponseEntity<ResponseMessage> addNewWaiterStaffs(@RequestBody SystemUserDto systemUserDto) throws WrongCredentialsException, NotFoundException {
        return userFacade.addNewAdminAndWaiterStaffs(systemUserDto);
    }

    @DeleteMapping("/kitchen")
    public ResponseEntity<ResponseMessage> deleteKitchenStaff(@RequestParam String email) {
        return userFacade.deleteKitchenAndCashDeskStaff(email);
    }

    @DeleteMapping("/cashDesk")
    public ResponseEntity<ResponseMessage> deleteCashDeskStaff(@RequestParam String email) {
        return userFacade.deleteKitchenAndCashDeskStaff(email);
    }

    @DeleteMapping("/admin")
    public ResponseEntity<ResponseMessage> deleteAdminStaff(@RequestParam String email) {
        return userFacade.deleteAdminAndWaiterStaff(email);
    }

    @DeleteMapping("/waiter")
    public ResponseEntity<ResponseMessage> deleteWaiterStaff(@RequestParam String email) {
        return userFacade.deleteAdminAndWaiterStaff(email);
    }

}
