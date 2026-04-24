package org.ricksnrz.apirest.apirestroom.Services.models.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlquileresDTO {

    private String dni;
    private String numeroHabitacion;
    private LocalDate fechaEntrada;

}
