package com.example.msalmacen.controller;

import com.example.msalmacen.dto.ProductoTerminadoDTO;
import com.example.msalmacen.service.ProductoTerminadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos-terminados")
public class ProductoTerminadoController {

    private final ProductoTerminadoService service;

    public ProductoTerminadoController(ProductoTerminadoService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<ProductoTerminadoDTO> crear(@RequestBody ProductoTerminadoDTO dto) {
        return ResponseEntity.ok(service.crearProductoTerminado(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoTerminadoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerProductoTerminado(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductoTerminadoDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoTerminadoDTO> actualizar(@PathVariable Long id, @RequestBody ProductoTerminadoDTO dto) {
        return ResponseEntity.ok(service.actualizarProductoTerminado(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarProductoTerminado(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductoTerminadoDTO> descontarStock(
            @PathVariable Long id,
            @RequestParam int cantidad) {

        ProductoTerminadoDTO dto = service.descontarStock(id, cantidad);
        return ResponseEntity.ok(dto);
    }

}
