package com.example.msalmacen.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data


@Builder

@AllArgsConstructor
@NoArgsConstructor
public class MateriaPrimaDTO {
    private String codigo;
    private String nombre;
    private String descripcion;
    private String tipoTela;
    private String color;
    private Double anchoCm;
    private Double pesoGsm;
    private BigDecimal precioMetro;
    private Integer stockActual;
    private Integer stockMinimo;
    private String unidad;
    private Boolean activo;
    private LocalDateTime fechaRegistro;



    public MateriaPrimaDTO(String codigo, String nombre, String descripcion, String tipoTela, String color, Double anchoCm, Double pesoGsm, BigDecimal precioMetro, Integer stockActual, Integer stockMinimo, String unidad, LocalDateTime fechaRegistro, Boolean activo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoTela = tipoTela;
        this.color = color;
        this.anchoCm = anchoCm;
        this.pesoGsm = pesoGsm;
        this.precioMetro = precioMetro;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.unidad = unidad;
        this.fechaRegistro = fechaRegistro;
        this.activo = activo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getPrecioMetro() {
        return precioMetro;
    }

    public void setPrecioMetro(BigDecimal precioMetro) {
        this.precioMetro = precioMetro;
    }

    public Double getPesoGsm() {
        return pesoGsm;
    }

    public void setPesoGsm(Double pesoGsm) {
        this.pesoGsm = pesoGsm;
    }

    public Double getAnchoCm() {
        return anchoCm;
    }

    public void setAnchoCm(Double anchoCm) {
        this.anchoCm = anchoCm;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTipoTela() {
        return tipoTela;
    }

    public void setTipoTela(String tipoTela) {
        this.tipoTela = tipoTela;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }
}