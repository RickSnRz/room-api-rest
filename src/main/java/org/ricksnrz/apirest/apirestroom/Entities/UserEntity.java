package org.ricksnrz.apirest.apirestroom.Entities;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Configuration;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    private String email;
    private String password;
}
