package com.example.ms.venta.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
//@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDto {

    private Long id;

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor a 0")
    private BigDecimal total;

    @NotNull(message = "La forma de pago es obligatoria")
    @Size(max = 30, message = "La forma de pago no puede exceder 30 caracteres")
    private String formaPago;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Campos adicionales para información del cliente (opcional)
    private String clienteNombre;
    private String clienteApellido;
    private String clienteDni;

    // Método de utilidad para obtener nombre completo del cliente
    public String getClienteNombreCompleto() {
        if (clienteNombre != null && clienteApellido != null) {
            return clienteNombre + " " + clienteApellido;
        }
        return null;
    }

    public VentaDto(Long id, Long clienteId, LocalDate fecha, String formaPago, BigDecimal total, LocalDateTime createdAt, String clienteDni, String clienteApellido, String clienteNombre, LocalDateTime updatedAt) {
        this.id = id;
        this.clienteId = clienteId;
        this.fecha = fecha;
        this.formaPago = formaPago;
        this.total = total;
        this.createdAt = createdAt;
        this.clienteDni = clienteDni;
        this.clienteApellido = clienteApellido;
        this.clienteNombre = clienteNombre;
        this.updatedAt = updatedAt;
    }

    // Constructor específico para el query simplificado
    public VentaDto(Long id, LocalDate fecha, String formaPago) {
        this.id = id;
        this.fecha = fecha;
        this.formaPago = formaPago;
    }


    public VentaDto() {
    }

    // Getters y Setters explícitos (complementando Lombok)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteApellido() {
        return clienteApellido;
    }

    public void setClienteApellido(String clienteApellido) {
        this.clienteApellido = clienteApellido;
    }

    public String getClienteDni() {
        return clienteDni;
    }

    public void setClienteDni(String clienteDni) {
        this.clienteDni = clienteDni;
    }
}