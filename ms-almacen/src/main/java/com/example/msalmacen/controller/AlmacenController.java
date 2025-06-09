package com.example.msalmacen.controller;


import com.example.msalmacen.dto.AlmacenDTO;
import com.example.msalmacen.service.AlmacenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/almacenes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AlmacenController {

    private static final Logger log = LoggerFactory.getLogger(AlmacenController.class);
    private final AlmacenService almacenService;

    // Health Check
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.info("Health check - Almacenes");
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Almacen Service");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    // POST /api/almacenes - Crear nuevo almacén
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody AlmacenDTO almacenDTO) {
        try {
            log.info("Creando nuevo almacén: {}", almacenDTO.getNombre());
            AlmacenDTO nuevoAlmacen = almacenService.crear(almacenDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAlmacen);
        } catch (Exception e) {
            log.error("Error al crear almacén: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // GET /api/almacenes/{id} - Obtener almacén por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            log.info("Obteniendo almacén con ID: {}", id);
            AlmacenDTO almacen = almacenService.obtenerPorId(id);
            return ResponseEntity.ok(almacen);
        } catch (Exception e) {
            log.error("Error al obtener almacén con ID {}: {}", id, e.getMessage());
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Almacén no encontrado");
            error.put("id", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // GET /api/almacenes - Obtener todos los almacenes
    @GetMapping
    public ResponseEntity<List<AlmacenDTO>> listarTodos() {
        try {
            log.info("Listando todos los almacenes");
            List<AlmacenDTO> almacenes = almacenService.listarTodos();
            return ResponseEntity.ok(almacenes);
        } catch (Exception e) {
            log.error("Error al listar almacenes: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // GET /api/almacenes/activos - Obtener almacenes activos
    @GetMapping("/activos")
    public ResponseEntity<List<AlmacenDTO>> listarActivos() {
        try {
            log.info("Listando almacenes activos");
            List<AlmacenDTO> almacenes = almacenService.listarActivos();
            return ResponseEntity.ok(almacenes);
        } catch (Exception e) {
            log.error("Error al listar almacenes activos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // PUT /api/almacenes/{id} - Actualizar almacén
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody AlmacenDTO almacenDTO) {
        try {
            log.info("Actualizando almacén con ID: {}", id);
            AlmacenDTO almacenActualizado = almacenService.actualizar(id, almacenDTO);
            return ResponseEntity.ok(almacenActualizado);
        } catch (Exception e) {
            log.error("Error al actualizar almacén con ID {}: {}", id, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // DELETE /api/almacenes/{id} - Eliminar almacén
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            log.info("Eliminando almacén con ID: {}", id);
            almacenService.eliminar(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Almacén eliminado correctamente");
            response.put("id", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al eliminar almacén con ID {}: {}", id, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // PUT /api/almacenes/{id}/activar - Activar almacén
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activar(@PathVariable Long id) {
        try {
            log.info("Activando almacén con ID: {}", id);
            almacenService.activar(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Almacén activado correctamente");
            response.put("id", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al activar almacén con ID {}: {}", id, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // PUT /api/almacenes/{id}/desactivar - Desactivar almacén
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        try {
            log.info("Desactivando almacén con ID: {}", id);
            almacenService.desactivar(id);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Almacén desactivado correctamente");
            response.put("id", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al desactivar almacén con ID {}: {}", id, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // GET /api/almacenes/buscar?nombre={nombre} - Buscar por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<AlmacenDTO>> buscarPorNombre(@RequestParam String nombre) {
        try {
            log.info("Buscando almacenes por nombre: {}", nombre);
            List<AlmacenDTO> almacenes = almacenService.buscarPorNombre(nombre);
            return ResponseEntity.ok(almacenes);
        } catch (Exception e) {
            log.error("Error al buscar almacenes por nombre {}: {}", nombre, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // GET /api/almacenes/tipo/{tipo} - Buscar por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<AlmacenDTO>> buscarPorTipo(@PathVariable String tipo) {
        try {
            log.info("Buscando almacenes por tipo: {}", tipo);
            List<AlmacenDTO> almacenes = almacenService.buscarPorTipo(tipo);
            return ResponseEntity.ok(almacenes);
        } catch (Exception e) {
            log.error("Error al buscar almacenes por tipo {}: {}", tipo, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // GET /api/almacenes/ubicacion/{ubicacion} - Buscar por ubicación
    @GetMapping("/ubicacion/{ubicacion}")
    public ResponseEntity<List<AlmacenDTO>> buscarPorUbicacion(@PathVariable String ubicacion) {
        try {
            log.info("Buscando almacenes por ubicación: {}", ubicacion);
            List<AlmacenDTO> almacenes = almacenService.buscarPorUbicacion(ubicacion);
            return ResponseEntity.ok(almacenes);
        } catch (Exception e) {
            log.error("Error al buscar almacenes por ubicación {}: {}", ubicacion, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}