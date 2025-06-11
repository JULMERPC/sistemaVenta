package com.example.ms.venta.client;

import com.example.ms.venta.dto.MateriaPrimaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

// Especificar la ruta base correcta
@FeignClient(name = "ms-almacen-service")
public interface MateriaPrimaClient {

    @GetMapping("/api/materias-primas/{id}")
    MateriaPrimaDTO obtenerMateriaPrimaPorId(@PathVariable("id") Long id);

    @GetMapping("/api/materias-primas")
    List<MateriaPrimaDTO> obtenerTodas();

    @GetMapping("/activas")
    List<MateriaPrimaDTO> obtenerMateriasPrimasActivas();
}