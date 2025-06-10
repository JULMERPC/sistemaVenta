package com.example.msalmacen.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
//@NoArgsConstructor
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

    public AlmacenDTO(Long id, LocalDateTime fechaRegistro, Boolean estado, String tipo, String ubicacion, String nombre) {
        this.id = id;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
        this.tipo = tipo;
        this.ubicacion = ubicacion;
        this.nombre = nombre;
    }

    public AlmacenDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}