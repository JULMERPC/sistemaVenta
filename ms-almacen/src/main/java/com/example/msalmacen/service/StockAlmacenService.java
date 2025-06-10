
package com.example.msalmacen.service;

import com.example.msalmacen.dto.StockAlmacenDTO;
import com.example.msalmacen.entity.Almacen;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.entity.StockAlmacen;
import com.example.msalmacen.repository.AlmacenRepository;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import com.example.msalmacen.repository.StockAlmacenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StockAlmacenService {

    private final StockAlmacenRepository stockAlmacenRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;
    private final AlmacenRepository almacenRepository;

    // Crear o actualizar stock
    public StockAlmacenDTO crearOActualizarStock(StockAlmacenDTO stockDTO) {
        log.info("Creando o actualizando stock para materia prima {} en almacén {}",
                stockDTO.getIdMateriaPrima(), stockDTO.getIdAlmacen());

        // Verificar que existan la materia prima y el almacén
        MateriaPrima materiaPrima = materiaPrimaRepository.findById(stockDTO.getIdMateriaPrima())
                .orElseThrow(() -> new RuntimeException("Materia prima no encontrada"));

        Almacen almacen = almacenRepository.findById(stockDTO.getIdAlmacen())
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado"));

        // Buscar si ya existe el stock
        Optional<StockAlmacen> stockExistente = stockAlmacenRepository
                .findByMateriaPrimaIdAndAlmacenId(stockDTO.getIdMateriaPrima(), stockDTO.getIdAlmacen());

        StockAlmacen stock;
        if (stockExistente.isPresent()) {
            // Actualizar stock existente
            stock = stockExistente.get();
            stock.setStockActual(stockDTO.getStockActual());
            stock.setUltimaActualizacion(LocalDateTime.now());
        } else {
            // Crear nuevo stock
            stock = StockAlmacen.builder()
                    .materiaPrima(materiaPrima)
                    .almacen(almacen)
                    .stockActual(stockDTO.getStockActual())
                    .ultimaActualizacion(LocalDateTime.now())
                    .build();
        }

        stock = stockAlmacenRepository.save(stock);
        return convertirADTO(stock);
    }

    // Obtener stock por ID
    @Transactional(readOnly = true)
    public StockAlmacenDTO obtenerPorId(Long id) {
        StockAlmacen stock = stockAlmacenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado"));
        return convertirADTO(stock);
    }

    // Obtener stock por materia prima y almacén
    @Transactional(readOnly = true)
    public Optional<StockAlmacenDTO> obtenerStock(Long materiaPrimaId, Long almacenId) {
        return stockAlmacenRepository.findByMateriaPrimaIdAndAlmacenId(materiaPrimaId, almacenId)
                .map(this::convertirADTO);
    }

    // Obtener todos los stocks de un almacén
    @Transactional(readOnly = true)
    public List<StockAlmacenDTO> obtenerStocksPorAlmacen(Long almacenId) {
        List<StockAlmacen> stocks = stockAlmacenRepository.findByAlmacenIdWithDetails(almacenId);
        return stocks.stream().map(this::convertirADTO).toList();
    }

    // Obtener todos los stocks de una materia prima
    @Transactional(readOnly = true)
    public List<StockAlmacenDTO> obtenerStocksPorMateriaPrima(Long materiaPrimaId) {
        List<StockAlmacen> stocks = stockAlmacenRepository.findByMateriaPrimaId(materiaPrimaId);
        return stocks.stream().map(this::convertirADTO).toList();
    }

    // Obtener todos los stocks
    @Transactional(readOnly = true)
    public List<StockAlmacenDTO> obtenerTodosLosStocks() {
        List<StockAlmacen> stocks = stockAlmacenRepository.findAllWithDetails();
        return stocks.stream().map(this::convertirADTO).toList();
    }

    // Obtener stocks disponibles (> 0)
    @Transactional(readOnly = true)
    public List<StockAlmacenDTO> obtenerStocksDisponibles() {
        List<StockAlmacen> stocks = stockAlmacenRepository.findStocksDisponibles();
        return stocks.stream().map(this::convertirADTO).toList();
    }

    // Obtener stocks bajos
    @Transactional(readOnly = true)
    public List<StockAlmacenDTO> obtenerStocksBajos() {
        List<StockAlmacen> stocks = stockAlmacenRepository.findStocksBajos();
        return stocks.stream().map(this::convertirADTO).toList();
    }

    // Sumar stock (entrada)
    public StockAlmacenDTO sumarStock(Long materiaPrimaId, Long almacenId, BigDecimal cantidad) {
        log.info("Sumando {} al stock de materia prima {} en almacén {}",
                cantidad, materiaPrimaId, almacenId);

        StockAlmacen stock = stockAlmacenRepository
                .findByMateriaPrimaIdAndAlmacenId(materiaPrimaId, almacenId)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado"));

        stock.sumarStock(cantidad);
        stock = stockAlmacenRepository.save(stock);
        return convertirADTO(stock);
    }

    // Restar stock (salida)
    public StockAlmacenDTO restarStock(Long materiaPrimaId, Long almacenId, BigDecimal cantidad) {
        log.info("Restando {} del stock de materia prima {} en almacén {}",
                cantidad, materiaPrimaId, almacenId);

        StockAlmacen stock = stockAlmacenRepository
                .findByMateriaPrimaIdAndAlmacenId(materiaPrimaId, almacenId)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado"));

        if (stock.getStockActual().compareTo(cantidad) < 0) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + stock.getStockActual());
        }

        stock.restarStock(cantidad);
        stock = stockAlmacenRepository.save(stock);
        return convertirADTO(stock);
    }

    // Obtener stock total de una materia prima
    @Transactional(readOnly = true)
    public BigDecimal obtenerStockTotal(Long materiaPrimaId) {
        return stockAlmacenRepository.getTotalStockMateriaPrima(materiaPrimaId);
    }

    // Verificar si hay stock suficiente
    @Transactional(readOnly = true)
    public boolean hayStockSuficiente(Long materiaPrimaId, Long almacenId, BigDecimal cantidadRequerida) {
        Optional<StockAlmacen> stock = stockAlmacenRepository
                .findByMateriaPrimaIdAndAlmacenId(materiaPrimaId, almacenId);

        return stock.isPresent() &&
                stock.get().getStockActual().compareTo(cantidadRequerida) >= 0;
    }

    // Eliminar stock
    public void eliminarStock(Long id) {
        if (!stockAlmacenRepository.existsById(id)) {
            throw new RuntimeException("Stock no encontrado");
        }
        stockAlmacenRepository.deleteById(id);
        log.info("Stock eliminado con ID: {}", id);
    }

    // Método privado para convertir entidad a DTO
    private StockAlmacenDTO convertirADTO(StockAlmacen stock) {
        return StockAlmacenDTO.builder()
                .id(stock.getId())
                .idMateriaPrima(stock.getMateriaPrima().getId())
                .idAlmacen(stock.getAlmacen().getId())
                .stockActual(stock.getStockActual())
                .ultimaActualizacion(stock.getUltimaActualizacion())
                .nombreMateriaPrima(stock.getMateriaPrima().getNombre())
                .nombreAlmacen(stock.getAlmacen().getNombre())
                .unidadMedida(stock.getMateriaPrima().getUnidadMedida())
                .stockMinimo(stock.getMateriaPrima().getStockMinimo())
                .stockBajo(stock.stockBajo())
                .build();
    }
}