package org.ricksnrz.apirest.apirestroom.Configuration;


import org.ricksnrz.apirest.apirestroom.Services.models.validations.UserValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationsConfig {

    @Bean
    public UserValidation userValidation() {
        return new UserValidation();
    }

}
