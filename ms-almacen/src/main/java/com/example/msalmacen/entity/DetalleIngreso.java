package com.example.msalmacen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "detalle_ingreso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleIngreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ingreso", nullable = false)
    @NotNull(message = "El ingreso de materia prima es obligatorio")
    private IngresoMateriaPrima ingresoMateriaPrima;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_materia_prima", nullable = false)
    @NotNull(message = "La materia prima es obligatoria")
    private MateriaPrima materiaPrima;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.0", inclusive = false, message = "La cantidad debe ser mayor a 0")
    private BigDecimal cantidad;

    @Column(name = "costo_unitario", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "El costo unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo unitario debe ser mayor a 0")
    private BigDecimal costoUnitario;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Método para calcular el costo total
    public BigDecimal getCostoTotal() {
        if (cantidad != null && costoUnitario != null) {
            return cantidad.multiply(costoUnitario);
        }
        return BigDecimal.ZERO;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters y Setters explícitos (complementando Lombok)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IngresoMateriaPrima getIngresoMateriaPrima() {
        return ingresoMateriaPrima;
    }

    public void setIngresoMateriaPrima(IngresoMateriaPrima ingresoMateriaPrima) {
        this.ingresoMateriaPrima = ingresoMateriaPrima;
    }

    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    public void setMateriaPrima(MateriaPrima materiaPrima) {
        this.materiaPrima = materiaPrima;
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
}