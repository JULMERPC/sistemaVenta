package com.example.ms.venta.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVentaDto {

    private Long id;

    @NotNull(message = "La venta es obligatoria")
    private Long ventaId;

    @NotNull(message = "La materia prima es obligatoria")
    private Long materiaPrimaId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor a 0")
    private BigDecimal precioUnitario;

    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El subtotal debe ser mayor a 0")
    private BigDecimal subtotal;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Campos adicionales para información de la materia prima (opcional)
    private String materiaPrimaNombre;
    private String materiaPrimaDescripcion;
    private String materiaPrimaUnidadMedida;

    // Campos adicionales para información de la venta (opcional)
    private String ventaFecha;
    private String ventaFormaPago;

    // Método de utilidad para calcular subtotal
    public void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }

    // Getters y Setters explícitos (complementando Lombok)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public Long getMateriaPrimaId() {
        return materiaPrimaId;
    }

    public void setMateriaPrimaId(Long materiaPrimaId) {
        this.materiaPrimaId = materiaPrimaId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        // Recalcular subtotal cuando cambia la cantidad
        if (cantidad != null && precioUnitario != null) {
            calcularSubtotal();
        }
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
        // Recalcular subtotal cuando cambia el precio
        if (cantidad != null && precioUnitario != null) {
            calcularSubtotal();
        }
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
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

    public String getMateriaPrimaNombre() {
        return materiaPrimaNombre;
    }

    public void setMateriaPrimaNombre(String materiaPrimaNombre) {
        this.materiaPrimaNombre = materiaPrimaNombre;
    }

    public String getMateriaPrimaDescripcion() {
        return materiaPrimaDescripcion;
    }

    public void setMateriaPrimaDescripcion(String materiaPrimaDescripcion) {
        this.materiaPrimaDescripcion = materiaPrimaDescripcion;
    }

    public String getMateriaPrimaUnidadMedida() {
        return materiaPrimaUnidadMedida;
    }

    public void setMateriaPrimaUnidadMedida(String materiaPrimaUnidadMedida) {
        this.materiaPrimaUnidadMedida = materiaPrimaUnidadMedida;
    }

    public String getVentaFecha() {
        return ventaFecha;
    }

    public void setVentaFecha(String ventaFecha) {
        this.ventaFecha = ventaFecha;
    }

    public String getVentaFormaPago() {
        return ventaFormaPago;
    }

    public void setVentaFormaPago(String ventaFormaPago) {
        this.ventaFormaPago = ventaFormaPago;
    }
}