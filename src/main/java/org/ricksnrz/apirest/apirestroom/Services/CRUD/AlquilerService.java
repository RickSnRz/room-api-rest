package org.ricksnrz.apirest.apirestroom.Services.CRUD;


import org.ricksnrz.apirest.apirestroom.Entities.Alquiler;
import org.ricksnrz.apirest.apirestroom.Entities.Habitacion;
import org.ricksnrz.apirest.apirestroom.Repositories.AlquilerRepository;
import org.ricksnrz.apirest.apirestroom.Repositories.HabitacionRepository;
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

    public List<Alquiler> obtenerTodas() {
        return alquilerRepository.findAll();
    }

    public Alquiler crearAlquiler(Alquiler alquiler) throws Exception {
        // Buscar la habitación completa desde la base de datos
        Long habitacionId = alquiler.getHabitacion().getId();
        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new Exception("Habitación no encontrada"));

        // Verificar si la habitación está disponible
        if (!"Ocupada".equalsIgnoreCase(habitacion.getEstado())) {
            // Cambiar el estado a "ocupada"
            habitacion.setEstado("Ocupada");
            habitacionRepository.save(habitacion);
        } else {
            throw new Exception("La habitación ya está ocupada.");
        }

        // Asociar la habitación completa al alquiler
        alquiler.setHabitacion(habitacion);

        // Guardar el alquiler
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
