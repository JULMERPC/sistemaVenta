
package com.example.msalmacen.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateriaPrimaDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String unidadMedida;
    private BigDecimal stockMinimo;
    private Boolean estado;
    private LocalDateTime fechaRegistro;

    // Constructor para crear DTO sin ID (para creación)
    public MateriaPrimaDTO(String nombre, String descripcion, String unidadMedida,
                           BigDecimal stockMinimo, Boolean estado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.unidadMedida = unidadMedida;
        this.stockMinimo = stockMinimo;
        this.estado = estado;
    }
}