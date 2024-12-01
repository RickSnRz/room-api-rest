package org.ricksnrz.apirest.apirestroom.Services.impl;

import org.ricksnrz.apirest.apirestroom.Entities.UserEntity;
import org.ricksnrz.apirest.apirestroom.Repositories.UserRepository;
import org.ricksnrz.apirest.apirestroom.Services.IAuthServiceImpl;
import org.ricksnrz.apirest.apirestroom.Services.IJWTUtilityService;
import org.ricksnrz.apirest.apirestroom.Services.models.dtos.LoginDTO;
import org.ricksnrz.apirest.apirestroom.Services.models.dtos.ResponseDTO;
import org.ricksnrz.apirest.apirestroom.Services.models.validations.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AuthServiceImpl implements IAuthServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IJWTUtilityService jwtUtilityService;

    @Autowired
    private UserValidation userValidation;

    @Override
    public HashMap<String, String> login(LoginDTO loginDTO) throws Exception {
        try {

            HashMap<String, String> jwt = new HashMap<>();
            Optional<UserEntity> user = userRepository.findByEmail(loginDTO.getEmail());

            if (user.isEmpty()) {
                jwt.put("error", "User not registered");
                return jwt;
            }

            if (verifyPassword(loginDTO.getPassword(), user.get().getPassword())) {
                jwt.put("jwt", jwtUtilityService.generateJWT(user.get().getId()));
            } else {
                jwt.put("error", "Invalid password");
            }

            return jwt;
        }catch (Exception e){
            throw new Exception(e.toString());
        }
    }

    public ResponseDTO register(UserEntity user) throws Exception {
        try {

            ResponseDTO response = userValidation.validate(user);

            if(response.getNumofErrors() > 0){
                return response;
            }

            List<UserEntity> getAllUsers = userRepository.findAll();

            for (UserEntity repetFields : getAllUsers){
                if(repetFields != null){
                    response.setNumofErrors(1);
                    response.setMessage("User already exists");
                    return response;
                }
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
            response.setMessage("User registered successfully");

            return response;

        } catch (Exception e){
            throw  new Exception(e.toString());
        }
    }

    private boolean verifyPassword(String enteredPassword, String storedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword);
    }
}
