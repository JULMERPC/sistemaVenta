package com.example.ms.venta.controller;

import com.example.ms.venta.dto.VentaDto;
import com.example.ms.venta.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    // Crear venta
    @PostMapping
    public ResponseEntity<?> crearVenta(@Valid @RequestBody VentaDto ventaDto) {
        try {
            VentaDto ventaCreada = ventaService.crearVenta(ventaDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaCreada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Obtener todas las ventas
    @GetMapping
    public ResponseEntity<List<VentaDto>> obtenerTodasLasVentas() {
        List<VentaDto> ventas = ventaService.obtenerTodasLasVentas();
        return ResponseEntity.ok(ventas);
    }

    // Obtener venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerVentaPorId(@PathVariable Long id) {
        Optional<VentaDto> venta = ventaService.obtenerVentaPorId(id);
        if (venta.isPresent()) {
            return ResponseEntity.ok(venta.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Obtener ventas por cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VentaDto>> obtenerVentasPorCliente(@PathVariable Long clienteId) {
        List<VentaDto> ventas = ventaService.obtenerVentasPorCliente(clienteId);
        return ResponseEntity.ok(ventas);
    }

    // Obtener ventas por rango de fechas
    @GetMapping("/fechas")
    public ResponseEntity<List<VentaDto>> obtenerVentasPorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<VentaDto> ventas = ventaService.obtenerVentasPorFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(ventas);
    }

    // Obtener ventas por forma de pago
    @GetMapping("/forma-pago/{formaPago}")
    public ResponseEntity<List<VentaDto>> obtenerVentasPorFormaPago(@PathVariable String formaPago) {
        List<VentaDto> ventas = ventaService.obtenerVentasPorFormaPago(formaPago);
        return ResponseEntity.ok(ventas);
    }

    // Obtener ventas del día actual
    @GetMapping("/hoy")
    public ResponseEntity<List<VentaDto>> obtenerVentasHoy() {
        List<VentaDto> ventas = ventaService.obtenerVentasHoy();
        return ResponseEntity.ok(ventas);
    }

    // Actualizar venta
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarVenta(@PathVariable Long id,
                                             @Valid @RequestBody VentaDto ventaDto) {
        try {
            VentaDto ventaActualizada = ventaService.actualizarVenta(id, ventaDto);
            return ResponseEntity.ok(ventaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Eliminar venta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarVenta(@PathVariable Long id) {
        try {
            ventaService.eliminarVenta(id);
            return ResponseEntity.ok().body("Venta eliminada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Verificar si existe venta
    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existeVenta(@PathVariable Long id) {
        boolean existe = ventaService.existeVenta(id);
        return ResponseEntity.ok(existe);
    }

    // Endpoints de reportes y estadísticas

    // Obtener total de ventas por cliente
    @GetMapping("/cliente/{clienteId}/total")
    public ResponseEntity<BigDecimal> obtenerTotalVentasPorCliente(@PathVariable Long clienteId) {
        BigDecimal total = ventaService.obtenerTotalVentasPorCliente(clienteId);
        return ResponseEntity.ok(total);
    }

    // Obtener total de ventas por fecha
    @GetMapping("/fecha/{fecha}/total")
    public ResponseEntity<BigDecimal> obtenerTotalVentasPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        BigDecimal total = ventaService.obtenerTotalVentasPorFecha(fecha);
        return ResponseEntity.ok(total);
    }

    // Obtener total de ventas por rango de fechas
    @GetMapping("/fechas/total")
    public ResponseEntity<BigDecimal> obtenerTotalVentasPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        BigDecimal total = ventaService.obtenerTotalVentasPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(total);
    }

    // Contar ventas por cliente
    @GetMapping("/cliente/{clienteId}/count")
    public ResponseEntity<Long> contarVentasPorCliente(@PathVariable Long clienteId) {
        long count = ventaService.contarVentasPorCliente(clienteId);
        return ResponseEntity.ok(count);
    }

    // Endpoint para obtener estadísticas generales
//    @GetMapping("/estadisticas")
//    public ResponseEntity<?> obtenerEstadisticas() {
//        try {
//            List<VentaDto> ventas = ventaService.obtenerTodasLasVentas();
//            long totalVentas = ventas.size();
//
//            BigDecimal montoTotal = ventas.stream()
//                    .map(VentaDto::getTotal)
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//            long ventasEfectivo = ventas.stream()
//                    .filter(v -> "efectivo".equalsIgnoreCase(v.getFormaPago()))
//                    .count();
//
//            long ventasTarjeta = ventas.stream()
//                    .filter(v -> v.getFormaPago() != null &&
//                            v.getFormaPago().toLowerCase().contains("tarjeta"))
//                    .count();
//
//            BigDecimal promedioVenta = totalVentas > 0 ?
//                    montoTotal.divide(BigDecimal.valueOf(totalVentas), 2, BigDecimal.ROUND_HALF_UP) :
//                    BigDecimal.ZERO;
//
//            return ResponseEntity.ok(new Object() {
//                public final long total = totalVentas;
//                public final BigDecimal montoTotal = montoTotal;
//                public final BigDecimal promedio = promedioVenta;
//                public final long efectivo = ventasEfectivo;
//                public final long tarjeta = ventasTarjeta;
//            });
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error al obtener estadísticas: " + e.getMessage());
//        }
//    }


    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        try {
            List<VentaDto> ventas = ventaService.obtenerTodasLasVentas();
            long totalVentas = ventas.size();

            BigDecimal montoTotalVentas = ventas.stream()
                    .map(VentaDto::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            long ventasEfectivo = ventas.stream()
                    .filter(v -> "efectivo".equalsIgnoreCase(v.getFormaPago()))
                    .count();

            long ventasTarjeta = ventas.stream()
                    .filter(v -> v.getFormaPago() != null &&
                            v.getFormaPago().toLowerCase().contains("tarjeta"))
                    .count();

            BigDecimal promedioVenta = totalVentas > 0 ?
                    montoTotalVentas.divide(BigDecimal.valueOf(totalVentas), 2, BigDecimal.ROUND_HALF_UP) :
                    BigDecimal.ZERO;

            return ResponseEntity.ok(new Object() {
                public final long total = totalVentas;
                public final BigDecimal montoTotal = montoTotalVentas;
                public final BigDecimal promedio = promedioVenta;
                public final long efectivo = ventasEfectivo;
                public final long tarjeta = ventasTarjeta;
            });
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener estadísticas: " + e.getMessage());
        }
    }


    // Endpoint para obtener resumen del día
    @GetMapping("/resumen-hoy")
    public ResponseEntity<?> obtenerResumenHoy() {
        try {
            List<VentaDto> ventasHoy = ventaService.obtenerVentasHoy();
            long totalVentasHoy = ventasHoy.size();

            BigDecimal montoTotalHoy = ventasHoy.stream()
                    .map(VentaDto::getTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return ResponseEntity.ok(new Object() {
                public final LocalDate fecha = LocalDate.now();
                public final long totalVentas = totalVentasHoy;
                public final BigDecimal montoTotal = montoTotalHoy;
            });
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener resumen de ventas de hoy: " + e.getMessage());
        }
    }




}