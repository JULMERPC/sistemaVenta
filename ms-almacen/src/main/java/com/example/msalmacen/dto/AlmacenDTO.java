package com.example.msalmacen.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlmacenDTO {

    private Long id;

    @NotBlank(message = "El nombre del almacén es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    private String ubicacion;

    @Size(max = 50, message = "El tipo no puede exceder 50 caracteres")
    private String tipo; // tienda, taller, depósito, etc.

    private Boolean estado; // activo/inactivo

    private LocalDateTime fechaRegistro;


}