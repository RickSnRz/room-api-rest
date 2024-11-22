package org.ricksnrz.apirest.apirestroom.Controllers.CRUD;


import org.ricksnrz.apirest.apirestroom.Entities.Inquilino;
import org.ricksnrz.apirest.apirestroom.Repositories.InquilinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inquilinos")
public class InquilinoController {

    @Autowired
    private InquilinoRepository inquilinoRepository;

    @GetMapping
    public List<Inquilino> listarInquilinos(){
        return inquilinoRepository.findAll();
    }
    @GetMapping("/{id}")
    public Inquilino obtenerInquilino(@PathVariable Long id){
        return inquilinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquilino no encontrado con el id: " + id));
    }
    @PostMapping
    public Inquilino agregarInquilino(@RequestBody Inquilino inquilino){
        return inquilinoRepository.save(inquilino);
    }

    @PutMapping("/{id}")
    public Inquilino actualizarInquilino(@PathVariable Long id, @RequestBody Inquilino inquilino) {
        Inquilino existente = inquilinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquilino no encontrado"));
        existente.setNombre(inquilino.getNombre());
        existente.setApellido(inquilino.getApellido());
        existente.setDni(inquilino.getDni());
        existente.setTelefono(inquilino.getTelefono());
        existente.setEmail(inquilino.getEmail());
        return inquilinoRepository.save(existente);
    }

    @DeleteMapping("/{id}")
    public String eliminarInquilino(@PathVariable Long id){
        Inquilino inquilino = inquilinoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquilino no encontrado con el id: " + id));
        inquilinoRepository.delete(inquilino);
        return "El inquilino con el id: " + id + " ha sido eliminado.";
    }
}
