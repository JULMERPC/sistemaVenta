
package com.example.msalmacen.controller;

import com.example.msalmacen.dto.StockAlmacenDTO;
import com.example.msalmacen.service.StockAlmacenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock-almacen")
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*")
public class StockAlmacenController {

    private final StockAlmacenService stockAlmacenService;

    // Crear o actualizar stock
    @PostMapping
    public ResponseEntity<StockAlmacenDTO> crearOActualizarStock(@Valid @RequestBody StockAlmacenDTO stockDTO) {
        try {
            StockAlmacenDTO stockCreado = stockAlmacenService.crearOActualizarStock(stockDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(stockCreado);
        } catch (RuntimeException e) {
            log.error("Error al crear/actualizar stock: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener stock por ID
    @GetMapping("/{id}")
    public ResponseEntity<StockAlmacenDTO> obtenerStockPorId(@PathVariable Long id) {
        try {
            StockAlmacenDTO stock = stockAlmacenService.obtenerPorId(id);
            return ResponseEntity.ok(stock);
        } catch (RuntimeException e) {
            log.error("Stock no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener stock específico por materia prima y almacén
    @GetMapping("/materia-prima/{materiaPrimaId}/almacen/{almacenId}")
    public ResponseEntity<StockAlmacenDTO> obtenerStock(
            @PathVariable Long materiaPrimaId,
            @PathVariable Long almacenId) {

        Optional<StockAlmacenDTO> stock = stockAlmacenService.obtenerStock(materiaPrimaId, almacenId);
        return stock.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener todos los stocks de un almacén
    @GetMapping("/almacen/{almacenId}")
    public ResponseEntity<List<StockAlmacenDTO>> obtenerStocksPorAlmacen(@PathVariable Long almacenId) {
        List<StockAlmacenDTO> stocks = stockAlmacenService.obtenerStocksPorAlmacen(almacenId);
        return ResponseEntity.ok(stocks);
    }

    // Obtener todos los stocks de una materia prima
    @GetMapping("/materia-prima/{materiaPrimaId}")
    public ResponseEntity<List<StockAlmacenDTO>> obtenerStocksPorMateriaPrima(@PathVariable Long materiaPrimaId) {
        List<StockAlmacenDTO> stocks = stockAlmacenService.obtenerStocksPorMateriaPrima(materiaPrimaId);
        return ResponseEntity.ok(stocks);
    }

    // Obtener todos los stocks
    @GetMapping
    public ResponseEntity<List<StockAlmacenDTO>> obtenerTodosLosStocks() {
        List<StockAlmacenDTO> stocks = stockAlmacenService.obtenerTodosLosStocks();
        return ResponseEntity.ok(stocks);
    }

    // Obtener stocks disponibles (con stock > 0)
    @GetMapping("/disponibles")
    public ResponseEntity<List<StockAlmacenDTO>> obtenerStocksDisponibles() {
        List<StockAlmacenDTO> stocks = stockAlmacenService.obtenerStocksDisponibles();
        return ResponseEntity.ok(stocks);
    }

    // Obtener stocks bajos (stock actual < stock mínimo)
    @GetMapping("/bajos")
    public ResponseEntity<List<StockAlmacenDTO>> obtenerStocksBajos() {
        List<StockAlmacenDTO> stocks = stockAlmacenService.obtenerStocksBajos();
        return ResponseEntity.ok(stocks);
    }

    // Sumar stock (entrada de mercancía)
    @PutMapping("/materia-prima/{materiaPrimaId}/almacen/{almacenId}/sumar")
    public ResponseEntity<StockAlmacenDTO> sumarStock(
            @PathVariable Long materiaPrimaId,
            @PathVariable Long almacenId,
            @RequestBody @Valid CantidadRequest cantidad) {

        try {
            StockAlmacenDTO stock = stockAlmacenService.sumarStock(
                    materiaPrimaId, almacenId, cantidad.getCantidad());
            return ResponseEntity.ok(stock);
        } catch (RuntimeException e) {
            log.error("Error al sumar stock: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Restar stock (salida de mercancía)
    @PutMapping("/materia-prima/{materiaPrimaId}/almacen/{almacenId}/restar")
    public ResponseEntity<StockAlmacenDTO> restarStock(
            @PathVariable Long materiaPrimaId,
            @PathVariable Long almacenId,
            @RequestBody @Valid CantidadRequest cantidad) {

        try {
            StockAlmacenDTO stock = stockAlmacenService.restarStock(
                    materiaPrimaId, almacenId, cantidad.getCantidad());
            return ResponseEntity.ok(stock);
        } catch (RuntimeException e) {
            log.error("Error al restar stock: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    // Obtener stock total de una materia prima en todos los almacenes
    @GetMapping("/materia-prima/{materiaPrimaId}/total")
    public ResponseEntity<StockTotalResponse> obtenerStockTotal(@PathVariable Long materiaPrimaId) {
        BigDecimal stockTotal = stockAlmacenService.obtenerStockTotal(materiaPrimaId);
        return ResponseEntity.ok(new StockTotalResponse(stockTotal));
    }

    // Verificar si hay stock suficiente
    @GetMapping("/materia-prima/{materiaPrimaId}/almacen/{almacenId}/verificar")
    public ResponseEntity<StockSuficienteResponse> verificarStockSuficiente(
            @PathVariable Long materiaPrimaId,
            @PathVariable Long almacenId,
            @RequestParam @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor a 0") BigDecimal cantidad) {

        boolean suficiente = stockAlmacenService.hayStockSuficiente(materiaPrimaId, almacenId, cantidad);
        return ResponseEntity.ok(new StockSuficienteResponse(suficiente, cantidad));
    }

    // Eliminar stock
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarStock(@PathVariable Long id) {
        try {
            stockAlmacenService.eliminarStock(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error al eliminar stock: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    // DTOs internos para requests y responses
    public static class CantidadRequest {
        @NotNull(message = "La cantidad es obligatoria")
        @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor a 0")
        private BigDecimal cantidad;

        public CantidadRequest() {}

        public CantidadRequest(BigDecimal cantidad) {
            this.cantidad = cantidad;
        }

        public BigDecimal getCantidad() {
            return cantidad;
        }

        public void setCantidad(BigDecimal cantidad) {
            this.cantidad = cantidad;
        }
    }

    public static class StockTotalResponse {
        private BigDecimal stockTotal;

        public StockTotalResponse() {}

        public StockTotalResponse(BigDecimal stockTotal) {
            this.stockTotal = stockTotal;
        }

        public BigDecimal getStockTotal() {
            return stockTotal;
        }

        public void setStockTotal(BigDecimal stockTotal) {
            this.stockTotal = stockTotal;
        }
    }

    public static class StockSuficienteResponse {
        private Boolean suficiente;
        private BigDecimal cantidadConsultada;

        public StockSuficienteResponse() {}

        public StockSuficienteResponse(Boolean suficiente, BigDecimal cantidadConsultada) {
            this.suficiente = suficiente;
            this.cantidadConsultada = cantidadConsultada;
        }

        public Boolean getSuficiente() {
            return suficiente;
        }

        public void setSuficiente(Boolean suficiente) {
            this.suficiente = suficiente;
        }

        public BigDecimal getCantidadConsultada() {
            return cantidadConsultada;
        }

        public void setCantidadConsultada(BigDecimal cantidadConsultada) {
            this.cantidadConsultada = cantidadConsultada;
        }
    }
}