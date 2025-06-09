package com.example.msalmacen.controller;

import com.example.msalmacen.dto.IngresoMateriaPrimaDTO;
import com.example.msalmacen.service.IngresoMateriaPrimaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/ingresos-materia-prima")
@RequiredArgsConstructor
@Slf4j
public class IngresoMateriaPrimaController {

    private final IngresoMateriaPrimaService ingresoService;

    @GetMapping
    public ResponseEntity<List<IngresoMateriaPrimaDTO>> obtenerTodos() {
        try {
            List<IngresoMateriaPrimaDTO> ingresos = ingresoService.obtenerTodos();
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            log.error("Error al obtener todos los ingresos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngresoMateriaPrimaDTO> obtenerPorId(@PathVariable Long id) {
        try {
            return ingresoService.obtenerPorId(id)
                    .map(ingreso -> ResponseEntity.ok(ingreso))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error al obtener ingreso por ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<IngresoMateriaPrimaDTO> crear(@Valid @RequestBody IngresoMateriaPrimaDTO dto) {
        try {
            IngresoMateriaPrimaDTO ingresoCreado = ingresoService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(ingresoCreado);
        } catch (RuntimeException e) {
            log.error("Error de validación al crear ingreso: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error al crear ingreso: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngresoMateriaPrimaDTO> actualizar(@PathVariable Long id,
                                                             @Valid @RequestBody IngresoMateriaPrimaDTO dto) {
        try {
            IngresoMateriaPrimaDTO ingresoActualizado = ingresoService.actualizar(id, dto);
            return ResponseEntity.ok(ingresoActualizado);
        } catch (RuntimeException e) {
            log.error("Error de validación al actualizar ingreso {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Error al actualizar ingreso {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            ingresoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error al eliminar ingreso {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error al eliminar ingreso {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoints de búsqueda específicos
    @GetMapping("/proveedor/{idProveedor}")
    public ResponseEntity<List<IngresoMateriaPrimaDTO>> obtenerPorProveedor(@PathVariable Long idProveedor) {
        try {
            List<IngresoMateriaPrimaDTO> ingresos = ingresoService.obtenerPorProveedor(idProveedor);
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            log.error("Error al obtener ingresos por proveedor {}: {}", idProveedor, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/almacen/{idAlmacen}")
    public ResponseEntity<List<IngresoMateriaPrimaDTO>> obtenerPorAlmacen(@PathVariable Long idAlmacen) {
        try {
            List<IngresoMateriaPrimaDTO> ingresos = ingresoService.obtenerPorAlmacen(idAlmacen);
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            log.error("Error al obtener ingresos por almacén {}: {}", idAlmacen, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar-por-fecha")
    public ResponseEntity<List<IngresoMateriaPrimaDTO>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        try {
            List<IngresoMateriaPrimaDTO> ingresos = ingresoService.obtenerPorRangoFechas(fechaInicio, fechaFin);
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            log.error("Error al obtener ingresos por rango de fechas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar-por-documento")
    public ResponseEntity<List<IngresoMateriaPrimaDTO>> obtenerPorDocumento(@RequestParam String documento) {
        try {
            List<IngresoMateriaPrimaDTO> ingresos = ingresoService.obtenerPorDocumento(documento);
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            log.error("Error al obtener ingresos por documento {}: {}", documento, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}