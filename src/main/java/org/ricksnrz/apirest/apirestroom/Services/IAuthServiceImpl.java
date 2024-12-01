package org.ricksnrz.apirest.apirestroom.Services;

import org.ricksnrz.apirest.apirestroom.Entities.UserEntity;
import org.ricksnrz.apirest.apirestroom.Services.models.dtos.LoginDTO;
import org.ricksnrz.apirest.apirestroom.Services.models.dtos.ResponseDTO;

import java.util.HashMap;


public interface IAuthServiceImpl {
    public HashMap<String, String> login(LoginDTO loginDTO) throws Exception;
    public ResponseDTO register(UserEntity user) throws Exception;
}
