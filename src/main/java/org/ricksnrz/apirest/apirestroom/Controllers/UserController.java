package org.ricksnrz.apirest.apirestroom.Controllers;


import org.ricksnrz.apirest.apirestroom.Entities.UserEntity;
import org.ricksnrz.apirest.apirestroom.Services.IUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserServiceImpl userService;

    @GetMapping("/find-all")
    private ResponseEntity<List<UserEntity>> findAllUsers() {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }
}
