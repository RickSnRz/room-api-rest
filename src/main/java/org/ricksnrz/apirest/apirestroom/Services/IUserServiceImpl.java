package org.ricksnrz.apirest.apirestroom.Services;

import org.ricksnrz.apirest.apirestroom.Entities.UserEntity;

import java.util.List;

public interface IUserServiceImpl {

    public List<UserEntity> findAllUsers();

}
