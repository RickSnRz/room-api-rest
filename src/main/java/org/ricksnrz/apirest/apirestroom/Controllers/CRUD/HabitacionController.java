package org.ricksnrz.apirest.apirestroom.Controllers.CRUD;


import org.ricksnrz.apirest.apirestroom.Entities.Habitacion;
import org.ricksnrz.apirest.apirestroom.Repositories.HabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habitaciones")
public class HabitacionController {

    @Autowired
    private HabitacionRepository habitacionRepository;

    @GetMapping
    public List<Habitacion> listarHabitaciones(){
        return habitacionRepository.findAll();
    }
    @GetMapping("/{id}")
    public Habitacion obtenerHabitacion(@PathVariable Long id){
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habitacion no encontrada con el id: " + id));
    }
    @PostMapping
    public Habitacion agregarHabitacion(@RequestBody Habitacion habitacion){
        return habitacionRepository.save(habitacion);
    }
    @PutMapping("/{id}")
    public Habitacion actualizarHabitacion(@PathVariable Long id, @RequestBody Habitacion habitacion) {
        Habitacion existente = habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habitacion no encontrada"));
        existente.setNumero(habitacion.getNumero());
        existente.setPiso(habitacion.getPiso());
        existente.setPrecio(habitacion.getPrecio());
        existente.setEstado(habitacion.getEstado());
        return habitacionRepository.save(existente);
    }
    @DeleteMapping("/{id}")
    public String eliminarHabitacion(@PathVariable Long id){
        Habitacion habitacion = habitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Habitacion no encontrada con el id: " + id));
        habitacionRepository.delete(habitacion);
        return "La habitacion con el id: " + id + " ha sido eliminada.";
    }
}
