package com.tsofnsalesforce.LoginandRegistration.controller;

import com.tsofnsalesforce.LoginandRegistration.request.AddPermissionRequest;
import com.tsofnsalesforce.LoginandRegistration.request.DeletePermissionRequest;
import com.tsofnsalesforce.LoginandRegistration.service.AdminService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/add-permission")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> updatePermission(@RequestBody @Valid AddPermissionRequest request) throws MessagingException {
        adminService.AddPermission(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/delete-permission")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> deletePermission(@RequestBody @Valid DeletePermissionRequest request) throws MessagingException {
        adminService.deletePermission(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> userroles(){
        adminService.printRoles();
        return ResponseEntity.ok().build();
    }
}
