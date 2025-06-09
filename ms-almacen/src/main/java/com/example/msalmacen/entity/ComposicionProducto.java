package com.example.msalmacen.entity;

import jakarta.persistence.*;
import lombok.*;


@Table(name = "composicion_producto")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComposicionProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_terminado_id", nullable = false)
    private ProductoTerminado productoTerminado;

    @ManyToOne
    @JoinColumn(name = "materia_prima_id", nullable = false)
    private MateriaPrima materiaPrima;

    @Column(name = "cantidad_necesaria", nullable = false)
    private Double cantidadNecesaria; // en metros
}
