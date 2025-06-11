package com.example.ms.venta.service;

import com.example.ms.venta.client.MateriaPrimaClient;
import com.example.ms.venta.dto.DetalleVentaDto;
import com.example.ms.venta.dto.MateriaPrimaDTO;
import com.example.ms.venta.dto.VentaDto;
import com.example.ms.venta.entity.DetalleVenta;
import com.example.ms.venta.repository.DetalleVentaRepository;
import com.example.ms.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private MateriaPrimaClient materiaPrimaClient;  // Inyectar Feign Client

    @Autowired
    private VentaRepository ventaRepository;


    // Crear un nuevo detalle de venta con validación de materia prima
    public DetalleVentaDto crearDetalleVenta(DetalleVentaDto detalleVentaDto) {
        // Validar que la materia prima existe y está activa
        try {
            MateriaPrimaDTO materiaPrima = materiaPrimaClient.obtenerMateriaPrimaPorId(detalleVentaDto.getMateriaPrimaId());
            if (materiaPrima == null || !materiaPrima.getEstado()) {
                throw new RuntimeException("La materia prima no existe o está inactiva");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al validar materia prima: " + e.getMessage());
        }

        // Validar que no exista ya un detalle para la misma venta y materia prima
        if (detalleVentaRepository.existsByVentaIdAndMateriaPrimaId(
                detalleVentaDto.getVentaId(), detalleVentaDto.getMateriaPrimaId())) {
            throw new RuntimeException("Ya existe un detalle para esta venta y materia prima");
        }

        DetalleVenta detalleVenta = convertirDtoAEntity(detalleVentaDto);
        detalleVenta.calcularSubtotal();

        DetalleVenta detalleGuardado = detalleVentaRepository.save(detalleVenta);
        return convertirEntityADto(detalleGuardado);
    }
    // Crear un nuevo detalle de venta
//    public DetalleVentaDto crearDetalleVenta(DetalleVentaDto detalleVentaDto) {
//        // Validar que no exista ya un detalle para la misma venta y materia prima
//        if (detalleVentaRepository.existsByVentaIdAndMateriaPrimaId(
//                detalleVentaDto.getVentaId(), detalleVentaDto.getMateriaPrimaId())) {
//            throw new RuntimeException("Ya existe un detalle para esta venta y materia prima");
//        }
//
//        DetalleVenta detalleVenta = convertirDtoAEntity(detalleVentaDto);
//        detalleVenta.calcularSubtotal(); // Asegurar que el subtotal esté calculado
//
//        DetalleVenta detalleGuardado = detalleVentaRepository.save(detalleVenta);
//        return convertirEntityADto(detalleGuardado);
//    }

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
//    private DetalleVentaDto convertirEntityADto(DetalleVenta detalleVenta) {
//        return DetalleVentaDto.builder()
//                .id(detalleVenta.getId())
//                .ventaId(detalleVenta.getVentaId())
//                .materiaPrimaId(detalleVenta.getMateriaPrimaId())
//                .cantidad(detalleVenta.getCantidad())
//                .precioUnitario(detalleVenta.getPrecioUnitario())
//                .subtotal(detalleVenta.getSubtotal())
//                .createdAt(detalleVenta.getCreatedAt())
//                .updatedAt(detalleVenta.getUpdatedAt())
//                .build();
//    }

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


    private DetalleVentaDto convertirEntityADto(DetalleVenta detalleVenta) {
        DetalleVentaDto.DetalleVentaDtoBuilder builder = DetalleVentaDto.builder()
                .id(detalleVenta.getId())
                .ventaId(detalleVenta.getVentaId())
                .materiaPrimaId(detalleVenta.getMateriaPrimaId())
                .cantidad(detalleVenta.getCantidad())
                .precioUnitario(detalleVenta.getPrecioUnitario())
                .subtotal(detalleVenta.getSubtotal())
                .createdAt(detalleVenta.getCreatedAt())
                .updatedAt(detalleVenta.getUpdatedAt());

        try {
            MateriaPrimaDTO mp = materiaPrimaClient.obtenerMateriaPrimaPorId(detalleVenta.getMateriaPrimaId());
            builder
                    .materiaPrimaNombre(mp.getNombre())
                    .materiaPrimaDescripcion(mp.getDescripcion())
                    .materiaPrimaUnidadMedida(mp.getUnidadMedida());
        } catch (Exception e) {
            System.err.println("No se pudo obtener info de materia prima: " + e.getMessage());
        }
        // Obtener datos de venta
        // Obtener datos de venta
        try {
            VentaDto venta = ventaRepository.findVentaDtoById(detalleVenta.getVentaId());
            if (venta != null) {
                builder
                        .ventaFecha(venta.getFecha() != null ? venta.getFecha().toString() : null)
                        .ventaFormaPago(venta.getFormaPago());
            }
        } catch (Exception e) {
            System.err.println("No se pudo obtener info de venta: " + e.getMessage());
        }


        return builder.build();
    }
}