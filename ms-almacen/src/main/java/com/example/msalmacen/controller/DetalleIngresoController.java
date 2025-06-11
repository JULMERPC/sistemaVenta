//package com.example.msalmacen.controller;
//
//import com.example.msalmacen.dto.DetalleIngresoDTO;
//import com.example.msalmacen.service.DetalleIngresoService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/detalle-ingresos")
//@RequiredArgsConstructor
//@Slf4j
//@CrossOrigin(origins = "*")
//public class DetalleIngresoController {
//
//    private final DetalleIngresoService detalleIngresoService;
//
//    // Crear un nuevo detalle de ingreso
//    @PostMapping
//    public ResponseEntity<DetalleIngresoDTO> crearDetalleIngreso(@Valid @RequestBody DetalleIngresoDTO detalleIngresoDTO) {
//        try {
//            log.info("Creando detalle de ingreso: {}", detalleIngresoDTO);
//            DetalleIngresoDTO nuevoDetalle = detalleIngresoService.crearDetalleIngreso(detalleIngresoDTO);
//            return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
//        } catch (RuntimeException e) {
//            log.error("Error al crear detalle de ingreso: {}", e.getMessage());
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            log.error("Error interno al crear detalle de ingreso", e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Obtener todos los detalles de ingreso
//    @GetMapping
//    public ResponseEntity<List<DetalleIngresoDTO>> obtenerTodosLosDetalles() {
//        try {
//            log.info("Obteniendo todos los detalles de ingreso");
//            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerTodosLosDetalles();
//            return new ResponseEntity<>(detalles, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Error al obtener detalles de ingreso", e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Obtener un detalle por ID
//    @GetMapping("/{id}")
//    public ResponseEntity<DetalleIngresoDTO> obtenerDetallePorId(@PathVariable Long id) {
//        try {
//            log.info("Obteniendo detalle de ingreso con ID: {}", id);
//            Optional<DetalleIngresoDTO> detalle = detalleIngresoService.obtenerDetallePorId(id);
//
//            return detalle.map(d -> new ResponseEntity<>(d, HttpStatus.OK))
//                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//        } catch (Exception e) {
//            log.error("Error al obtener detalle de ingreso con ID: {}", id, e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Obtener detalles por ingreso
//    @GetMapping("/ingreso/{ingresoId}")
//    public ResponseEntity<List<DetalleIngresoDTO>> obtenerDetallesPorIngreso(@PathVariable Long ingresoId) {
//        try {
//            log.info("Obteniendo detalles para el ingreso ID: {}", ingresoId);
//            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerDetallesPorIngreso(ingresoId);
//            return new ResponseEntity<>(detalles, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Error al obtener detalles para el ingreso ID: {}", ingresoId, e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Obtener detalles por materia prima
//    @GetMapping("/materia-prima/{materiaPrimaId}")
//    public ResponseEntity<List<DetalleIngresoDTO>> obtenerDetallesPorMateriaPrima(@PathVariable Long materiaPrimaId) {
//        try {
//            log.info("Obteniendo detalles para la materia prima ID: {}", materiaPrimaId);
//            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerDetallesPorMateriaPrima(materiaPrimaId);
//            return new ResponseEntity<>(detalles, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Error al obtener detalles para la materia prima ID: {}", materiaPrimaId, e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Actualizar un detalle de ingreso
//    @PutMapping("/{id}")
//    public ResponseEntity<DetalleIngresoDTO> actualizarDetalleIngreso(
//            @PathVariable Long id,
//            @Valid @RequestBody DetalleIngresoDTO detalleIngresoDTO) {
//        try {
//            log.info("Actualizando detalle de ingreso con ID: {}", id);
//            DetalleIngresoDTO detalleActualizado = detalleIngresoService.actualizarDetalleIngreso(id, detalleIngresoDTO);
//            return new ResponseEntity<>(detalleActualizado, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            log.error("Error al actualizar detalle de ingreso con ID: {}, Error: {}", id, e.getMessage());
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            log.error("Error interno al actualizar detalle de ingreso con ID: {}", id, e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Eliminar un detalle de ingreso
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> eliminarDetalleIngreso(@PathVariable Long id) {
//        try {
//            log.info("Eliminando detalle de ingreso con ID: {}", id);
//            detalleIngresoService.eliminarDetalleIngreso(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (RuntimeException e) {
//            log.error("Error al eliminar detalle de ingreso con ID: {}, Error: {}", id, e.getMessage());
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } catch (Exception e) {
//            log.error("Error interno al eliminar detalle de ingreso con ID: {}", id, e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Obtener total de cantidad por materia prima
//    @GetMapping("/total-cantidad/materia-prima/{materiaPrimaId}")
//    public ResponseEntity<BigDecimal> obtenerTotalCantidadPorMateriaPrima(@PathVariable Long materiaPrimaId) {
//        try {
//            log.info("Obteniendo total de cantidad para materia prima ID: {}", materiaPrimaId);
//            BigDecimal totalCantidad = detalleIngresoService.obtenerTotalCantidadPorMateriaPrima(materiaPrimaId);
//            return new ResponseEntity<>(totalCantidad, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Error al obtener total de cantidad para materia prima ID: {}", materiaPrimaId, e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Obtener total de costo por ingreso
//    @GetMapping("/total-costo/ingreso/{ingresoId}")
//    public ResponseEntity<BigDecimal> obtenerTotalCostoPorIngreso(@PathVariable Long ingresoId) {
//        try {
//            log.info("Obteniendo total de costo para ingreso ID: {}", ingresoId);
//            BigDecimal totalCosto = detalleIngresoService.obtenerTotalCostoPorIngreso(ingresoId);
//            return new ResponseEntity<>(totalCosto, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Error al obtener total de costo para ingreso ID: {}", ingresoId, e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Obtener detalles por rango de fechas
//    @GetMapping("/rango-fechas")
//    public ResponseEntity<List<DetalleIngresoDTO>> obtenerDetallesPorRangoFechas(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
//        try {
//            log.info("Obteniendo detalles entre {} y {}", fechaInicio, fechaFin);
//            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerDetallesPorRangoFechas(fechaInicio, fechaFin);
//            return new ResponseEntity<>(detalles, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Error al obtener detalles por rango de fechas", e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Obtener último costo unitario de una materia prima
//    @GetMapping("/ultimo-costo/materia-prima/{materiaPrimaId}")
//    public ResponseEntity<BigDecimal> obtenerUltimoCostoUnitario(@PathVariable Long materiaPrimaId) {
//        try {
//            log.info("Obteniendo último costo unitario para materia prima ID: {}", materiaPrimaId);
//            Optional<BigDecimal> ultimoCosto = detalleIngresoService.obtenerUltimoCostoUnitario(materiaPrimaId);
//
//            return ultimoCosto.map(costo -> new ResponseEntity<>(costo, HttpStatus.OK))
//                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
//        } catch (Exception e) {
//            log.error("Error al obtener último costo unitario para materia prima ID: {}", materiaPrimaId, e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // Endpoint para crear múltiples detalles de ingreso (útil para ingresos con varios items)
//    @PostMapping("/lote")
//    public ResponseEntity<List<DetalleIngresoDTO>> crearDetallesIngreso(@Valid @RequestBody List<DetalleIngresoDTO> detallesIngresoDTO) {
//        try {
//            log.info("Creando {} detalles de ingreso en lote", detallesIngresoDTO.size());
//            List<DetalleIngresoDTO> nuevosDetalles = detallesIngresoDTO.stream()
//                    .map(detalleIngresoService::crearDetalleIngreso)
//                    .toList();
//            return new ResponseEntity<>(nuevosDetalles, HttpStatus.CREATED);
//        } catch (RuntimeException e) {
//            log.error("Error al crear detalles de ingreso en lote: {}", e.getMessage());
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            log.error("Error interno al crear detalles de ingreso en lote", e);
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//}

package com.example.msalmacen.controller;

import com.example.msalmacen.dto.DetalleIngresoDTO;
import com.example.msalmacen.service.DetalleIngresoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detalle-ingresos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DetalleIngresoController {

    private final DetalleIngresoService detalleIngresoService;

    /**
     * Crear un nuevo detalle de ingreso
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody DetalleIngresoDTO detalleIngresoDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Creando detalle de ingreso: {}", detalleIngresoDTO);

            DetalleIngresoDTO detalleCreado = detalleIngresoService.crear(detalleIngresoDTO);

            response.put("success", true);
            response.put("message", "Detalle de ingreso creado exitosamente");
            response.put("data", detalleCreado);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (EntityNotFoundException e) {
            log.error("Error al crear detalle de ingreso - Entidad no encontrada: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (IllegalArgumentException e) {
            log.error("Error al crear detalle de ingreso - Argumento inválido: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            log.error("Error inesperado al crear detalle de ingreso: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener todos los detalles de ingreso
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodos() {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Obteniendo todos los detalles de ingreso");

            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerTodos();

            response.put("success", true);
            response.put("message", "Detalles de ingreso obtenidos exitosamente");
            response.put("data", detalles);
            response.put("total", detalles.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener detalles de ingreso: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener detalle de ingreso por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerPorId(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Obteniendo detalle de ingreso por ID: {}", id);

            DetalleIngresoDTO detalle = detalleIngresoService.obtenerPorId(id);

            response.put("success", true);
            response.put("message", "Detalle de ingreso encontrado");
            response.put("data", detalle);

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            log.error("Detalle de ingreso no encontrado con ID {}: {}", id, e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            log.error("Error al obtener detalle de ingreso por ID {}: {}", id, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener detalles por ID de ingreso de materia prima
     */
    @GetMapping("/ingreso/{ingresoId}")
    public ResponseEntity<Map<String, Object>> obtenerPorIngresoMateriaPrima(@PathVariable Long ingresoId) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Obteniendo detalles por ingreso de materia prima ID: {}", ingresoId);

            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerPorIngresoMateriaPrima(ingresoId);

            response.put("success", true);
            response.put("message", "Detalles de ingreso obtenidos exitosamente");
            response.put("data", detalles);
            response.put("total", detalles.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener detalles por ingreso ID {}: {}", ingresoId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener detalles por ID de materia prima
     */
    @GetMapping("/materia-prima/{materiaPrimaId}")
    public ResponseEntity<Map<String, Object>> obtenerPorMateriaPrima(@PathVariable Long materiaPrimaId) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Obteniendo detalles por materia prima ID: {}", materiaPrimaId);

            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerPorMateriaPrima(materiaPrimaId);

            response.put("success", true);
            response.put("message", "Detalles de ingreso obtenidos exitosamente");
            response.put("data", detalles);
            response.put("total", detalles.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener detalles por materia prima ID {}: {}", materiaPrimaId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Actualizar detalle de ingreso
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody DetalleIngresoDTO detalleIngresoDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Actualizando detalle de ingreso con ID: {}", id);

            DetalleIngresoDTO detalleActualizado = detalleIngresoService.actualizar(id, detalleIngresoDTO);

            response.put("success", true);
            response.put("message", "Detalle de ingreso actualizado exitosamente");
            response.put("data", detalleActualizado);

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            log.error("Error al actualizar detalle de ingreso - Entidad no encontrada: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            log.error("Error al actualizar detalle de ingreso con ID {}: {}", id, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Eliminar detalle de ingreso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Eliminando detalle de ingreso con ID: {}", id);

            detalleIngresoService.eliminar(id);

            response.put("success", true);
            response.put("message", "Detalle de ingreso eliminado exitosamente");

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            log.error("Error al eliminar detalle de ingreso - No encontrado: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            log.error("Error al eliminar detalle de ingreso con ID {}: {}", id, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Crear múltiples detalles de ingreso
     */
    @PostMapping("/multiple")
    public ResponseEntity<Map<String, Object>> crearMultiples(@Valid @RequestBody List<DetalleIngresoDTO> detallesDTO) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Creando {} detalles de ingreso", detallesDTO.size());

            List<DetalleIngresoDTO> detallesCreados = detalleIngresoService.crearMultiples(detallesDTO);

            response.put("success", true);
            response.put("message", "Detalles de ingreso creados exitosamente");
            response.put("data", detallesCreados);
            response.put("total", detallesCreados.size());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (EntityNotFoundException e) {
            log.error("Error al crear detalles múltiples - Entidad no encontrada: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (IllegalArgumentException e) {
            log.error("Error al crear detalles múltiples - Argumento inválido: {}", e.getMessage());
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            log.error("Error inesperado al crear detalles múltiples: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Eliminar todos los detalles de un ingreso específico
     */
    @DeleteMapping("/ingreso/{ingresoId}")
    public ResponseEntity<Map<String, Object>> eliminarPorIngresoMateriaPrima(@PathVariable Long ingresoId) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Eliminando todos los detalles del ingreso ID: {}", ingresoId);

            detalleIngresoService.eliminarPorIngresoMateriaPrima(ingresoId);

            response.put("success", true);
            response.put("message", "Todos los detalles del ingreso han sido eliminados exitosamente");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al eliminar detalles del ingreso ID {}: {}", ingresoId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Contar detalles por ingreso
     */
    @GetMapping("/count/ingreso/{ingresoId}")
    public ResponseEntity<Map<String, Object>> contarPorIngresoMateriaPrima(@PathVariable Long ingresoId) {
        Map<String, Object> response = new HashMap<>();

        try {
            log.info("Contando detalles del ingreso ID: {}", ingresoId);

            Long cantidad = detalleIngresoService.contarPorIngresoMateriaPrima(ingresoId);

            response.put("success", true);
            response.put("message", "Cantidad de detalles obtenida exitosamente");
            response.put("data", cantidad);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al contar detalles del ingreso ID {}: {}", ingresoId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}