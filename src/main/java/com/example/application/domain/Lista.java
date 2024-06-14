package com.example.application.domain;

import com.example.application.enums.EstadoLista;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "listas")
public class Lista {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @NotBlank
    @Column(name = "nombre", nullable = false, length = 64)
    private String nombre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 16)
    private EstadoLista estado;

    @NotNull
    @Column(name = "coste", nullable = false)
    private Double coste = 0.0;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "lista")
    private List<Regalo> regalos;
}
