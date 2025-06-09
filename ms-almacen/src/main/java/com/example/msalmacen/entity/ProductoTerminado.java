package com.example.msalmacen.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "producto_terminado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoTerminado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual ;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo ;

    @Column(nullable = false)
    private Boolean activo = true;
}
