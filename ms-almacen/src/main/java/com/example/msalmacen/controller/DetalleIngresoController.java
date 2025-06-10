package com.example.msalmacen.controller;

import com.example.msalmacen.dto.DetalleIngresoDTO;
import com.example.msalmacen.service.DetalleIngresoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalle-ingresos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DetalleIngresoController {

    private final DetalleIngresoService detalleIngresoService;

    // Crear un nuevo detalle de ingreso
    @PostMapping
    public ResponseEntity<DetalleIngresoDTO> crearDetalleIngreso(@Valid @RequestBody DetalleIngresoDTO detalleIngresoDTO) {
        try {
            log.info("Creando detalle de ingreso: {}", detalleIngresoDTO);
            DetalleIngresoDTO nuevoDetalle = detalleIngresoService.crearDetalleIngreso(detalleIngresoDTO);
            return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            log.error("Error al crear detalle de ingreso: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error interno al crear detalle de ingreso", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los detalles de ingreso
    @GetMapping
    public ResponseEntity<List<DetalleIngresoDTO>> obtenerTodosLosDetalles() {
        try {
            log.info("Obteniendo todos los detalles de ingreso");
            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerTodosLosDetalles();
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener detalles de ingreso", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener un detalle por ID
    @GetMapping("/{id}")
    public ResponseEntity<DetalleIngresoDTO> obtenerDetallePorId(@PathVariable Long id) {
        try {
            log.info("Obteniendo detalle de ingreso con ID: {}", id);
            Optional<DetalleIngresoDTO> detalle = detalleIngresoService.obtenerDetallePorId(id);

            return detalle.map(d -> new ResponseEntity<>(d, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            log.error("Error al obtener detalle de ingreso con ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener detalles por ingreso
    @GetMapping("/ingreso/{ingresoId}")
    public ResponseEntity<List<DetalleIngresoDTO>> obtenerDetallesPorIngreso(@PathVariable Long ingresoId) {
        try {
            log.info("Obteniendo detalles para el ingreso ID: {}", ingresoId);
            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerDetallesPorIngreso(ingresoId);
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener detalles para el ingreso ID: {}", ingresoId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener detalles por materia prima
    @GetMapping("/materia-prima/{materiaPrimaId}")
    public ResponseEntity<List<DetalleIngresoDTO>> obtenerDetallesPorMateriaPrima(@PathVariable Long materiaPrimaId) {
        try {
            log.info("Obteniendo detalles para la materia prima ID: {}", materiaPrimaId);
            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerDetallesPorMateriaPrima(materiaPrimaId);
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener detalles para la materia prima ID: {}", materiaPrimaId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un detalle de ingreso
    @PutMapping("/{id}")
    public ResponseEntity<DetalleIngresoDTO> actualizarDetalleIngreso(
            @PathVariable Long id,
            @Valid @RequestBody DetalleIngresoDTO detalleIngresoDTO) {
        try {
            log.info("Actualizando detalle de ingreso con ID: {}", id);
            DetalleIngresoDTO detalleActualizado = detalleIngresoService.actualizarDetalleIngreso(id, detalleIngresoDTO);
            return new ResponseEntity<>(detalleActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.error("Error al actualizar detalle de ingreso con ID: {}, Error: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error interno al actualizar detalle de ingreso con ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un detalle de ingreso
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalleIngreso(@PathVariable Long id) {
        try {
            log.info("Eliminando detalle de ingreso con ID: {}", id);
            detalleIngresoService.eliminarDetalleIngreso(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            log.error("Error al eliminar detalle de ingreso con ID: {}, Error: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error interno al eliminar detalle de ingreso con ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener total de cantidad por materia prima
    @GetMapping("/total-cantidad/materia-prima/{materiaPrimaId}")
    public ResponseEntity<BigDecimal> obtenerTotalCantidadPorMateriaPrima(@PathVariable Long materiaPrimaId) {
        try {
            log.info("Obteniendo total de cantidad para materia prima ID: {}", materiaPrimaId);
            BigDecimal totalCantidad = detalleIngresoService.obtenerTotalCantidadPorMateriaPrima(materiaPrimaId);
            return new ResponseEntity<>(totalCantidad, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener total de cantidad para materia prima ID: {}", materiaPrimaId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener total de costo por ingreso
    @GetMapping("/total-costo/ingreso/{ingresoId}")
    public ResponseEntity<BigDecimal> obtenerTotalCostoPorIngreso(@PathVariable Long ingresoId) {
        try {
            log.info("Obteniendo total de costo para ingreso ID: {}", ingresoId);
            BigDecimal totalCosto = detalleIngresoService.obtenerTotalCostoPorIngreso(ingresoId);
            return new ResponseEntity<>(totalCosto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener total de costo para ingreso ID: {}", ingresoId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener detalles por rango de fechas
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<DetalleIngresoDTO>> obtenerDetallesPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            log.info("Obteniendo detalles entre {} y {}", fechaInicio, fechaFin);
            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerDetallesPorRangoFechas(fechaInicio, fechaFin);
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener detalles por rango de fechas", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener último costo unitario de una materia prima
    @GetMapping("/ultimo-costo/materia-prima/{materiaPrimaId}")
    public ResponseEntity<BigDecimal> obtenerUltimoCostoUnitario(@PathVariable Long materiaPrimaId) {
        try {
            log.info("Obteniendo último costo unitario para materia prima ID: {}", materiaPrimaId);
            Optional<BigDecimal> ultimoCosto = detalleIngresoService.obtenerUltimoCostoUnitario(materiaPrimaId);

            return ultimoCosto.map(costo -> new ResponseEntity<>(costo, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            log.error("Error al obtener último costo unitario para materia prima ID: {}", materiaPrimaId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para crear múltiples detalles de ingreso (útil para ingresos con varios items)
    @PostMapping("/lote")
    public ResponseEntity<List<DetalleIngresoDTO>> crearDetallesIngreso(@Valid @RequestBody List<DetalleIngresoDTO> detallesIngresoDTO) {
        try {
            log.info("Creando {} detalles de ingreso en lote", detallesIngresoDTO.size());
            List<DetalleIngresoDTO> nuevosDetalles = detallesIngresoDTO.stream()
                    .map(detalleIngresoService::crearDetalleIngreso)
                    .toList();
            return new ResponseEntity<>(nuevosDetalles, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            log.error("Error al crear detalles de ingreso en lote: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error interno al crear detalles de ingreso en lote", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}