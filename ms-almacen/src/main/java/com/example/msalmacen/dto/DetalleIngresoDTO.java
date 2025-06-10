//package com.example.msalmacen.dto;
//
//
//import lombok.*;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.DecimalMin;
//import java.math.BigDecimal;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class DetalleIngresoDTO {
//
//    private Long id;
//
//    @NotNull(message = "El ID del ingreso es obligatorio")
//    private Long idIngreso;
//
//    @NotNull(message = "El ID de la materia prima es obligatorio")
//    private Long idMateriaPrima;
//
//    @NotNull(message = "La cantidad es obligatoria")
//    @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor a 0")
//    private BigDecimal cantidad;
//
//    @NotNull(message = "El costo unitario es obligatorio")
//    @DecimalMin(value = "0.01", message = "El costo unitario debe ser mayor a 0")
//    private BigDecimal costoUnitario;
//
//    // Campos adicionales para la vista (no persistidos)
//    private String nombreMateriaPrima;
//    private String codigoMateriaPrima;
//    private String unidadMateriaPrima;
//    private BigDecimal costoTotal;
//
//    // Constructor sin campos de vista
//    public DetalleIngresoDTO(Long id, Long idIngreso, Long idMateriaPrima,
//                             BigDecimal cantidad, BigDecimal costoUnitario) {
//        this.id = id;
//        this.idIngreso = idIngreso;
//        this.idMateriaPrima = idMateriaPrima;
//        this.cantidad = cantidad;
//        this.costoUnitario = costoUnitario;
//    }
//
//    // Método para calcular el costo total
//    public BigDecimal getCostoTotal() {
//        if (cantidad != null && costoUnitario != null) {
//            return cantidad.multiply(costoUnitario);
//        }
//        return BigDecimal.ZERO;
//    }
//
//    // Getters y Setters explícitos
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getIdIngreso() {
//        return idIngreso;
//    }
//
//    public void setIdIngreso(Long idIngreso) {
//        this.idIngreso = idIngreso;
//    }
//
//    public Long getIdMateriaPrima() {
//        return idMateriaPrima;
//    }
//
//    public void setIdMateriaPrima(Long idMateriaPrima) {
//        this.idMateriaPrima = idMateriaPrima;
//    }
//
//    public BigDecimal getCantidad() {
//        return cantidad;
//    }
//
//    public void setCantidad(BigDecimal cantidad) {
//        this.cantidad = cantidad;
//    }
//
//    public BigDecimal getCostoUnitario() {
//        return costoUnitario;
//    }
//
//    public void setCostoUnitario(BigDecimal costoUnitario) {
//        this.costoUnitario = costoUnitario;
//    }
//
//    public String getNombreMateriaPrima() {
//        return nombreMateriaPrima;
//    }
//
//    public void setNombreMateriaPrima(String nombreMateriaPrima) {
//        this.nombreMateriaPrima = nombreMateriaPrima;
//    }
//
//    public String getCodigoMateriaPrima() {
//        return codigoMateriaPrima;
//    }
//
//    public void setCodigoMateriaPrima(String codigoMateriaPrima) {
//        this.codigoMateriaPrima = codigoMateriaPrima;
//    }
//
//    public String getUnidadMateriaPrima() {
//        return unidadMateriaPrima;
//    }
//
//    public void setUnidadMateriaPrima(String unidadMateriaPrima) {
//        this.unidadMateriaPrima = unidadMateriaPrima;
//    }
//
//    public void setCostoTotal(BigDecimal costoTotal) {
//        this.costoTotal = costoTotal;
//    }
//}

package com.example.msalmacen.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "La cantidad debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal cantidad;

    @NotNull(message = "El costo unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El costo unitario debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El costo unitario debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal costoUnitario;

    // Campos calculados
    private BigDecimal costoTotal;

    // Información de la materia prima (para consultas)
    private String nombreMateriaPrima;
    private String descripcionMateriaPrima;
    private String unidadMedida;
    private Boolean estadoMateriaPrima;

    // Información del ingreso (para consultas)
    private LocalDate fechaIngreso;
    private String proveedorIngreso;
    private String numeroDocumento;

    // Constructor para crear DTO básico
    public DetalleIngresoDTO(Long ingresoMateriaPrimaId, Long materiaPrimaId,
                             BigDecimal cantidad, BigDecimal costoUnitario) {
        this.ingresoMateriaPrimaId = ingresoMateriaPrimaId;
        this.materiaPrimaId = materiaPrimaId;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.costoTotal = calcularCostoTotal();
    }

    // Método para calcular el costo total
    public BigDecimal calcularCostoTotal() {
        if (cantidad != null && costoUnitario != null) {
            return cantidad.multiply(costoUnitario);
        }
        return BigDecimal.ZERO;
    }

    // Método para obtener el costo total (getter personalizado)
    public BigDecimal getCostoTotal() {
        if (costoTotal == null) {
            costoTotal = calcularCostoTotal();
        }
        return costoTotal;
    }

    // Método para validar que los datos sean válidos
    public boolean isValid() {
        return ingresoMateriaPrimaId != null &&
                materiaPrimaId != null &&
                cantidad != null && cantidad.compareTo(BigDecimal.ZERO) > 0 &&
                costoUnitario != null && costoUnitario.compareTo(BigDecimal.ZERO) > 0;
    }

    public void setCostoTotal(BigDecimal costoTotal) {
        this.costoTotal = costoTotal;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    // Getters y Setters explícitos
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
        this.costoTotal = null; // Reset para recalcular
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
        this.costoTotal = null; // Reset para recalcular
    }

    public String getNombreMateriaPrima() {
        return nombreMateriaPrima;
    }

    public void setNombreMateriaPrima(String nombreMateriaPrima) {
        this.nombreMateriaPrima = nombreMateriaPrima;
    }

    public String getDescripcionMateriaPrima() {
        return descripcionMateriaPrima;
    }

    public void setDescripcionMateriaPrima(String descripcionMateriaPrima) {
        this.descripcionMateriaPrima = descripcionMateriaPrima;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Boolean getEstadoMateriaPrima() {
        return estadoMateriaPrima;
    }

    public void setEstadoMateriaPrima(Boolean estadoMateriaPrima) {
        this.estadoMateriaPrima = estadoMateriaPrima;
    }



    public String getProveedorIngreso() {
        return proveedorIngreso;
    }

    public void setProveedorIngreso(String proveedorIngreso) {
        this.proveedorIngreso = proveedorIngreso;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
}