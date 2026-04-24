package org.ricksnrz.apirest.apirestroom.Controllers.CRUD;


import org.ricksnrz.apirest.apirestroom.Entities.Alquiler;
import org.ricksnrz.apirest.apirestroom.Services.CRUD.AlquilerService;
import org.ricksnrz.apirest.apirestroom.Services.models.dtos.AlquileresDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;

    @GetMapping
    public List<Alquiler> obtenerTodas() {
        return alquilerService.obtenerTodas();
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearAlquiler(@RequestBody AlquileresDTO dto) {
        try {
            Alquiler nuevo = alquilerService.crearAlquiler(dto);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/finalizar/{id}")
    public ResponseEntity<?> finalizarAlquiler(@PathVariable Long id, @RequestParam String fechaSalida) {
        try {
            LocalDate fecha = LocalDate.parse(fechaSalida);
            Alquiler alquilerFinalizado = alquilerService.finalizarAlquiler(id, fecha);
            return ResponseEntity.ok(alquilerFinalizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
