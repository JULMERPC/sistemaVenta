//package com.example.msalmacen.controller;
//
//
//import com.example.msalmacen.dto.DetalleIngresoDTO;
//import com.example.msalmacen.service.DetalleIngresoService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import jakarta.validation.Valid;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/detalle-ingresos")
//@RequiredArgsConstructor
//@Validated
//@CrossOrigin(origins = "*")
//public class DetalleIngresoController {
//
//    private final DetalleIngresoService detalleIngresoService;
//
//    // Crear un nuevo detalle de ingreso
//    @PostMapping
//    public ResponseEntity<DetalleIngresoDTO> crearDetalleIngreso(@Valid @RequestBody DetalleIngresoDTO detalleIngresoDTO) {
//        try {
//            DetalleIngresoDTO detalleCreado = detalleIngresoService.crearDetalleIngreso(detalleIngresoDTO);
//            return new ResponseEntity<>(detalleCreado, HttpStatus.CREATED);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    // Obtener todos los detalles de ingreso
//    @GetMapping
//    public ResponseEntity<List<DetalleIngresoDTO>> obtenerTodosLosDetalles() {
//        try {
//            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerTodosLosDetalles();
//            return new ResponseEntity<>(detalles, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Obtener detalle por ID
//    @GetMapping("/{id}")
//    public ResponseEntity<DetalleIngresoDTO> obtenerDetallePorId(@PathVariable Long id) {
//        try {
//            Optional<DetalleIngresoDTO> detalle = detalleIngresoService.obtenerDetallePorId(id);
//            return detalle.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
//                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Obtener detalles por ID de ingreso
//    @GetMapping("/ingreso/{idIngreso}")
//    public ResponseEntity<List<DetalleIngresoDTO>> obtenerDetallesPorIngreso(@PathVariable Long idIngreso) {
//        try {
//            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerDetallesPorIngreso(idIngreso);
//            return new ResponseEntity<>(detalles, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Obtener detalles por ID de materia prima
//    @GetMapping("/materia-prima/{idMateriaPrima}")
//    public ResponseEntity<List<DetalleIngresoDTO>> obtenerDetallesPorMateriaPrima(@PathVariable Long idMateriaPrima) {
//        try {
//            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerDetallesPorMateriaPrima(idMateriaPrima);
//            return new ResponseEntity<>(detalles, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Actualizar detalle de ingreso
//    @PutMapping("/{id}")
//    public ResponseEntity<DetalleIngresoDTO> actualizarDetalleIngreso(
//            @PathVariable Long id,
//            @Valid @RequestBody DetalleIngresoDTO detalleIngresoDTO) {
//        try {
//            DetalleIngresoDTO detalleActualizado = detalleIngresoService.actualizarDetalleIngreso(id, detalleIngresoDTO);
//            return new ResponseEntity<>(detalleActualizado, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Eliminar detalle de ingreso
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> eliminarDetalleIngreso(@PathVariable Long id) {
//        try {
//            detalleIngresoService.eliminarDetalleIngreso(id);
//            return new ResponseEntity<>("Detalle de ingreso eliminado correctamente", HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>("Detalle de ingreso no encontrado", HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Obtener total de un ingreso
//    @GetMapping("/total-ingreso/{idIngreso}")
//    public ResponseEntity<Double> obtenerTotalIngreso(@PathVariable Long idIngreso) {
//        try {
//            Double total = detalleIngresoService.obtenerTotalIngreso(idIngreso);
//            return new ResponseEntity<>(total, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Eliminar todos los detalles de un ingreso
//    @DeleteMapping("/ingreso/{idIngreso}")
//    public ResponseEntity<String> eliminarDetallesPorIngreso(@PathVariable Long idIngreso) {
//        try {
//            detalleIngresoService.eliminarDetallesPorIngreso(idIngreso);
//            return new ResponseEntity<>("Detalles del ingreso eliminados correctamente", HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Endpoint adicional para crear múltiples detalles
//    @PostMapping("/lote")
//    public ResponseEntity<List<DetalleIngresoDTO>> crearDetallesEnLote(@Valid @RequestBody List<DetalleIngresoDTO> detallesDTO) {
//        try {
//            List<DetalleIngresoDTO> detallesCreados = detallesDTO.stream()
//                    .map(detalleIngresoService::crearDetalleIngreso)
//                    .toList();
//            return new ResponseEntity<>(detallesCreados, HttpStatus.CREATED);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}

package com.example.msalmacen.controller;

import com.example.msalmacen.dto.DetalleIngresoDTO;
import com.example.msalmacen.service.DetalleIngresoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detalle-ingresos")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*")
public class DetalleIngresoController {

    private final DetalleIngresoService detalleIngresoService;

    // Crear un nuevo detalle de ingreso
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearDetalleIngreso(@Valid @RequestBody DetalleIngresoDTO detalleDTO) {
        log.info("Solicitud para crear detalle de ingreso: {}", detalleDTO);

        try {
            DetalleIngresoDTO detalleCreado = detalleIngresoService.crearDetalleIngreso(detalleDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Detalle de ingreso creado exitosamente");
            response.put("data", detalleCreado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error al crear detalle de ingreso: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al crear detalle de ingreso: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Crear múltiples detalles de ingreso
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> crearMultiplesDetalles(@Valid @RequestBody List<DetalleIngresoDTO> detallesDTO) {
        log.info("Solicitud para crear {} detalles de ingreso", detallesDTO.size());

        try {
            List<DetalleIngresoDTO> detallesCreados = detalleIngresoService.crearMultiplesDetalles(detallesDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Detalles de ingreso creados exitosamente");
            response.put("data", detallesCreados);
            response.put("total", detallesCreados.size());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error al crear múltiples detalles de ingreso: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al crear detalles de ingreso: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Obtener detalle por ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerDetallePorId(@PathVariable @Min(1) Long id) {
        log.info("Solicitud para obtener detalle de ingreso con ID: {}", id);

        try {
            DetalleIngresoDTO detalle = detalleIngresoService.obtenerDetallePorId(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", detalle);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener detalle de ingreso: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener detalle de ingreso: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Obtener todos los detalles con paginación
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodosLosDetalles(Pageable pageable) {
        log.info("Solicitud para obtener todos los detalles de ingreso - Página: {}, Tamaño: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        try {
            List<DetalleIngresoDTO> todosLosDetalles = detalleIngresoService.obtenerTodosLosDetalles();

            // Implementar paginación manual
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), todosLosDetalles.size());
            List<DetalleIngresoDTO> detallesPaginados = todosLosDetalles.subList(start, end);

            Page<DetalleIngresoDTO> page = new PageImpl<>(detallesPaginados, pageable, todosLosDetalles.size());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", page.getContent());
            response.put("currentPage", page.getNumber());
            response.put("totalPages", page.getTotalPages());
            response.put("totalElements", page.getTotalElements());
            response.put("size", page.getSize());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener detalles de ingreso: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener detalles de ingreso: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Obtener detalles por ID de ingreso
    @GetMapping("/ingreso/{ingresoId}")
    public ResponseEntity<Map<String, Object>> obtenerDetallesPorIngresoId(@PathVariable @Min(1) Long ingresoId) {
        log.info("Solicitud para obtener detalles del ingreso ID: {}", ingresoId);

        try {
            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerDetallesPorIngresoId(ingresoId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", detalles);
            response.put("total", detalles.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener detalles por ingreso ID: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener detalles por ingreso: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Obtener detalles por ID de materia prima
    @GetMapping("/materia-prima/{materiaPrimaId}")
    public ResponseEntity<Map<String, Object>> obtenerDetallesPorMateriaPrimaId(@PathVariable @Min(1) Long materiaPrimaId) {
        log.info("Solicitud para obtener detalles de la materia prima ID: {}", materiaPrimaId);

        try {
            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerDetallesPorMateriaPrimaId(materiaPrimaId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", detalles);
            response.put("total", detalles.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener detalles por materia prima ID: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al obtener detalles por materia prima: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Actualizar detalle de ingreso
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarDetalleIngreso(
            @PathVariable @Min(1) Long id,
            @Valid @RequestBody DetalleIngresoDTO detalleDTO) {
        log.info("Solicitud para actualizar detalle de ingreso ID: {}", id);

        try {
            DetalleIngresoDTO detalleActualizado = detalleIngresoService.actualizarDetalleIngreso(id, detalleDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Detalle de ingreso actualizado exitosamente");
            response.put("data", detalleActualizado);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al actualizar detalle de ingreso: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al actualizar detalle de ingreso: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Eliminar detalle de ingreso
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarDetalleIngreso(@PathVariable @Min(1) Long id) {
        log.info("Solicitud para eliminar detalle de ingreso ID: {}", id);

        try {
            detalleIngresoService.eliminarDetalle(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Detalle de ingreso eliminado exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al eliminar detalle de ingreso: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al eliminar detalle de ingreso: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Eliminar todos los detalles de un ingreso
    @DeleteMapping("/ingreso/{ingresoId}")
    public ResponseEntity<Map<String, Object>> eliminarDetallesPorIngresoId(@PathVariable @Min(1) Long ingresoId) {
        log.info("Solicitud para eliminar todos los detalles del ingreso ID: {}", ingresoId);

        try {
            detalleIngresoService.eliminarDetallesPorIngresoId(ingresoId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Todos los detalles del ingreso eliminados exitosamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al eliminar detalles por ingreso ID: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al eliminar detalles del ingreso: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Calcular costo total por ingreso
    @GetMapping("/ingreso/{ingresoId}/costo-total")
    public ResponseEntity<Map<String, Object>> calcularCostoTotalPorIngreso(@PathVariable @Min(1) Long ingresoId) {
        log.info("Solicitud para calcular costo total del ingreso ID: {}", ingresoId);

        try {
            BigDecimal costoTotal = detalleIngresoService.calcularCostoTotalPorIngreso(ingresoId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("ingresoId", ingresoId);
            response.put("costoTotal", costoTotal);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al calcular costo total: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al calcular costo total: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Calcular cantidad total ingresada por materia prima
    @GetMapping("/materia-prima/{materiaPrimaId}/cantidad-total")
    public ResponseEntity<Map<String, Object>> calcularCantidadTotalPorMateriaPrima(@PathVariable @Min(1) Long materiaPrimaId) {
        log.info("Solicitud para calcular cantidad total de la materia prima ID: {}", materiaPrimaId);

        try {
            BigDecimal cantidadTotal = detalleIngresoService.calcularCantidadTotalPorMateriaPrima(materiaPrimaId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("materiaPrimaId", materiaPrimaId);
            response.put("cantidadTotal", cantidadTotal);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al calcular cantidad total: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al calcular cantidad total: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Endpoint para validar un detalle antes de guardarlo
    @PostMapping("/validar")
    public ResponseEntity<Map<String, Object>> validarDetalle(@Valid @RequestBody DetalleIngresoDTO detalleDTO) {
        log.info("Solicitud para validar detalle de ingreso");

        try {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("valid", detalleDTO.isValid());
            response.put("costoTotal", detalleDTO.getCostoTotal());
            response.put("message", "Detalle validado correctamente");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al validar detalle: {}", e.getMessage());

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error al validar detalle: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Manejo de errores de validación
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Errores de validación");
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Manejo de errores de constraint violation
    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(
            jakarta.validation.ConstraintViolationException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Error de validación: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}