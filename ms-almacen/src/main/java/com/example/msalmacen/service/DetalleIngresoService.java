package com.example.msalmacen.service;


import com.example.msalmacen.dto.DetalleIngresoDTO;
import com.example.msalmacen.entity.DetalleIngreso;
import com.example.msalmacen.entity.IngresoMateriaPrima;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.repository.DetalleIngresoRepository;
import com.example.msalmacen.repository.IngresoMateriaPrimaRepository;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DetalleIngresoService {

    private final DetalleIngresoRepository detalleIngresoRepository;
    private final IngresoMateriaPrimaRepository ingresoMateriaPrimaRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;

    // Crear un nuevo detalle de ingreso
    public DetalleIngresoDTO crearDetalleIngreso(DetalleIngresoDTO detalleIngresoDTO) {
        // Validar que el ingreso existe
        IngresoMateriaPrima ingreso = ingresoMateriaPrimaRepository.findById(detalleIngresoDTO.getIdIngreso())
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado con ID: " + detalleIngresoDTO.getIdIngreso()));

        // Validar que la materia prima existe
        MateriaPrima materiaPrima = materiaPrimaRepository.findById(detalleIngresoDTO.getIdMateriaPrima())
                .orElseThrow(() -> new RuntimeException("Materia prima no encontrada con ID: " + detalleIngresoDTO.getIdMateriaPrima()));

        // Verificar que no exista ya un detalle para esta combinación
        if (detalleIngresoRepository.existsByIngresoMateriaPrimaIdAndMateriaPrimaId(
                detalleIngresoDTO.getIdIngreso(), detalleIngresoDTO.getIdMateriaPrima())) {
            throw new RuntimeException("Ya existe un detalle para esta materia prima en este ingreso");
        }

        // Crear la entidad
        DetalleIngreso detalleIngreso = DetalleIngreso.builder()
                .ingresoMateriaPrima(ingreso)
                .materiaPrima(materiaPrima)
                .cantidad(detalleIngresoDTO.getCantidad())
                .costoUnitario(detalleIngresoDTO.getCostoUnitario())
                .build();

        // Guardar
        DetalleIngreso detalleGuardado = detalleIngresoRepository.save(detalleIngreso);

        // Actualizar stock de materia prima
        actualizarStockMateriaPrima(materiaPrima, detalleIngresoDTO.getCantidad());

        return convertirADTO(detalleGuardado);
    }

    // Obtener todos los detalles
    @Transactional(readOnly = true)
    public List<DetalleIngresoDTO> obtenerTodosLosDetalles() {
        return detalleIngresoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener detalle por ID
    @Transactional(readOnly = true)
    public Optional<DetalleIngresoDTO> obtenerDetallePorId(Long id) {
        return detalleIngresoRepository.findById(id)
                .map(this::convertirADTO);
    }

    // Obtener detalles por ID de ingreso
    @Transactional(readOnly = true)
    public List<DetalleIngresoDTO> obtenerDetallesPorIngreso(Long idIngreso) {
        return detalleIngresoRepository.findByIngresoMateriaPrimaId(idIngreso).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener detalles por ID de materia prima
    @Transactional(readOnly = true)
    public List<DetalleIngresoDTO> obtenerDetallesPorMateriaPrima(Long idMateriaPrima) {
        return detalleIngresoRepository.findByMateriaPrimaId(idMateriaPrima).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Actualizar detalle de ingreso
    public DetalleIngresoDTO actualizarDetalleIngreso(Long id, DetalleIngresoDTO detalleIngresoDTO) {
        DetalleIngreso detalleExistente = detalleIngresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de ingreso no encontrado con ID: " + id));

        // Guardar cantidad anterior para ajustar stock
        BigDecimal cantidadAnterior = detalleExistente.getCantidad();

        // Actualizar campos
        detalleExistente.setCantidad(detalleIngresoDTO.getCantidad());
        detalleExistente.setCostoUnitario(detalleIngresoDTO.getCostoUnitario());

        // Guardar cambios
        DetalleIngreso detalleActualizado = detalleIngresoRepository.save(detalleExistente);

        // Ajustar stock de materia prima
        BigDecimal diferenciaCantidad = detalleIngresoDTO.getCantidad().subtract(cantidadAnterior);
        if (diferenciaCantidad.compareTo(BigDecimal.ZERO) != 0) {
            actualizarStockMateriaPrima(detalleExistente.getMateriaPrima(), diferenciaCantidad);
        }

        return convertirADTO(detalleActualizado);
    }

    // Eliminar detalle de ingreso
    public void eliminarDetalleIngreso(Long id) {
        DetalleIngreso detalle = detalleIngresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de ingreso no encontrado con ID: " + id));

        // Restar la cantidad del stock
        BigDecimal cantidadARestar = detalle.getCantidad().negate();
        actualizarStockMateriaPrima(detalle.getMateriaPrima(), cantidadARestar);

        detalleIngresoRepository.delete(detalle);
    }

    // Obtener total de un ingreso
    @Transactional(readOnly = true)
    public Double obtenerTotalIngreso(Long idIngreso) {
        Double total = detalleIngresoRepository.getTotalIngreso(idIngreso);
        return total != null ? total : 0.0;
    }

    // Eliminar todos los detalles de un ingreso
    public void eliminarDetallesPorIngreso(Long idIngreso) {
        List<DetalleIngreso> detalles = detalleIngresoRepository.findByIngresoMateriaPrimaId(idIngreso);

        // Ajustar stock para cada detalle
        for (DetalleIngreso detalle : detalles) {
            BigDecimal cantidadARestar = detalle.getCantidad().negate();
            actualizarStockMateriaPrima(detalle.getMateriaPrima(), cantidadARestar);
        }

        detalleIngresoRepository.deleteByIngresoMateriaPrimaId(idIngreso);
    }

    // Método privado para actualizar stock de materia prima
    private void actualizarStockMateriaPrima(MateriaPrima materiaPrima, BigDecimal cantidad) {
        Integer stockActual = materiaPrima.getStockActual();
        Integer nuevaCantidad = cantidad.intValue();
        materiaPrima.setStockActual(stockActual + nuevaCantidad);
        materiaPrimaRepository.save(materiaPrima);
    }

    // Método privado para convertir entidad a DTO
    private DetalleIngresoDTO convertirADTO(DetalleIngreso detalleIngreso) {
        DetalleIngresoDTO dto = DetalleIngresoDTO.builder()
                .id(detalleIngreso.getId())
                .idIngreso(detalleIngreso.getIngresoMateriaPrima().getId())
                .idMateriaPrima(detalleIngreso.getMateriaPrima().getId())
                .cantidad(detalleIngreso.getCantidad())
                .costoUnitario(detalleIngreso.getCostoUnitario())
                .build();

        // Agregar información adicional
        dto.setNombreMateriaPrima(detalleIngreso.getMateriaPrima().getNombre());
        dto.setCodigoMateriaPrima(detalleIngreso.getMateriaPrima().getCodigo());
        dto.setUnidadMateriaPrima(detalleIngreso.getMateriaPrima().getUnidad());
        dto.setCostoTotal(detalleIngreso.getCostoTotal());

        return dto;
    }
}