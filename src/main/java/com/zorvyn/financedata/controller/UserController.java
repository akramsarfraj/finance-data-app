package com.zorvyn.financedata.controller;

import com.zorvyn.financedata.dto.AdminDto;
import com.zorvyn.financedata.dto.ResponseBody;
import com.zorvyn.financedata.dto.UserDto;
import com.zorvyn.financedata.model.User;
import com.zorvyn.financedata.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.AbstractDocument;

@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<ResponseBody> loginUser(@RequestParam String email,
                                                        @RequestParam String password){
        return userService.login(email,password);
    }



    @PostMapping("/create-admin")
    public ResponseEntity<ResponseBody<User>> createAdminUser(@Valid @RequestBody AdminDto dto){
        return userService.createAdmin(dto);
    }

    @PostMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<User>> createNewUser(@Valid @RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }

    @PutMapping("/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBody<User>> updateUser(@Valid @RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }

    @GetMapping("/users/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUser(@RequestParam(defaultValue = "10") int pageSize,
                                        @RequestParam(defaultValue = "0") int pageNumber,
                                        @RequestParam(required = false) Boolean isDelete,
                                        @RequestParam(required = false) Boolean isActive){


        return userService.getAllUsers(pageSize,pageNumber,isDelete,isActive);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable(name = "id") Long id){
        return userService.getUserById(id);
    }

    @PutMapping("/user/status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserStatus(@PathVariable(name = "id") Long id,
                                              @RequestParam boolean isActive){
        return userService.updateUserActiveStatus(isActive,id);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> softDeleteUser(@RequestParam Long id){
        return userService.softDeleteUser(id);
    }

    @DeleteMapping("/user/permanent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserPermanent(@RequestParam Long id){
        return userService.deleteUserPermanently(id);
    }

    @PostMapping("/user/recover")
    public ResponseEntity<?> recoverUser(@RequestParam String email){
        return userService.recoverDeleteUser(email);
    }
}
