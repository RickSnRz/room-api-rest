package org.ricksnrz.apirest.apirestroom.Services.models.validations;

import org.ricksnrz.apirest.apirestroom.Entities.UserEntity;
import org.ricksnrz.apirest.apirestroom.Services.models.dtos.ResponseDTO;

public class UserValidation {

    public ResponseDTO validate(UserEntity user){
        ResponseDTO response = new ResponseDTO();

        response.setNumofErrors(0);
        if(user.getFirstName() == null ||
           user.getFirstName().length() < 3 ||
           user.getFirstName().length() > 15){
            response.setNumofErrors(response.getNumofErrors() + 1);
            response.setMessage("El campo nombre no puede ser nulo y debe tener entre 3 y 15 caracteres");
        }
        if (user.getLastName() == null ||
            user.getLastName().length() < 3 ||
            user.getLastName().length() > 30){
            response.setNumofErrors(response.getNumofErrors() + 1);
            response.setMessage("El campo apellido no puede ser nulo y debe tener entre 3 y 15 caracteres");
        }
        if (user.getEmail() == null ||
                !user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            response.setNumofErrors(response.getNumofErrors() + 1);
            response.setMessage("El campo email no puede ser nulo y debe tener un formato de correo valido");
        }
        if (user.getPassword() == null ||
                !user.getPassword().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,16}$")){
            response.setNumofErrors(response.getNumofErrors() + 1);
            response.setMessage("El campo password no puede ser nulo y debe tener entre 8 y 16 caracteres, al menos una letra mayuscula, una letra minuscula y un numero");
        }

        return response;
    }

}
