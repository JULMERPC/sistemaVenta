package com.example.msalmacen.service;


import com.example.msalmacen.dto.StockAlmacenDTO;
import com.example.msalmacen.entity.Almacen;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.entity.StockAlmacen;
import com.example.msalmacen.repository.StockAlmacenRepository;
import com.example.msalmacen.repository.AlmacenRepository;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockAlmacenService {

    @Autowired
    private StockAlmacenRepository stockAlmacenRepository;

    @Autowired
    private MateriaPrimaRepository materiaPrimaRepository;

    @Autowired
    private AlmacenRepository almacenRepository;

    // ============ MÉTODOS PRINCIPALES ============

    @Transactional(readOnly = true)
    public List<StockAlmacenDTO> obtenerTodosLosStocks() {
        return stockAlmacenRepository.findStocksActivosConDetalles()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StockAlmacenDTO> obtenerStocksPorAlmacen(Long almacenId) {
        return stockAlmacenRepository.findStocksConDetallesPorAlmacen(almacenId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StockAlmacenDTO> obtenerStocksPorMateriaPrima(Long materiaPrimaId) {
        return stockAlmacenRepository.findByMateriaPrimaId(materiaPrimaId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<StockAlmacenDTO> obtenerStockPorMateriaPrimaYAlmacen(Long materiaPrimaId, Long almacenId) {
        return stockAlmacenRepository.findByMateriaPrimaIdAndAlmacenId(materiaPrimaId, almacenId)
                .map(this::convertirADTO);
    }

    @Transactional(readOnly = true)
    public List<StockAlmacenDTO> obtenerStocksConExistencias() {
        return stockAlmacenRepository.findStocksConDetallesCompletos()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StockAlmacenDTO> obtenerStocksBajoMinimo() {
        return stockAlmacenRepository.findStocksBajoMinimo()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StockAlmacenDTO> buscarStocks(String busqueda) {
        return stockAlmacenRepository.buscarStocksPorMateriaPrima(busqueda)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ============ OPERACIONES DE STOCK ============

    public StockAlmacenDTO incrementarStock(Long materiaPrimaId, Long almacenId, BigDecimal cantidad) {
        validarCantidad(cantidad);

        StockAlmacen stock = obtenerOCrearStock(materiaPrimaId, almacenId);
        stock.incrementarStock(cantidad);

        StockAlmacen stockGuardado = stockAlmacenRepository.save(stock);
        return convertirADTO(stockGuardado);
    }

    public StockAlmacenDTO decrementarStock(Long materiaPrimaId, Long almacenId, BigDecimal cantidad) {
        validarCantidad(cantidad);

        StockAlmacen stock = stockAlmacenRepository.findByMateriaPrimaIdAndAlmacenId(materiaPrimaId, almacenId)
                .orElseThrow(() -> new RuntimeException("No existe stock para la materia prima " + materiaPrimaId + " en el almacén " + almacenId));

        if (!stock.tieneStockSuficiente(cantidad)) {
            throw new RuntimeException("Stock insuficiente. Stock actual: " + stock.getStockActual() + ", cantidad solicitada: " + cantidad);
        }

        stock.decrementarStock(cantidad);
        StockAlmacen stockGuardado = stockAlmacenRepository.save(stock);
        return convertirADTO(stockGuardado);
    }

    public StockAlmacenDTO ajustarStock(Long materiaPrimaId, Long almacenId, BigDecimal nuevoStock) {
        if (nuevoStock == null || nuevoStock.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        StockAlmacen stock = obtenerOCrearStock(materiaPrimaId, almacenId);
        stock.setStockActual(nuevoStock);

        StockAlmacen stockGuardado = stockAlmacenRepository.save(stock);
        return convertirADTO(stockGuardado);
    }

    public void transferirStock(Long materiaPrimaId, Long almacenOrigenId, Long almacenDestinoId, BigDecimal cantidad) {
        validarCantidad(cantidad);

        // Decrementar del origen
        decrementarStock(materiaPrimaId, almacenOrigenId, cantidad);

        // Incrementar en el destino
        incrementarStock(materiaPrimaId, almacenDestinoId, cantidad);
    }

    // ============ CONSULTAS ESPECIALES ============

    @Transactional(readOnly = true)
    public boolean verificarStockSuficiente(Long materiaPrimaId, Long almacenId, BigDecimal cantidadRequerida) {
        Boolean resultado = stockAlmacenRepository.existeStockSuficiente(materiaPrimaId, almacenId, cantidadRequerida);
        return resultado != null && resultado;
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerStockTotalMateriaPrima(Long materiaPrimaId) {
        return stockAlmacenRepository.obtenerStockTotalMateriaPrima(materiaPrimaId);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerStockActual(Long materiaPrimaId, Long almacenId) {
        return stockAlmacenRepository.findByMateriaPrimaIdAndAlmacenId(materiaPrimaId, almacenId)
                .map(StockAlmacen::getStockActual)
                .orElse(BigDecimal.ZERO);
    }

    // ============ OPERACIONES DE MANTENIMIENTO ============

    public void eliminarStocksCero() {
        stockAlmacenRepository.eliminarStocksCero();
    }

    public void eliminarStock(Long id) {
        stockAlmacenRepository.deleteById(id);
    }

    // ============ MÉTODOS AUXILIARES ============

    private StockAlmacen obtenerOCrearStock(Long materiaPrimaId, Long almacenId) {
        return stockAlmacenRepository.findByMateriaPrimaIdAndAlmacenId(materiaPrimaId, almacenId)
                .orElseGet(() -> crearNuevoStock(materiaPrimaId, almacenId));
    }

    private StockAlmacen crearNuevoStock(Long materiaPrimaId, Long almacenId) {
        MateriaPrima materiaPrima = materiaPrimaRepository.findById(materiaPrimaId)
                .orElseThrow(() -> new RuntimeException("Materia prima no encontrada: " + materiaPrimaId));

        Almacen almacen = almacenRepository.findById(almacenId)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado: " + almacenId));

        return StockAlmacen.builder()
                .materiaPrima(materiaPrima)
                .almacen(almacen)
                .stockActual(BigDecimal.ZERO)
                .ultimaActualizacion(LocalDateTime.now())
                .build();
    }

    private void validarCantidad(BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
    }

    private StockAlmacenDTO convertirADTO(StockAlmacen stock) {
        return StockAlmacenDTO.builder()
                .id(stock.getId())
                .idMateriaPrima(stock.getMateriaPrima().getId())
                .idAlmacen(stock.getAlmacen().getId())
                .stockActual(stock.getStockActual())
                .ultimaActualizacion(stock.getUltimaActualizacion())
                .codigoMateriaPrima(stock.getMateriaPrima().getCodigo())
                .nombreMateriaPrima(stock.getMateriaPrima().getNombre())
                .nombreAlmacen(stock.getAlmacen().getNombre())
                .ubicacionAlmacen(stock.getAlmacen().getUbicacion())
                .unidadMedida(stock.getMateriaPrima().getUnidad())
                .stockMinimoMateriaPrima(stock.getMateriaPrima().getStockMinimo())
                .build();
    }
}