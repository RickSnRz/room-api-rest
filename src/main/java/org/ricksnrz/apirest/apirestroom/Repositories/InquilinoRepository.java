package org.ricksnrz.apirest.apirestroom.Repositories;

import org.ricksnrz.apirest.apirestroom.Entities.Inquilino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InquilinoRepository extends JpaRepository<Inquilino, Long> {

    Optional<Inquilino> findByDni(String dni);

}
