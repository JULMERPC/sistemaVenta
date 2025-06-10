package com.example.ms.venta.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
