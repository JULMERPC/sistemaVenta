package com.example.mscalidad.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspecciones_calidad")
public class InspeccionCalidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoProducto;

    private String inspector;

    private String resultado;

    private String observaciones;

    private LocalDateTime fechaInspeccion;

    private String estado;

    private boolean pregunta1;
    private boolean pregunta2;
    private boolean pregunta3;
    private boolean pregunta4;
    private boolean pregunta5;
    private boolean pregunta6;

    // Constructor vacío para JPA
    public InspeccionCalidad() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaInspeccion() {
        return fechaInspeccion;
    }

    public void setFechaInspeccion(LocalDateTime fechaInspeccion) {
        this.fechaInspeccion = fechaInspeccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isPregunta1() {
        return pregunta1;
    }

    public void setPregunta1(boolean pregunta1) {
        this.pregunta1 = pregunta1;
    }

    public boolean isPregunta2() {
        return pregunta2;
    }

    public void setPregunta2(boolean pregunta2) {
        this.pregunta2 = pregunta2;
    }

    public boolean isPregunta3() {
        return pregunta3;
    }

    public void setPregunta3(boolean pregunta3) {
        this.pregunta3 = pregunta3;
    }

    public boolean isPregunta4() {
        return pregunta4;
    }

    public void setPregunta4(boolean pregunta4) {
        this.pregunta4 = pregunta4;
    }

    public boolean isPregunta5() {
        return pregunta5;
    }

    public void setPregunta5(boolean pregunta5) {
        this.pregunta5 = pregunta5;
    }

    public boolean isPregunta6() {
        return pregunta6;
    }

    public void setPregunta6(boolean pregunta6) {
        this.pregunta6 = pregunta6;
    }
}

