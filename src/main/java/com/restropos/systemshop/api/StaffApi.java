package com.restropos.systemshop.api;

import com.restropos.systemshop.dto.BasicUserDto;
import com.restropos.systemshop.dto.SystemUserDto;
import com.restropos.systemshop.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/staffs")
public class StaffApi {
    @Autowired
    private UserFacade userFacade;

    @GetMapping("/kitchen")
    public List<BasicUserDto> getAllKitchenStaffs(){
        return userFacade.getAllKitchenStaffs();
    }

    @GetMapping("/cashDesk")
    public List<BasicUserDto> getAllCashDeskStaffs(){
        return userFacade.getAllCashDeskStaffs();
    }

    @GetMapping("/admin")
    public List<SystemUserDto> getAllAdminStaffs(){
        return userFacade.getAllAdminStaffs();
    }

    @GetMapping("/waiter")
    public List<SystemUserDto> getAllWaiterStaffs(){
        return userFacade.getAllWaiterStaffs();
    }
}
