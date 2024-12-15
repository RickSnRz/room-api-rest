package org.ricksnrz.apirest.apirestroom.Controllers;

import org.ricksnrz.apirest.apirestroom.Entities.UserEntity;
import org.ricksnrz.apirest.apirestroom.Services.IAuthServiceImpl;
import org.ricksnrz.apirest.apirestroom.Services.models.dtos.LoginDTO;
import org.ricksnrz.apirest.apirestroom.Services.models.dtos.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    IAuthServiceImpl authService;

    @PostMapping("/register")
    private ResponseEntity<ResponseDTO> register(@RequestBody UserEntity user) throws Exception {
        return new ResponseEntity<>(authService.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    private ResponseEntity<HashMap<String, String> > login(@RequestBody LoginDTO loginResquest) throws Exception {
        HashMap<String, String> login = authService.login(loginResquest);
         if(login.containsKey("jwt")){
                return new ResponseEntity<>(login, HttpStatus.OK);
         } else {
                return new ResponseEntity<>(login, HttpStatus.UNAUTHORIZED);
         }
    }
}
