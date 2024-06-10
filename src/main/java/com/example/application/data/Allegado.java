package com.example.application.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "allegados")
public class Allegado {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @NotBlank
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
