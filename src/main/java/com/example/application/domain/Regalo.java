package com.example.application.domain;

import com.example.application.enums.EstadoRegalo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "regalos")
public class Regalo {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;

    @NotBlank
    @Column(name = "nombre", nullable = false, length = 64)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 32)
    private EstadoRegalo estado;

    @Column(name = "precio", nullable = true)
    private Double precio;

    @Column(name = "url", nullable = true, length = 256)
    private String url;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_allegado")
    private Allegado allegado;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_lista")
    private Lista lista;
}
