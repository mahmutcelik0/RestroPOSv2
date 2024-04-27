package com.restropos.systemorder.api;

import com.restropos.systemorder.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/employees")
public class EmployeeController {
    @Autowired
    private FirebaseService firebaseService;


}
