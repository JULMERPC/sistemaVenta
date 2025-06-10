package com.example.ms.venta.controller;

import com.example.ms.venta.dto.DetalleVentaDto;
import com.example.ms.venta.service.DetalleVentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalle-ventas")
@CrossOrigin(origins = "*")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    // Crear un nuevo detalle de venta
    @PostMapping
    public ResponseEntity<?> crearDetalleVenta(@Valid @RequestBody DetalleVentaDto detalleVentaDto) {
        try {
            DetalleVentaDto detalleCreado = detalleVentaService.crearDetalleVenta(detalleVentaDto);
            return new ResponseEntity<>(detalleCreado, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error al crear detalle de venta: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error interno del servidor"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Crear múltiples detalles de venta
    @PostMapping("/multiple")
    public ResponseEntity<?> crearMultiplesDetalles(@Valid @RequestBody List<DetalleVentaDto> detallesDto) {
        try {
            List<DetalleVentaDto> detallesCreados = detalleVentaService.crearMultiplesDetalles(detallesDto);
            return new ResponseEntity<>(detallesCreados, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error al crear detalles: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error interno del servidor"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Obtener todos los detalles de venta
    @GetMapping
    public ResponseEntity<List<DetalleVentaDto>> obtenerTodosLosDetalles() {
        try {
            List<DetalleVentaDto> detalles = detalleVentaService.obtenerTodosLosDetalles();
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener detalle por ID
//    @GetMapping("/{id}")
//    public ResponseEntity<?> obtenerDetallePorId(@PathVariable Long id) {
//        try {
//            Optional<DetalleVentaDto> detalle = detalleVentaService.obtenerDetallePorId(id);
//            return detalle.map(d -> new ResponseEntity<>(d, HttpStatus.OK))
//                    .orElseGet(() -> new ResponseEntity<>(
//                            new ErrorResponse("Detalle de venta no encontrado"),
//                            HttpStatus.NOT_FOUND));
//        } catch (Exception e) {
//            return new ResponseEntity<>(
//                    new ErrorResponse("Error interno del servidor"),
//                    HttpStatus.INTERNAL_SERVER_ERROR
//            );
//        }
//    }
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerDetallePorId(@PathVariable Long id) {
        try {
            Optional<DetalleVentaDto> detalle = detalleVentaService.obtenerDetallePorId(id);
            return detalle
                    .<ResponseEntity<?>>map(d -> ResponseEntity.ok(d))
                    .orElseGet(() -> ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(new ErrorResponse("Detalle de venta no encontrado")));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor"));
        }
    }


    // Obtener detalles por venta
    @GetMapping("/venta/{ventaId}")
    public ResponseEntity<List<DetalleVentaDto>> obtenerDetallesPorVenta(@PathVariable Long ventaId) {
        try {
            List<DetalleVentaDto> detalles = detalleVentaService.obtenerDetallesPorVenta(ventaId);
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener detalles por materia prima
    @GetMapping("/materia-prima/{materiaPrimaId}")
    public ResponseEntity<List<DetalleVentaDto>> obtenerDetallesPorMateriaPrima(@PathVariable Long materiaPrimaId) {
        try {
            List<DetalleVentaDto> detalles = detalleVentaService.obtenerDetallesPorMateriaPrima(materiaPrimaId);
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar detalle de venta
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarDetalleVenta(@PathVariable Long id,
                                                    @Valid @RequestBody DetalleVentaDto detalleVentaDto) {
        try {
            DetalleVentaDto detalleActualizado = detalleVentaService.actualizarDetalleVenta(id, detalleVentaDto);
            return new ResponseEntity<>(detalleActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error al actualizar detalle: " + e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error interno del servidor"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Eliminar detalle de venta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDetalleVenta(@PathVariable Long id) {
        try {
            detalleVentaService.eliminarDetalleVenta(id);
            return new ResponseEntity<>(
                    new SuccessResponse("Detalle de venta eliminado exitosamente"),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error al eliminar detalle: " + e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error interno del servidor"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Eliminar todos los detalles de una venta
    @DeleteMapping("/venta/{ventaId}")
    public ResponseEntity<?> eliminarDetallesPorVenta(@PathVariable Long ventaId) {
        try {
            detalleVentaService.eliminarDetallesPorVenta(ventaId);
            return new ResponseEntity<>(
                    new SuccessResponse("Detalles de venta eliminados exitosamente"),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error interno del servidor"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Calcular total de una venta
    @GetMapping("/venta/{ventaId}/total")
    public ResponseEntity<?> calcularTotalVenta(@PathVariable Long ventaId) {
        try {
            BigDecimal total = detalleVentaService.calcularTotalVenta(ventaId);
            return new ResponseEntity<>(new TotalResponse(total), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error interno del servidor"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Contar detalles de una venta
    @GetMapping("/venta/{ventaId}/count")
    public ResponseEntity<?> contarDetallesPorVenta(@PathVariable Long ventaId) {
        try {
            long count = detalleVentaService.contarDetallesPorVenta(ventaId);
            return new ResponseEntity<>(new CountResponse(count), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error interno del servidor"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Verificar si existe detalle
    @GetMapping("/exists/venta/{ventaId}/materia-prima/{materiaPrimaId}")
    public ResponseEntity<?> verificarExistenciaDetalle(@PathVariable Long ventaId,
                                                        @PathVariable Long materiaPrimaId) {
        try {
            boolean existe = detalleVentaService.existeDetalle(ventaId, materiaPrimaId);
            return new ResponseEntity<>(new ExistsResponse(existe), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error interno del servidor"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Obtener materias primas más vendidas
    @GetMapping("/estadisticas/materias-primas-mas-vendidas")
    public ResponseEntity<?> obtenerMateriasPrimasMasVendidas() {
        try {
            List<Object[]> estadisticas = detalleVentaService.obtenerMateriasPrimasMasVendidas();
            return new ResponseEntity<>(estadisticas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ErrorResponse("Error interno del servidor"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // Clases de respuesta para APIs específicas
    public static class ErrorResponse {
        private String mensaje;
        private String timestamp;

        public ErrorResponse(String mensaje) {
            this.mensaje = mensaje;
            this.timestamp = java.time.LocalDateTime.now().toString();
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class SuccessResponse {
        private String mensaje;
        private String timestamp;

        public SuccessResponse(String mensaje) {
            this.mensaje = mensaje;
            this.timestamp = java.time.LocalDateTime.now().toString();
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class TotalResponse {
        private BigDecimal total;

        public TotalResponse(BigDecimal total) {
            this.total = total;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }
    }

    public static class CountResponse {
        private long count;

        public CountResponse(long count) {
            this.count = count;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    public static class ExistsResponse {
        private boolean exists;

        public ExistsResponse(boolean exists) {
            this.exists = exists;
        }

        public boolean isExists() {
            return exists;
        }

        public void setExists(boolean exists) {
            this.exists = exists;
        }
    }
}
