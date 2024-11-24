package org.ricksnrz.apirest.apirestroom.Services.CRUD;

import org.ricksnrz.apirest.apirestroom.Entities.Inquilino;
import org.ricksnrz.apirest.apirestroom.Repositories.InquilinoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InquilinoService {

    private final InquilinoRepository inquilinoRepository;

    public InquilinoService(InquilinoRepository inquilinoRepository) {
        this.inquilinoRepository = inquilinoRepository;
    }

    public List<Inquilino> obtenerTodos() {
        return inquilinoRepository.findAll();
    }

    public Optional<Inquilino> obtenerPorId(Long id) {
        return inquilinoRepository.findById(id);
    }

    public Inquilino guardar(Inquilino inquilino) {
        return inquilinoRepository.save(inquilino);
    }

    public void eliminar(Long id) {
        if (inquilinoRepository.existsById(id)) {
            inquilinoRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Inquilino con ID " + id + " no encontrado.");
        }
    }
}
