package com.example.msalmacen.dto;

import lombok.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleIngresoDTO {

    private Long id;

    @NotNull(message = "El ID del ingreso es obligatorio")
    private Long ingresoMateriaPrimaId;

    @NotNull(message = "El ID de la materia prima es obligatorio")
    private Long materiaPrimaId;

    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.0", inclusive = false, message = "La cantidad debe ser mayor a 0")
    private BigDecimal cantidad;

    @NotNull(message = "El costo unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo unitario debe ser mayor a 0")
    private BigDecimal costoUnitario;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Información adicional para mostrar en responses
    private IngresoMateriaPrimaDTO ingresoMateriaPrima;
    private MateriaPrimaDTO materiaPrima;

    // Constructor para creación (sin ID)
    public DetalleIngresoDTO(Long ingresoMateriaPrimaId, Long materiaPrimaId,
                             BigDecimal cantidad, BigDecimal costoUnitario) {
        this.ingresoMateriaPrimaId = ingresoMateriaPrimaId;
        this.materiaPrimaId = materiaPrimaId;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
    }

    // Método para calcular el costo total
    public BigDecimal getCostoTotal() {
        if (cantidad != null && costoUnitario != null) {
            return cantidad.multiply(costoUnitario);
        }
        return BigDecimal.ZERO;
    }

    // Getters y Setters explícitos (complementando Lombok)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIngresoMateriaPrimaId() {
        return ingresoMateriaPrimaId;
    }

    public void setIngresoMateriaPrimaId(Long ingresoMateriaPrimaId) {
        this.ingresoMateriaPrimaId = ingresoMateriaPrimaId;
    }

    public Long getMateriaPrimaId() {
        return materiaPrimaId;
    }

    public void setMateriaPrimaId(Long materiaPrimaId) {
        this.materiaPrimaId = materiaPrimaId;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
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

    public IngresoMateriaPrimaDTO getIngresoMateriaPrima() {
        return ingresoMateriaPrima;
    }

    public void setIngresoMateriaPrima(IngresoMateriaPrimaDTO ingresoMateriaPrima) {
        this.ingresoMateriaPrima = ingresoMateriaPrima;
    }

    public MateriaPrimaDTO getMateriaPrima() {
        return materiaPrima;
    }

    public void setMateriaPrima(MateriaPrimaDTO materiaPrima) {
        this.materiaPrima = materiaPrima;
    }
}