package com.example.msalmacen.controller;


import com.example.msalmacen.dto.StockAlmacenDTO;
import com.example.msalmacen.service.StockAlmacenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock-almacen")
@CrossOrigin(origins = "*")
public class StockAlmacenController {

    @Autowired
    private StockAlmacenService stockAlmacenService;

    // ============ CONSULTAS GENERALES ============

    @GetMapping
    public ResponseEntity<List<StockAlmacenDTO>> obtenerTodosLosStocks() {
        try {
            List<StockAlmacenDTO> stocks = stockAlmacenService.obtenerTodosLosStocks();
            return ResponseEntity.ok(stocks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/almacen/{almacenId}")
    public ResponseEntity<List<StockAlmacenDTO>> obtenerStocksPorAlmacen(@PathVariable Long almacenId) {
        try {
            List<StockAlmacenDTO> stocks = stockAlmacenService.obtenerStocksPorAlmacen(almacenId);
            return ResponseEntity.ok(stocks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/materia-prima/{materiaPrimaId}")
    public ResponseEntity<List<StockAlmacenDTO>> obtenerStocksPorMateriaPrima(@PathVariable Long materiaPrimaId) {
        try {
            List<StockAlmacenDTO> stocks = stockAlmacenService.obtenerStocksPorMateriaPrima(materiaPrimaId);
            return ResponseEntity.ok(stocks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/materia-prima/{materiaPrimaId}/almacen/{almacenId}")
    public ResponseEntity<StockAlmacenDTO> obtenerStockEspecifico(
            @PathVariable Long materiaPrimaId,
            @PathVariable Long almacenId) {
        try {
            Optional<StockAlmacenDTO> stock = stockAlmacenService.obtenerStockPorMateriaPrimaYAlmacen(materiaPrimaId, almacenId);
            return stock.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/con-existencias")
    public ResponseEntity<List<StockAlmacenDTO>> obtenerStocksConExistencias() {
        try {
            List<StockAlmacenDTO> stocks = stockAlmacenService.obtenerStocksConExistencias();
            return ResponseEntity.ok(stocks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/bajo-minimo")
    public ResponseEntity<List<StockAlmacenDTO>> obtenerStocksBajoMinimo() {
        try {
            List<StockAlmacenDTO> stocks = stockAlmacenService.obtenerStocksBajoMinimo();
            return ResponseEntity.ok(stocks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<StockAlmacenDTO>> buscarStocks(@RequestParam String q) {
        try {
            List<StockAlmacenDTO> stocks = stockAlmacenService.buscarStocks(q);
            return ResponseEntity.ok(stocks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============ OPERACIONES DE STOCK ============

    @PostMapping("/incrementar")
    public ResponseEntity<StockAlmacenDTO> incrementarStock(@RequestBody IncrementarStockRequest request) {
        try {
            StockAlmacenDTO stock = stockAlmacenService.incrementarStock(
                    request.getMateriaPrimaId(),
                    request.getAlmacenId(),
                    request.getCantidad()
            );
            return ResponseEntity.ok(stock);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/decrementar")
    public ResponseEntity<StockAlmacenDTO> decrementarStock(@RequestBody DecrementarStockRequest request) {
        try {
            StockAlmacenDTO stock = stockAlmacenService.decrementarStock(
                    request.getMateriaPrimaId(),
                    request.getAlmacenId(),
                    request.getCantidad()
            );
            return ResponseEntity.ok(stock);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/ajustar")
    public ResponseEntity<StockAlmacenDTO> ajustarStock(@RequestBody AjustarStockRequest request) {
        try {
            StockAlmacenDTO stock = stockAlmacenService.ajustarStock(
                    request.getMateriaPrimaId(),
                    request.getAlmacenId(),
                    request.getNuevoStock()
            );
            return ResponseEntity.ok(stock);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/transferir")
    public ResponseEntity<String> transferirStock(@RequestBody TransferirStockRequest request) {
        try {
            stockAlmacenService.transferirStock(
                    request.getMateriaPrimaId(),
                    request.getAlmacenOrigenId(),
                    request.getAlmacenDestinoId(),
                    request.getCantidad()
            );
            return ResponseEntity.ok("Transferencia realizada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    // ============ CONSULTAS ESPECIALES ============

    @GetMapping("/verificar-stock")
    public ResponseEntity<Boolean> verificarStockSuficiente(
            @RequestParam Long materiaPrimaId,
            @RequestParam Long almacenId,
            @RequestParam BigDecimal cantidad) {
        try {
            boolean suficiente = stockAlmacenService.verificarStockSuficiente(materiaPrimaId, almacenId, cantidad);
            return ResponseEntity.ok(suficiente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/total-materia-prima/{materiaPrimaId}")
    public ResponseEntity<BigDecimal> obtenerStockTotalMateriaPrima(@PathVariable Long materiaPrimaId) {
        try {
            BigDecimal stockTotal = stockAlmacenService.obtenerStockTotalMateriaPrima(materiaPrimaId);
            return ResponseEntity.ok(stockTotal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/actual")
    public ResponseEntity<BigDecimal> obtenerStockActual(
            @RequestParam Long materiaPrimaId,
            @RequestParam Long almacenId) {
        try {
            BigDecimal stockActual = stockAlmacenService.obtenerStockActual(materiaPrimaId, almacenId);
            return ResponseEntity.ok(stockActual);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ============ OPERACIONES DE MANTENIMIENTO ============

    @DeleteMapping("/limpiar-ceros")
    public ResponseEntity<String> eliminarStocksCero() {
        try {
            stockAlmacenService.eliminarStocksCero();
            return ResponseEntity.ok("Stocks con valor cero eliminados exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar stocks cero");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarStock(@PathVariable Long id) {
        try {
            stockAlmacenService.eliminarStock(id);
            return ResponseEntity.ok("Stock eliminado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar stock");
        }
    }

    // ============ CLASES INTERNAS PARA REQUESTS ============

    public static class IncrementarStockRequest {
        private Long materiaPrimaId;
        private Long almacenId;
        private BigDecimal cantidad;

        // Getters y Setters
        public Long getMateriaPrimaId() { return materiaPrimaId; }
        public void setMateriaPrimaId(Long materiaPrimaId) { this.materiaPrimaId = materiaPrimaId; }
        public Long getAlmacenId() { return almacenId; }
        public void setAlmacenId(Long almacenId) { this.almacenId = almacenId; }
        public BigDecimal getCantidad() { return cantidad; }
        public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    }

    public static class DecrementarStockRequest {
        private Long materiaPrimaId;
        private Long almacenId;
        private BigDecimal cantidad;

        // Getters y Setters
        public Long getMateriaPrimaId() { return materiaPrimaId; }
        public void setMateriaPrimaId(Long materiaPrimaId) { this.materiaPrimaId = materiaPrimaId; }
        public Long getAlmacenId() { return almacenId; }
        public void setAlmacenId(Long almacenId) { this.almacenId = almacenId; }
        public BigDecimal getCantidad() { return cantidad; }
        public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    }

    public static class AjustarStockRequest {
        private Long materiaPrimaId;
        private Long almacenId;
        private BigDecimal nuevoStock;

        // Getters y Setters
        public Long getMateriaPrimaId() { return materiaPrimaId; }
        public void setMateriaPrimaId(Long materiaPrimaId) { this.materiaPrimaId = materiaPrimaId; }
        public Long getAlmacenId() { return almacenId; }
        public void setAlmacenId(Long almacenId) { this.almacenId = almacenId; }
        public BigDecimal getNuevoStock() { return nuevoStock; }
        public void setNuevoStock(BigDecimal nuevoStock) { this.nuevoStock = nuevoStock; }
    }

    public static class TransferirStockRequest {
        private Long materiaPrimaId;
        private Long almacenOrigenId;
        private Long almacenDestinoId;
        private BigDecimal cantidad;

        // Getters y Setters
        public Long getMateriaPrimaId() { return materiaPrimaId; }
        public void setMateriaPrimaId(Long materiaPrimaId) { this.materiaPrimaId = materiaPrimaId; }
        public Long getAlmacenOrigenId() { return almacenOrigenId; }
        public void setAlmacenOrigenId(Long almacenOrigenId) { this.almacenOrigenId = almacenOrigenId; }
        public Long getAlmacenDestinoId() { return almacenDestinoId; }
        public void setAlmacenDestinoId(Long almacenDestinoId) { this.almacenDestinoId = almacenDestinoId; }
        public BigDecimal getCantidad() { return cantidad; }
        public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }
    }
}