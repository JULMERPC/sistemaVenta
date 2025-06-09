package com.example.msalmacen.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductoTerminadoDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;

    // Nuevo: stockActual y stockMinimo
    private Integer stockActual;
    private Integer stockMinimo;

    private List<ComposicionDTO> composiciones;
}
