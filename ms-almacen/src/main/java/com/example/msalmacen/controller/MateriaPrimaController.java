package com.example.msalmacen.controller;

import com.example.msalmacen.dto.MateriaPrimaDTO;
import com.example.msalmacen.dto.ProveedorDTO;
import com.example.msalmacen.service.MateriaPrimaService;
import com.example.msalmacen.service.impl.MateriaPrimaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class MateriaPrimaController {

    @Autowired
    private MateriaPrimaService productoFinalService;

    @Autowired
    private MateriaPrimaServiceImpl productoFinalServiceImpl; // Para métodos adicionales

    @PostMapping
    public ResponseEntity<MateriaPrimaDTO> crearProducto(@Valid @RequestBody MateriaPrimaDTO productoDTO) {
        try {
            MateriaPrimaDTO nuevoProducto = productoFinalService.guardar(productoDTO);
            return ResponseEntity.ok(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<MateriaPrimaDTO>> listarProductos() {
        try {
            List<MateriaPrimaDTO> productos = productoFinalService.listar();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<MateriaPrimaDTO>> listarProductosActivos() {
        try {
            List<MateriaPrimaDTO> productos = productoFinalServiceImpl.listarActivos();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<MateriaPrimaDTO> obtenerProductoPorCodigo(@PathVariable String codigo) {
        try {
            MateriaPrimaDTO producto = productoFinalService.obtenerPorCodigo(codigo);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/bajo-stock")
    public ResponseEntity<List<MateriaPrimaDTO>> obtenerProductosBajoStock() {
        try {
            List<MateriaPrimaDTO> productos = productoFinalServiceImpl.obtenerProductosBajoStock();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{codigo}/stock")
    public ResponseEntity<MateriaPrimaDTO> actualizarStock(@PathVariable String codigo,
                                                           @RequestParam Integer nuevoStock) {
        try {
            MateriaPrimaDTO producto = productoFinalServiceImpl.actualizarStock(codigo, nuevoStock);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoints que integran con el servicio de proveedores
    @GetMapping("/proveedores")
    public ResponseEntity<List<ProveedorDTO>> obtenerProveedoresDisponibles() {
        try {
            List<ProveedorDTO> proveedores = productoFinalServiceImpl.obtenerProveedoresDisponibles();
            return ResponseEntity.ok(proveedores);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{codigo}/proveedor/{proveedorId}/info")
    public ResponseEntity<String> obtenerInfoProductoConProveedor(@PathVariable String codigo,
                                                                  @PathVariable Long proveedorId) {
        try {
            String info = productoFinalServiceImpl.obtenerInfoProductoConProveedor(codigo, proveedorId);
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Endpoint para obtener estadísticas del almacén
    @GetMapping("/estadisticas")
    public ResponseEntity<String> obtenerEstadisticas() {
        try {
            List<MateriaPrimaDTO> todosProductos = productoFinalService.listar();
            List<MateriaPrimaDTO> productosActivos = productoFinalServiceImpl.listarActivos();
            List<MateriaPrimaDTO> productosBajoStock = productoFinalServiceImpl.obtenerProductosBajoStock();

            String estadisticas = String.format(
                    "Estadísticas del Almacén:\n" +
                            "- Total de productos: %d\n" +
                            "- Productos activos: %d\n" +
                            "- Productos con bajo stock: %d\n" +
                            "- Productos inactivos: %d",
                    todosProductos.size(),
                    productosActivos.size(),
                    productosBajoStock.size(),
                    todosProductos.size() - productosActivos.size()
            );

            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener estadísticas");
        }
    }
}