

package com.example.msalmacen.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_almacen",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_materia_prima", "id_almacen"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockAlmacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_materia_prima", nullable = false)
    private MateriaPrima materiaPrima;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_almacen", nullable = false)
    private Almacen almacen;

    @Column(name = "stock_actual", nullable = false, precision = 10, scale = 2)
    private BigDecimal stockActual = BigDecimal.ZERO;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @PrePersist
    @PreUpdate
    public void actualizarTimestamp() {
        this.ultimaActualizacion = LocalDateTime.now();
        if (stockActual == null) {
            stockActual = BigDecimal.ZERO;
        }
    }

    // Constructor para crear nuevo stock sin ID
    public StockAlmacen(MateriaPrima materiaPrima, Almacen almacen, BigDecimal stockActual) {
        this.materiaPrima = materiaPrima;
        this.almacen = almacen;
        this.stockActual = stockActual != null ? stockActual : BigDecimal.ZERO;
    }

    // Métodos de utilidad
    public boolean tieneStock() {
        return stockActual != null && stockActual.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean stockBajo() {
        if (materiaPrima != null && materiaPrima.getStockMinimo() != null) {
            return stockActual.compareTo(materiaPrima.getStockMinimo()) < 0;
        }
        return false;
    }

    // Método para actualizar stock
    public void actualizarStock(BigDecimal nuevoStock) {
        this.stockActual = nuevoStock != null ? nuevoStock : BigDecimal.ZERO;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    // Método para sumar stock
    public void sumarStock(BigDecimal cantidad) {
        if (cantidad != null && cantidad.compareTo(BigDecimal.ZERO) > 0) {
            this.stockActual = this.stockActual.add(cantidad);
            this.ultimaActualizacion = LocalDateTime.now();
        }
    }

    // Método para restar stock
    public void restarStock(BigDecimal cantidad) {
        if (cantidad != null && cantidad.compareTo(BigDecimal.ZERO) > 0) {
            this.stockActual = this.stockActual.subtract(cantidad);
            if (this.stockActual.compareTo(BigDecimal.ZERO) < 0) {
                this.stockActual = BigDecimal.ZERO;
            }
            this.ultimaActualizacion = LocalDateTime.now();
        }
    }
}