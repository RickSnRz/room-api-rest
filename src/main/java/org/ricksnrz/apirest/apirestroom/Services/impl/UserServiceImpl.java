package org.ricksnrz.apirest.apirestroom.Services.impl;

import org.ricksnrz.apirest.apirestroom.Entities.UserEntity;
import org.ricksnrz.apirest.apirestroom.Repositories.UserRepository;
import org.ricksnrz.apirest.apirestroom.Services.IUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserServiceImpl {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }
}
