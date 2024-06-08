package com.example.application.data;

import com.example.application.enums.EstadoRegalo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "regalos")
public class Regalo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoRegalo estado;

    @Column(name = "precio", nullable = true)
    private Double precio;

    @Column(name = "url", nullable = true, length = 250)
    private String url;

    @ManyToOne
    @JoinColumn(name = "id_allegado")
    private Allegado allegado;

    @ManyToOne
    @JoinColumn(name = "id_lista")
    private Lista lista;
}
