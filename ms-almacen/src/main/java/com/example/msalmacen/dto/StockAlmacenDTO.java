package com.example.msalmacen.dto;


import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@Builder
public class StockAlmacenDTO {

    private Long id;

    @NotNull(message = "El ID de la materia prima es obligatorio")
    private Long idMateriaPrima;

    @NotNull(message = "El ID del almacén es obligatorio")
    private Long idAlmacen;

    @NotNull(message = "El stock actual es obligatorio")
    @DecimalMin(value = "0.0", message = "El stock actual no puede ser negativo")
    private BigDecimal stockActual;

    private LocalDateTime ultimaActualizacion;

    // Campos adicionales para la vista (no persistidos)
    private String codigoMateriaPrima;
    private String nombreMateriaPrima;
    private String nombreAlmacen;
    private String ubicacionAlmacen;
    private String unidadMedida;
    private Integer stockMinimoMateriaPrima;

    // Constructor sin campos de vista
    public StockAlmacenDTO(Long id, Long idMateriaPrima, Long idAlmacen,
                           BigDecimal stockActual, LocalDateTime ultimaActualizacion) {
        this.id = id;
        this.idMateriaPrima = idMateriaPrima;
        this.idAlmacen = idAlmacen;
        this.stockActual = stockActual;
        this.ultimaActualizacion = ultimaActualizacion;
    }

    // Constructor completo con información adicional
    public StockAlmacenDTO(Long id, Long idMateriaPrima, Long idAlmacen,
                           BigDecimal stockActual, LocalDateTime ultimaActualizacion,
                           String codigoMateriaPrima, String nombreMateriaPrima,
                           String nombreAlmacen, String ubicacionAlmacen,
                           String unidadMedida, Integer stockMinimoMateriaPrima) {
        this.id = id;
        this.idMateriaPrima = idMateriaPrima;
        this.idAlmacen = idAlmacen;
        this.stockActual = stockActual;
        this.ultimaActualizacion = ultimaActualizacion;
        this.codigoMateriaPrima = codigoMateriaPrima;
        this.nombreMateriaPrima = nombreMateriaPrima;
        this.nombreAlmacen = nombreAlmacen;
        this.ubicacionAlmacen = ubicacionAlmacen;
        this.unidadMedida = unidadMedida;
        this.stockMinimoMateriaPrima = stockMinimoMateriaPrima;
    }

    // Métodos auxiliares
    public boolean esBajoStock() {
        if (stockMinimoMateriaPrima != null && stockActual != null) {
            return stockActual.compareTo(BigDecimal.valueOf(stockMinimoMateriaPrima)) < 0;
        }
        return false;
    }

    public boolean tieneStock() {
        return stockActual != null && stockActual.compareTo(BigDecimal.ZERO) > 0;
    }

    // Getters y Setters explícitos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdMateriaPrima() {
        return idMateriaPrima;
    }

    public void setIdMateriaPrima(Long idMateriaPrima) {
        this.idMateriaPrima = idMateriaPrima;
    }

    public Long getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(Long idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public BigDecimal getStockActual() {
        return stockActual;
    }

    public void setStockActual(BigDecimal stockActual) {
        this.stockActual = stockActual;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }

    public String getCodigoMateriaPrima() {
        return codigoMateriaPrima;
    }

    public void setCodigoMateriaPrima(String codigoMateriaPrima) {
        this.codigoMateriaPrima = codigoMateriaPrima;
    }

    public String getNombreMateriaPrima() {
        return nombreMateriaPrima;
    }

    public void setNombreMateriaPrima(String nombreMateriaPrima) {
        this.nombreMateriaPrima = nombreMateriaPrima;
    }

    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
    }

    public String getUbicacionAlmacen() {
        return ubicacionAlmacen;
    }

    public void setUbicacionAlmacen(String ubicacionAlmacen) {
        this.ubicacionAlmacen = ubicacionAlmacen;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Integer getStockMinimoMateriaPrima() {
        return stockMinimoMateriaPrima;
    }

    public void setStockMinimoMateriaPrima(Integer stockMinimoMateriaPrima) {
        this.stockMinimoMateriaPrima = stockMinimoMateriaPrima;
    }
}