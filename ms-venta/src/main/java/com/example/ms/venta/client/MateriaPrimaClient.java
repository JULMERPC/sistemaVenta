package com.example.ms.venta.client;

import com.example.ms.venta.dto.MateriaPrimaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-almacen-service") // ajusta la URL real
public interface MateriaPrimaClient {

    @GetMapping("/{id}")
    MateriaPrimaDTO obtenerMateriaPrimaPorId(@PathVariable("id") Long id);

    // Puedes añadir más endpoints según tus necesidades

    @GetMapping("/materias-primas") // ajusta si la ruta real es distinta
    List<MateriaPrimaDTO> obtenerMateriasPrimas();

}
