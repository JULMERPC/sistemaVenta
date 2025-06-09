package com.example.msalmacen.dto;


import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngresoMateriaPrimaDTO {

    private Long id;

    @NotNull(message = "El ID del proveedor es obligatorio")
    private Long idProveedor;

    @NotNull(message = "El ID del almacén es obligatorio")
    private Long idAlmacen;

    private LocalDateTime fechaIngreso;

    @Size(max = 100, message = "El documento de referencia no puede exceder 100 caracteres")
    private String documentoRef;

    private String observaciones;

    // Información adicional para la vista (no persistida)
    private String nombreProveedor;
    private String nombreAlmacen;

    // Constructor sin campos de vista
    public IngresoMateriaPrimaDTO(Long id, Long idProveedor, Long idAlmacen,
                                  LocalDateTime fechaIngreso, String documentoRef, String observaciones) {
        this.id = id;
        this.idProveedor = idProveedor;
        this.idAlmacen = idAlmacen;
        this.fechaIngreso = fechaIngreso;
        this.documentoRef = documentoRef;
        this.observaciones = observaciones;
    }

    // Getters y Setters explícitos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Long getIdAlmacen() {
        return idAlmacen;
    }

    public void setIdAlmacen(Long idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getDocumentoRef() {
        return documentoRef;
    }

    public void setDocumentoRef(String documentoRef) {
        this.documentoRef = documentoRef;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getNombreAlmacen() {
        return nombreAlmacen;
    }

    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
    }
}