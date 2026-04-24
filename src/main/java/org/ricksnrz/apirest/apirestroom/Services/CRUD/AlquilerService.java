package org.ricksnrz.apirest.apirestroom.Services.CRUD;


import org.ricksnrz.apirest.apirestroom.Entities.Alquiler;
import org.ricksnrz.apirest.apirestroom.Entities.Habitacion;
import org.ricksnrz.apirest.apirestroom.Entities.Inquilino;
import org.ricksnrz.apirest.apirestroom.Repositories.AlquilerRepository;
import org.ricksnrz.apirest.apirestroom.Repositories.HabitacionRepository;
import org.ricksnrz.apirest.apirestroom.Repositories.InquilinoRepository;
import org.ricksnrz.apirest.apirestroom.Services.models.dtos.AlquileresDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AlquilerService {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private HabitacionRepository habitacionRepository;

    @Autowired
    private InquilinoRepository inquilinoRepository;

    public List<Alquiler> obtenerTodas() {
        return alquilerRepository.findAll();
    }

    public Alquiler crearAlquiler(AlquileresDTO dto) throws Exception {

        // 1. Buscar inquilino por DNI
        Inquilino inquilino = inquilinoRepository.findByDni(dto.getDni())
                .orElseThrow(() -> new Exception("Inquilino no encontrado"));

        // 2. Buscar habitación por número
        Habitacion habitacion = habitacionRepository.findByNumero(dto.getNumeroHabitacion())
                .orElseThrow(() -> new Exception("Habitación no encontrada"));

        // 3. Validar disponibilidad
        if (habitacion.getEstado().equalsIgnoreCase("Ocupado")) {
            throw new Exception("La habitación ya está ocupada");
        }

        // 4. Cambiar estado habitación
        habitacion.setEstado("Ocupado");
        habitacionRepository.save(habitacion);

        // 5. Crear entidad alquiler real
        Alquiler alquiler = new Alquiler();
        alquiler.setInquilino(inquilino);
        alquiler.setHabitacion(habitacion);
        alquiler.setFechaEntrada(dto.getFechaEntrada());

        // 6. Guardar alquiler
        return alquilerRepository.save(alquiler);
    }

    public Alquiler finalizarAlquiler(Long id, LocalDate fechaSalida) throws Exception {
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(id);
        if (alquilerOpt.isPresent()) {
            Alquiler alquiler = alquilerOpt.get();
            alquiler.setFechaSalida(fechaSalida);

            // Liberar la habitación
            Habitacion habitacion = alquiler.getHabitacion();
            habitacion.setEstado("Disponible");
            habitacionRepository.save(habitacion);

            return alquilerRepository.save(alquiler);
        } else {
            throw new Exception("Alquiler no encontrado.");
        }
    }
}
