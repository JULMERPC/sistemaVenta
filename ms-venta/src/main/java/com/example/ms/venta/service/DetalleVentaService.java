package com.example.ms.venta.service;

import com.example.ms.venta.dto.DetalleVentaDto;
import com.example.ms.venta.entity.DetalleVenta;
import com.example.ms.venta.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    // Crear un nuevo detalle de venta
    public DetalleVentaDto crearDetalleVenta(DetalleVentaDto detalleVentaDto) {
        // Validar que no exista ya un detalle para la misma venta y materia prima
        if (detalleVentaRepository.existsByVentaIdAndMateriaPrimaId(
                detalleVentaDto.getVentaId(), detalleVentaDto.getMateriaPrimaId())) {
            throw new RuntimeException("Ya existe un detalle para esta venta y materia prima");
        }

        DetalleVenta detalleVenta = convertirDtoAEntity(detalleVentaDto);
        detalleVenta.calcularSubtotal(); // Asegurar que el subtotal esté calculado

        DetalleVenta detalleGuardado = detalleVentaRepository.save(detalleVenta);
        return convertirEntityADto(detalleGuardado);
    }

    // Obtener todos los detalles de venta
    @Transactional(readOnly = true)
    public List<DetalleVentaDto> obtenerTodosLosDetalles() {
        return detalleVentaRepository.findAll()
                .stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    // Obtener detalle por ID
    @Transactional(readOnly = true)
    public Optional<DetalleVentaDto> obtenerDetallePorId(Long id) {
        return detalleVentaRepository.findById(id)
                .map(this::convertirEntityADto);
    }

    // Obtener todos los detalles de una venta específica
    @Transactional(readOnly = true)
    public List<DetalleVentaDto> obtenerDetallesPorVenta(Long ventaId) {
        return detalleVentaRepository.findByVentaId(ventaId)
                .stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    // Obtener detalles por materia prima
    @Transactional(readOnly = true)
    public List<DetalleVentaDto> obtenerDetallesPorMateriaPrima(Long materiaPrimaId) {
        return detalleVentaRepository.findByMateriaPrimaId(materiaPrimaId)
                .stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    // Actualizar detalle de venta
    public DetalleVentaDto actualizarDetalleVenta(Long id, DetalleVentaDto detalleVentaDto) {
        Optional<DetalleVenta> detalleExistente = detalleVentaRepository.findById(id);

        if (detalleExistente.isEmpty()) {
            throw new RuntimeException("Detalle de venta no encontrado con ID: " + id);
        }

        DetalleVenta detalle = detalleExistente.get();

        // Actualizar campos
        if (detalleVentaDto.getCantidad() != null) {
            detalle.setCantidad(detalleVentaDto.getCantidad());
        }
        if (detalleVentaDto.getPrecioUnitario() != null) {
            detalle.setPrecioUnitario(detalleVentaDto.getPrecioUnitario());
        }

        // Recalcular subtotal
        detalle.calcularSubtotal();

        DetalleVenta detalleActualizado = detalleVentaRepository.save(detalle);
        return convertirEntityADto(detalleActualizado);
    }

    // Eliminar detalle de venta
    public void eliminarDetalleVenta(Long id) {
        if (!detalleVentaRepository.existsById(id)) {
            throw new RuntimeException("Detalle de venta no encontrado con ID: " + id);
        }
        detalleVentaRepository.deleteById(id);
    }

    // Eliminar todos los detalles de una venta
    public void eliminarDetallesPorVenta(Long ventaId) {
        detalleVentaRepository.deleteByVentaId(ventaId);
    }

    // Calcular el total de una venta
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalVenta(Long ventaId) {
        return detalleVentaRepository.calcularTotalVenta(ventaId);
    }

    // Contar detalles de una venta
    @Transactional(readOnly = true)
    public long contarDetallesPorVenta(Long ventaId) {
        return detalleVentaRepository.countByVentaId(ventaId);
    }

    // Verificar si existe detalle para venta y materia prima
    @Transactional(readOnly = true)
    public boolean existeDetalle(Long ventaId, Long materiaPrimaId) {
        return detalleVentaRepository.existsByVentaIdAndMateriaPrimaId(ventaId, materiaPrimaId);
    }

    // Crear múltiples detalles de venta
    public List<DetalleVentaDto> crearMultiplesDetalles(List<DetalleVentaDto> detallesDto) {
        List<DetalleVenta> detalles = detallesDto.stream()
                .map(this::convertirDtoAEntity)
                .peek(detalle -> detalle.calcularSubtotal())
                .collect(Collectors.toList());

        List<DetalleVenta> detallesGuardados = detalleVentaRepository.saveAll(detalles);

        return detallesGuardados.stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    // Obtener materias primas más vendidas
    @Transactional(readOnly = true)
    public List<Object[]> obtenerMateriasPrimasMasVendidas() {
        return detalleVentaRepository.findMateriasPrimasMasVendidas();
    }

    // Métodos de conversión
    private DetalleVentaDto convertirEntityADto(DetalleVenta detalleVenta) {
        return DetalleVentaDto.builder()
                .id(detalleVenta.getId())
                .ventaId(detalleVenta.getVentaId())
                .materiaPrimaId(detalleVenta.getMateriaPrimaId())
                .cantidad(detalleVenta.getCantidad())
                .precioUnitario(detalleVenta.getPrecioUnitario())
                .subtotal(detalleVenta.getSubtotal())
                .createdAt(detalleVenta.getCreatedAt())
                .updatedAt(detalleVenta.getUpdatedAt())
                .build();
    }

    private DetalleVenta convertirDtoAEntity(DetalleVentaDto detalleVentaDto) {
        return DetalleVenta.builder()
                .id(detalleVentaDto.getId())
                .ventaId(detalleVentaDto.getVentaId())
                .materiaPrimaId(detalleVentaDto.getMateriaPrimaId())
                .cantidad(detalleVentaDto.getCantidad())
                .precioUnitario(detalleVentaDto.getPrecioUnitario())
                .subtotal(detalleVentaDto.getSubtotal())
                .build();
    }
}