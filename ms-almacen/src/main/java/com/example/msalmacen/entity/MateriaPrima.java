package com.example.msalmacen.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "materia_prima")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MateriaPrima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(name = "tipo_tela", length = 50)
    private String tipoTela;

    @Column(length = 50)
    private String color;

    @Column(name = "ancho_cm")
    private Double anchoCm;

    @Column(name = "peso_gsm")
    private Double pesoGsm;

    @Column(name = "precio_metro", precision = 10, scale = 2)
    private BigDecimal precioMetro;

    @Column(name = "stock_actual")
    private Integer stockActual = 0;

    @Column(name = "stock_minimo", precision = 10, scale = 2)
    private Integer stockMinimo = (int) 0.0;

    @Column(length = 10)
    private String unidad = "METROS";

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();



    public MateriaPrima(Long id, String codigo, String nombre, String descripcion, String color, Double pesoGsm, BigDecimal precioMetro, Integer stockMinimo, Boolean activo, LocalDateTime fechaRegistro, String unidad, Integer stockActual, Double anchoCm, String tipoTela) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.color = color;
        this.pesoGsm = pesoGsm;
        this.precioMetro = precioMetro;
        this.stockMinimo = stockMinimo;
        this.activo = activo;
        this.fechaRegistro = fechaRegistro;
        this.unidad = unidad;
        this.stockActual = stockActual;
        this.anchoCm = anchoCm;
        this.tipoTela = tipoTela;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Double getAnchoCm() {
        return anchoCm;
    }

    public void setAnchoCm(Double anchoCm) {
        this.anchoCm = anchoCm;
    }

    public Double getPesoGsm() {
        return pesoGsm;
    }

    public void setPesoGsm(Double pesoGsm) {
        this.pesoGsm = pesoGsm;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public BigDecimal getPrecioMetro() {
        return precioMetro;
    }

    public void setPrecioMetro(BigDecimal precioMetro) {
        this.precioMetro = precioMetro;
    }

    public String getTipoTela() {
        return tipoTela;
    }

    public void setTipoTela(String tipoTela) {
        this.tipoTela = tipoTela;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}