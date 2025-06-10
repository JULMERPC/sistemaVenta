//package com.example.msalmacen.entity;
//
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "stock_almacen",
//        uniqueConstraints = @UniqueConstraint(columnNames = {"id_materia_prima", "id_almacen"}))
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//public class StockAlmacen {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_materia_prima", nullable = false)
//    private MateriaPrima materiaPrima;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_almacen", nullable = false)
//    private Almacen almacen;
//
//    @Column(name = "stock_actual", nullable = false, precision = 10, scale = 2)
//    private BigDecimal stockActual = BigDecimal.ZERO;
//
//    @Column(name = "ultima_actualizacion")
//    private LocalDateTime ultimaActualizacion;
//
//    @PrePersist
//    @PreUpdate
//    public void actualizarTimestamp() {
//        this.ultimaActualizacion = LocalDateTime.now();
//    }
//
//    // Constructor de conveniencia
//    public StockAlmacen(MateriaPrima materiaPrima, Almacen almacen, BigDecimal stockActual) {
//        this.materiaPrima = materiaPrima;
//        this.almacen = almacen;
//        this.stockActual = stockActual != null ? stockActual : BigDecimal.ZERO;
//        this.ultimaActualizacion = LocalDateTime.now();
//    }
//
//    // Métodos de utilidad para operaciones de stock
//    public void incrementarStock(BigDecimal cantidad) {
//        if (cantidad != null && cantidad.compareTo(BigDecimal.ZERO) > 0) {
//            this.stockActual = this.stockActual.add(cantidad);
//            this.ultimaActualizacion = LocalDateTime.now();
//        }
//    }
//
//    public void decrementarStock(BigDecimal cantidad) {
//        if (cantidad != null && cantidad.compareTo(BigDecimal.ZERO) > 0) {
//            BigDecimal nuevoStock = this.stockActual.subtract(cantidad);
//            if (nuevoStock.compareTo(BigDecimal.ZERO) >= 0) {
//                this.stockActual = nuevoStock;
//                this.ultimaActualizacion = LocalDateTime.now();
//            } else {
//                throw new IllegalArgumentException("Stock insuficiente. Stock actual: " + this.stockActual + ", cantidad solicitada: " + cantidad);
//            }
//        }
//    }
//
//    public boolean tieneStockSuficiente(BigDecimal cantidadRequerida) {
//        return cantidadRequerida != null &&
//                this.stockActual.compareTo(cantidadRequerida) >= 0;
//    }
//
//    // Getters y Setters explícitos (complementando Lombok)
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public MateriaPrima getMateriaPrima() {
//        return materiaPrima;
//    }
//
//    public void setMateriaPrima(MateriaPrima materiaPrima) {
//        this.materiaPrima = materiaPrima;
//    }
//
//    public Almacen getAlmacen() {
//        return almacen;
//    }
//
//    public void setAlmacen(Almacen almacen) {
//        this.almacen = almacen;
//    }
//
//    public BigDecimal getStockActual() {
//        return stockActual;
//    }
//
//    public void setStockActual(BigDecimal stockActual) {
//        this.stockActual = stockActual != null ? stockActual : BigDecimal.ZERO;
//        this.ultimaActualizacion = LocalDateTime.now();
//    }
//
//    public LocalDateTime getUltimaActualizacion() {
//        return ultimaActualizacion;
//    }
//
//    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
//        this.ultimaActualizacion = ultimaActualizacion;
//    }
//}

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