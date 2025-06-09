package com.example.msalmacen.controller;


import com.example.msalmacen.dto.DetalleIngresoDTO;
import com.example.msalmacen.service.DetalleIngresoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalle-ingresos")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "*")
public class DetalleIngresoController {

    private final DetalleIngresoService detalleIngresoService;

    // Crear un nuevo detalle de ingreso
    @PostMapping
    public ResponseEntity<DetalleIngresoDTO> crearDetalleIngreso(@Valid @RequestBody DetalleIngresoDTO detalleIngresoDTO) {
        try {
            DetalleIngresoDTO detalleCreado = detalleIngresoService.crearDetalleIngreso(detalleIngresoDTO);
            return new ResponseEntity<>(detalleCreado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Obtener todos los detalles de ingreso
    @GetMapping
    public ResponseEntity<List<DetalleIngresoDTO>> obtenerTodosLosDetalles() {
        try {
            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerTodosLosDetalles();
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener detalle por ID
    @GetMapping("/{id}")
    public ResponseEntity<DetalleIngresoDTO> obtenerDetallePorId(@PathVariable Long id) {
        try {
            Optional<DetalleIngresoDTO> detalle = detalleIngresoService.obtenerDetallePorId(id);
            return detalle.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener detalles por ID de ingreso
    @GetMapping("/ingreso/{idIngreso}")
    public ResponseEntity<List<DetalleIngresoDTO>> obtenerDetallesPorIngreso(@PathVariable Long idIngreso) {
        try {
            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerDetallesPorIngreso(idIngreso);
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener detalles por ID de materia prima
    @GetMapping("/materia-prima/{idMateriaPrima}")
    public ResponseEntity<List<DetalleIngresoDTO>> obtenerDetallesPorMateriaPrima(@PathVariable Long idMateriaPrima) {
        try {
            List<DetalleIngresoDTO> detalles = detalleIngresoService.obtenerDetallesPorMateriaPrima(idMateriaPrima);
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar detalle de ingreso
    @PutMapping("/{id}")
    public ResponseEntity<DetalleIngresoDTO> actualizarDetalleIngreso(
            @PathVariable Long id,
            @Valid @RequestBody DetalleIngresoDTO detalleIngresoDTO) {
        try {
            DetalleIngresoDTO detalleActualizado = detalleIngresoService.actualizarDetalleIngreso(id, detalleIngresoDTO);
            return new ResponseEntity<>(detalleActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar detalle de ingreso
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarDetalleIngreso(@PathVariable Long id) {
        try {
            detalleIngresoService.eliminarDetalleIngreso(id);
            return new ResponseEntity<>("Detalle de ingreso eliminado correctamente", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Detalle de ingreso no encontrado", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener total de un ingreso
    @GetMapping("/total-ingreso/{idIngreso}")
    public ResponseEntity<Double> obtenerTotalIngreso(@PathVariable Long idIngreso) {
        try {
            Double total = detalleIngresoService.obtenerTotalIngreso(idIngreso);
            return new ResponseEntity<>(total, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar todos los detalles de un ingreso
    @DeleteMapping("/ingreso/{idIngreso}")
    public ResponseEntity<String> eliminarDetallesPorIngreso(@PathVariable Long idIngreso) {
        try {
            detalleIngresoService.eliminarDetallesPorIngreso(idIngreso);
            return new ResponseEntity<>("Detalles del ingreso eliminados correctamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint adicional para crear múltiples detalles
    @PostMapping("/lote")
    public ResponseEntity<List<DetalleIngresoDTO>> crearDetallesEnLote(@Valid @RequestBody List<DetalleIngresoDTO> detallesDTO) {
        try {
            List<DetalleIngresoDTO> detallesCreados = detallesDTO.stream()
                    .map(detalleIngresoService::crearDetalleIngreso)
                    .toList();
            return new ResponseEntity<>(detallesCreados, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}