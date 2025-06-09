package com.example.msalmacen.dto;


import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleIngresoDTO {

    private Long id;

    @NotNull(message = "El ID del ingreso es obligatorio")
    private Long idIngreso;

    @NotNull(message = "El ID de la materia prima es obligatorio")
    private Long idMateriaPrima;

    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor a 0")
    private BigDecimal cantidad;

    @NotNull(message = "El costo unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El costo unitario debe ser mayor a 0")
    private BigDecimal costoUnitario;

    // Campos adicionales para la vista (no persistidos)
    private String nombreMateriaPrima;
    private String codigoMateriaPrima;
    private String unidadMateriaPrima;
    private BigDecimal costoTotal;

    // Constructor sin campos de vista
    public DetalleIngresoDTO(Long id, Long idIngreso, Long idMateriaPrima,
                             BigDecimal cantidad, BigDecimal costoUnitario) {
        this.id = id;
        this.idIngreso = idIngreso;
        this.idMateriaPrima = idMateriaPrima;
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

    // Getters y Setters explícitos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdIngreso() {
        return idIngreso;
    }

    public void setIdIngreso(Long idIngreso) {
        this.idIngreso = idIngreso;
    }

    public Long getIdMateriaPrima() {
        return idMateriaPrima;
    }

    public void setIdMateriaPrima(Long idMateriaPrima) {
        this.idMateriaPrima = idMateriaPrima;
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

    public String getNombreMateriaPrima() {
        return nombreMateriaPrima;
    }

    public void setNombreMateriaPrima(String nombreMateriaPrima) {
        this.nombreMateriaPrima = nombreMateriaPrima;
    }

    public String getCodigoMateriaPrima() {
        return codigoMateriaPrima;
    }

    public void setCodigoMateriaPrima(String codigoMateriaPrima) {
        this.codigoMateriaPrima = codigoMateriaPrima;
    }

    public String getUnidadMateriaPrima() {
        return unidadMateriaPrima;
    }

    public void setUnidadMateriaPrima(String unidadMateriaPrima) {
        this.unidadMateriaPrima = unidadMateriaPrima;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }
}