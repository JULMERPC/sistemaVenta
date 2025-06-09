package com.example.msalmacen.client;

//import com.example.msalmacen.dto.ProveedorDTO;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//@FeignClient(name = "ms-proveedor", url = "http://localhost:8082")
//public interface ProveedorClient {
//
//    @GetMapping("/api/proveedores/{id}")
//    ProveedorDTO obtenerProveedor(@PathVariable Long id);
//}



import com.example.msalmacen.dto.ProveedorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// Ajusta el puerto según donde esté ejecutándose ms-proveedor2
@FeignClient(name = "ms-proveedor2-service")
public interface ProveedorClient {

    @GetMapping("/api/proveedores/{id}")
    ProveedorDTO obtenerProveedor(@PathVariable Long id);

    @GetMapping("/api/proveedores")
    List<ProveedorDTO> obtenerTodosProveedores();

    @GetMapping("/api/proveedores/activos")
    List<ProveedorDTO> obtenerProveedoresActivos();
}