package com.example.ms.venta.service;

import com.example.ms.venta.dto.VentaDto;
import com.example.ms.venta.entity.Venta;
import com.example.ms.venta.repository.VentaRepository;
import com.example.ms.venta.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;

    // Crear venta
    public VentaDto crearVenta(VentaDto ventaDto) {
        // Validar que el cliente existe
        if (!clienteRepository.existsById(ventaDto.getClienteId())) {
            throw new RuntimeException("Cliente no encontrado con ID: " + ventaDto.getClienteId());
        }

        // Validar total positivo
        if (ventaDto.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El total debe ser mayor a 0");
        }

        Venta venta = convertirDtoAEntity(ventaDto);
        Venta ventaGuardada = ventaRepository.save(venta);
        return convertirEntityADto(ventaGuardada);
    }

    // Obtener todas las ventas
    @Transactional(readOnly = true)
    public List<VentaDto> obtenerTodasLasVentas() {
        return ventaRepository.findAllByOrderByFechaDescCreatedAtDesc()
                .stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    // Obtener venta por ID
    @Transactional(readOnly = true)
    public Optional<VentaDto> obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id)
                .map(this::convertirEntityADto);
    }

    // Obtener ventas por cliente
    @Transactional(readOnly = true)
    public List<VentaDto> obtenerVentasPorCliente(Long clienteId) {
        return ventaRepository.findByClienteId(clienteId)
                .stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    // Obtener ventas por rango de fechas
    @Transactional(readOnly = true)
    public List<VentaDto> obtenerVentasPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return ventaRepository.findByFechaBetween(fechaInicio, fechaFin)
                .stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    // Obtener ventas por forma de pago
    @Transactional(readOnly = true)
    public List<VentaDto> obtenerVentasPorFormaPago(String formaPago) {
        return ventaRepository.findByFormaPago(formaPago)
                .stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    // Obtener ventas del día actual
    @Transactional(readOnly = true)
    public List<VentaDto> obtenerVentasHoy() {
        return ventaRepository.findVentasHoy()
                .stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    // Actualizar venta
    public VentaDto actualizarVenta(Long id, VentaDto ventaDto) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));

        // Validar que el cliente existe
        if (!clienteRepository.existsById(ventaDto.getClienteId())) {
            throw new RuntimeException("Cliente no encontrado con ID: " + ventaDto.getClienteId());
        }

        // Validar total positivo
        if (ventaDto.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El total debe ser mayor a 0");
        }

        // Actualizar datos
        venta.setClienteId(ventaDto.getClienteId());
        venta.setFecha(ventaDto.getFecha());
        venta.setTotal(ventaDto.getTotal());
        venta.setFormaPago(ventaDto.getFormaPago());

        Venta ventaActualizada = ventaRepository.save(venta);
        return convertirEntityADto(ventaActualizada);
    }

    // Eliminar venta
    public void eliminarVenta(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
        ventaRepository.deleteById(id);
    }

    // Obtener total de ventas por cliente
    @Transactional(readOnly = true)
    public BigDecimal obtenerTotalVentasPorCliente(Long clienteId) {
        BigDecimal total = ventaRepository.sumTotalByClienteId(clienteId);
        return total != null ? total : BigDecimal.ZERO;
    }

    // Obtener total de ventas por fecha
    @Transactional(readOnly = true)
    public BigDecimal obtenerTotalVentasPorFecha(LocalDate fecha) {
        BigDecimal total = ventaRepository.sumTotalByFecha(fecha);
        return total != null ? total : BigDecimal.ZERO;
    }

    // Obtener total de ventas por rango de fechas
    @Transactional(readOnly = true)
    public BigDecimal obtenerTotalVentasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        BigDecimal total = ventaRepository.sumTotalByFechaBetween(fechaInicio, fechaFin);
        return total != null ? total : BigDecimal.ZERO;
    }

    // Contar ventas por cliente
    @Transactional(readOnly = true)
    public long contarVentasPorCliente(Long clienteId) {
        return ventaRepository.countByClienteId(clienteId);
    }

    // Verificar si existe venta
    @Transactional(readOnly = true)
    public boolean existeVenta(Long id) {
        return ventaRepository.existsById(id);
    }

    // Métodos de conversión
    private VentaDto convertirEntityADto(Venta venta) {
        VentaDto ventaDto = VentaDto.builder()
                .id(venta.getId())
                .clienteId(venta.getClienteId())
                .fecha(venta.getFecha())
                .total(venta.getTotal())
                .formaPago(venta.getFormaPago())
                .createdAt(venta.getCreatedAt())
                .updatedAt(venta.getUpdatedAt())
                .build();

        // Opcionalmente, obtener información del cliente
        clienteRepository.findById(venta.getClienteId()).ifPresent(cliente -> {
            ventaDto.setClienteNombre(cliente.getNombre());
            ventaDto.setClienteApellido(cliente.getApellido());
            ventaDto.setClienteDni(cliente.getDni());
        });

        return ventaDto;
    }

    private Venta convertirDtoAEntity(VentaDto ventaDto) {
        return Venta.builder()
                .id(ventaDto.getId())
                .clienteId(ventaDto.getClienteId())
                .fecha(ventaDto.getFecha())
                .total(ventaDto.getTotal())
                .formaPago(ventaDto.getFormaPago())
                .build();
    }
}