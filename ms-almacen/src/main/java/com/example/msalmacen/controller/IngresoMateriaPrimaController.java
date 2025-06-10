package com.example.msalmacen.controller;

import com.example.msalmacen.dto.IngresoMateriaPrimaDTO;
import com.example.msalmacen.service.IngresoMateriaPrimaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ingresos-materia-prima")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*")
public class IngresoMateriaPrimaController {

    private final IngresoMateriaPrimaService ingresoService;

    @GetMapping
    public ResponseEntity<List<IngresoMateriaPrimaDTO>> obtenerTodos() {
        try {
            List<IngresoMateriaPrimaDTO> ingresos = ingresoService.obtenerTodos();
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            log.error("Error al obtener todos los ingresos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngresoMateriaPrimaDTO> obtenerPorId(@PathVariable Long id) {
        try {
            Optional<IngresoMateriaPrimaDTO> ingreso = ingresoService.obtenerPorId(id);
            return ingreso.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error al obtener ingreso por ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody IngresoMateriaPrimaDTO dto) {
        try {
            IngresoMateriaPrimaDTO ingresoCreado = ingresoService.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(ingresoCreado);
        } catch (RuntimeException e) {
            log.error("Error al crear ingreso: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al crear ingreso: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody IngresoMateriaPrimaDTO dto) {
        try {
            IngresoMateriaPrimaDTO ingresoActualizado = ingresoService.actualizar(id, dto);
            return ResponseEntity.ok(ingresoActualizado);
        } catch (RuntimeException e) {
            log.error("Error al actualizar ingreso {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al actualizar ingreso {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            ingresoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error al eliminar ingreso {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Error interno al eliminar ingreso {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    // Endpoints específicos para consultas
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<IngresoMateriaPrimaDTO>> obtenerPorProveedor(
            @PathVariable Long proveedorId) {
        try {
            List<IngresoMateriaPrimaDTO> ingresos = ingresoService.obtenerPorProveedor(proveedorId);
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            log.error("Error al obtener ingresos por proveedor {}: {}", proveedorId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/almacen/{almacenId}")
    public ResponseEntity<List<IngresoMateriaPrimaDTO>> obtenerPorAlmacen(
            @PathVariable Long almacenId) {
        try {
            List<IngresoMateriaPrimaDTO> ingresos = ingresoService.obtenerPorAlmacen(almacenId);
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            log.error("Error al obtener ingresos por almacén {}: {}", almacenId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/fechas")
    public ResponseEntity<List<IngresoMateriaPrimaDTO>> obtenerPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<IngresoMateriaPrimaDTO> ingresos = ingresoService.obtenerPorRangoFechas(fechaInicio, fechaFin);
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            log.error("Error al obtener ingresos por rango de fechas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/documento/{nroDocumento}")
    public ResponseEntity<IngresoMateriaPrimaDTO> obtenerPorNumeroDocumento(
            @PathVariable String nroDocumento) {
        try {
            Optional<IngresoMateriaPrimaDTO> ingreso = ingresoService.obtenerPorNumeroDocumento(nroDocumento);
            return ingreso.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error al obtener ingreso por número de documento {}: {}", nroDocumento, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoints para reportes y totales
    @GetMapping("/total/proveedor/{proveedorId}")
    public ResponseEntity<BigDecimal> obtenerTotalPorProveedorYFecha(
            @PathVariable Long proveedorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            BigDecimal total = ingresoService.obtenerTotalPorProveedorYFecha(proveedorId, fechaInicio, fechaFin);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            log.error("Error al obtener total por proveedor y fecha: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/total/almacen/{almacenId}")
    public ResponseEntity<BigDecimal> obtenerTotalPorAlmacenYFecha(
            @PathVariable Long almacenId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            BigDecimal total = ingresoService.obtenerTotalPorAlmacenYFecha(almacenId, fechaInicio, fechaFin);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            log.error("Error al obtener total por almacén y fecha: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}