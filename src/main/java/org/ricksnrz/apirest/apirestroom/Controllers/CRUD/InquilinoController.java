package org.ricksnrz.apirest.apirestroom.Controllers.CRUD;


import org.ricksnrz.apirest.apirestroom.Entities.Inquilino;
import org.ricksnrz.apirest.apirestroom.Services.CRUD.InquilinoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquilinos")
public class InquilinoController {

    private final InquilinoService inquilinoService;

    public InquilinoController(InquilinoService inquilinoService) {
        this.inquilinoService = inquilinoService;
    }

    @GetMapping
    public List<Inquilino> obtenerTodos() {
        return inquilinoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inquilino> obtenerPorId(@PathVariable Long id) {
        return inquilinoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Inquilino crear(@RequestBody Inquilino inquilino) {
        return inquilinoService.guardar(inquilino);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inquilino> actualizar(@PathVariable Long id, @RequestBody Inquilino inquilino) {
        return inquilinoService.obtenerPorId(id)
                .map(existing -> {
                    inquilino.setId(id); // Asegurar que se actualice el inquilino correcto
                    return ResponseEntity.ok(inquilinoService.guardar(inquilino));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            inquilinoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
