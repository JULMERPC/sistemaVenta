
package com.example.msalmacen.controller;

import com.example.msalmacen.dto.MateriaPrimaDTO;
import com.example.msalmacen.service.MateriaPrimaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/materias-primas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class MateriaPrimaController {

    private final MateriaPrimaService materiaPrimaService;

    // Obtener todas las materias primas
    @GetMapping
    public ResponseEntity<List<MateriaPrimaDTO>> obtenerTodas() {
        try {
            log.info("GET /api/materias-primas - Obteniendo todas las materias primas");
            List<MateriaPrimaDTO> materiasPrimas = materiaPrimaService.obtenerTodas();
            return ResponseEntity.ok(materiasPrimas);
        } catch (Exception e) {
            log.error("Error al obtener materias primas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener materias primas activas
    @GetMapping("/activas")
    public ResponseEntity<List<MateriaPrimaDTO>> obtenerActivas() {
        try {
            log.info("GET /api/materias-primas/activas - Obteniendo materias primas activas");
            List<MateriaPrimaDTO> materiasPrimas = materiaPrimaService.obtenerActivas();
            return ResponseEntity.ok(materiasPrimas);
        } catch (Exception e) {
            log.error("Error al obtener materias primas activas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener materia prima por ID
    @GetMapping("/{id}")
    public ResponseEntity<MateriaPrimaDTO> obtenerPorId(@PathVariable Long id) {
        try {
            log.info("GET /api/materias-primas/{} - Obteniendo materia prima por ID", id);
            Optional<MateriaPrimaDTO> materiaPrima = materiaPrimaService.obtenerPorId(id);

            if (materiaPrima.isPresent()) {
                return ResponseEntity.ok(materiaPrima.get());
            } else {
                log.warn("Materia prima no encontrada con ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error al obtener materia prima con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Crear nueva materia prima
    @PostMapping
    public ResponseEntity<MateriaPrimaDTO> crear(@RequestBody MateriaPrimaDTO materiaPrimaDTO) {
        try {
            log.info("POST /api/materias-primas - Creando nueva materia prima: {}", materiaPrimaDTO.getNombre());
            MateriaPrimaDTO materiaPrimaCreada = materiaPrimaService.crear(materiaPrimaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(materiaPrimaCreada);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear materia prima: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error interno al crear materia prima: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Actualizar materia prima
    @PutMapping("/{id}")
    public ResponseEntity<MateriaPrimaDTO> actualizar(@PathVariable Long id, @RequestBody MateriaPrimaDTO materiaPrimaDTO) {
        try {
            log.info("PUT /api/materias-primas/{} - Actualizando materia prima", id);
            MateriaPrimaDTO materiaPrimaActualizada = materiaPrimaService.actualizar(id, materiaPrimaDTO);
            return ResponseEntity.ok(materiaPrimaActualizada);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al actualizar materia prima con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Error interno al actualizar materia prima con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Eliminar materia prima (eliminación lógica)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            log.info("DELETE /api/materias-primas/{} - Eliminando materia prima", id);
            materiaPrimaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("Error al eliminar materia prima con ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error interno al eliminar materia prima con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar materias primas por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<MateriaPrimaDTO>> buscarPorNombre(@RequestParam String nombre) {
        try {
            log.info("GET /api/materias-primas/buscar?nombre={} - Buscando por nombre", nombre);
            List<MateriaPrimaDTO> materiasPrimas = materiaPrimaService.buscarPorNombre(nombre);
            return ResponseEntity.ok(materiasPrimas);
        } catch (Exception e) {
            log.error("Error al buscar materias primas por nombre '{}': {}", nombre, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Buscar materias primas por unidad de medida
    @GetMapping("/unidad-medida/{unidadMedida}")
    public ResponseEntity<List<MateriaPrimaDTO>> buscarPorUnidadMedida(@PathVariable String unidadMedida) {
        try {
            log.info("GET /api/materias-primas/unidad-medida/{} - Buscando por unidad de medida", unidadMedida);
            List<MateriaPrimaDTO> materiasPrimas = materiaPrimaService.buscarPorUnidadMedida(unidadMedida);
            return ResponseEntity.ok(materiasPrimas);
        } catch (Exception e) {
            log.error("Error al buscar materias primas por unidad de medida '{}': {}", unidadMedida, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Activar materia prima
    @PatchMapping("/{id}/activar")
    public ResponseEntity<MateriaPrimaDTO> activar(@PathVariable Long id) {
        try {
            log.info("PATCH /api/materias-primas/{}/activar - Activando materia prima", id);
            MateriaPrimaDTO materiaPrimaActivada = materiaPrimaService.activar(id);
            return ResponseEntity.ok(materiaPrimaActivada);
        } catch (IllegalArgumentException e) {
            log.warn("Error al activar materia prima con ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error interno al activar materia prima con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint de health check
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("MateriaPrima Controller is running");
    }
}