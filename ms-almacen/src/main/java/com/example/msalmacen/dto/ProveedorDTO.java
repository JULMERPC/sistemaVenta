package com.example.msalmacen.dto;
//
import lombok.*;
//
//@Data
////@NoArgsConstructor
////@AllArgsConstructor
//@Builder
//public class ProveedorDTO {
//    private Long id;
//    private String nombre;
//    private String ruc;
//    private String direccion;
//
//    public ProveedorDTO() {
//    }
//
//    public ProveedorDTO(Long id, String nombre, String ruc, String direccion) {
//        this.id = id;
//        this.nombre = nombre;
//        this.ruc = ruc;
//        this.direccion = direccion;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getDireccion() {
//        return direccion;
//    }
//
//    public void setDireccion(String direccion) {
//        this.direccion = direccion;
//    }
//
//    public String getRuc() {
//        return ruc;
//    }
//
//    public void setRuc(String ruc) {
//        this.ruc = ruc;
//    }
//
//    public String getNombre() {
//        return nombre;
//    }
//
//    public void setNombre(String nombre) {
//        this.nombre = nombre;
//    }
//}


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProveedorDTO {
    private Long id;
    private String nombreEmpresa;
    private String contacto;
    private String correoElectronico;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String pais;

    public ProveedorDTO() {
    }

    public ProveedorDTO(Long id, String nombreEmpresa, String contacto, String correoElectronico,
                        String telefono, String direccion, String ciudad, String pais) {
        this.id = id;
        this.nombreEmpresa = nombreEmpresa;
        this.contacto = contacto;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }
}