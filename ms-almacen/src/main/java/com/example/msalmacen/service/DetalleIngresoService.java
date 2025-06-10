
package com.example.msalmacen.service;

import com.example.msalmacen.dto.DetalleIngresoDTO;
import com.example.msalmacen.entity.DetalleIngreso;
import com.example.msalmacen.entity.IngresoMateriaPrima;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.repository.DetalleIngresoRepository;
import com.example.msalmacen.repository.IngresoMateriaPrimaRepository;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class DetalleIngresoService {

    private final DetalleIngresoRepository detalleIngresoRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;
    private final IngresoMateriaPrimaRepository ingresoMateriaPrimaRepository;

    // Crear un nuevo detalle de ingreso
    public DetalleIngresoDTO crearDetalleIngreso(DetalleIngresoDTO detalleDTO) {
        log.info("Creando detalle de ingreso para materia prima ID: {}", detalleDTO.getMateriaPrimaId());

        // Validar que la materia prima existe y está activa
        MateriaPrima materiaPrima = materiaPrimaRepository.findById(detalleDTO.getMateriaPrimaId())
                .orElseThrow(() -> new RuntimeException("Materia prima no encontrada con ID: " + detalleDTO.getMateriaPrimaId()));

        if (!materiaPrima.isActiva()) {
            throw new RuntimeException("La materia prima está inactiva y no se puede ingresar");
        }

        // Validar que el ingreso existe
        IngresoMateriaPrima ingreso = ingresoMateriaPrimaRepository.findById(detalleDTO.getIngresoMateriaPrimaId())
                .orElseThrow(() -> new RuntimeException("Ingreso de materia prima no encontrado con ID: " + detalleDTO.getIngresoMateriaPrimaId()));

        // Verificar si ya existe un detalle para esta materia prima en este ingreso
        boolean existeDetalle = detalleIngresoRepository.existsByIngresoMateriaPrimaIdAndMateriaPrimaId(
                detalleDTO.getIngresoMateriaPrimaId(), detalleDTO.getMateriaPrimaId());

        if (existeDetalle) {
            throw new RuntimeException("Ya existe un detalle para esta materia prima en este ingreso");
        }

        // Crear la entidad
        DetalleIngreso detalleIngreso = DetalleIngreso.builder()
                .ingresoMateriaPrima(ingreso)
                .materiaPrima(materiaPrima)
                .cantidad(detalleDTO.getCantidad())
                .costoUnitario(detalleDTO.getCostoUnitario())
                .build();

        DetalleIngreso detalleGuardado = detalleIngresoRepository.save(detalleIngreso);
        log.info("Detalle de ingreso creado exitosamente con ID: {}", detalleGuardado.getId());

        return convertirADTO(detalleGuardado);
    }

    // Actualizar un detalle de ingreso existente
    public DetalleIngresoDTO actualizarDetalleIngreso(Long id, DetalleIngresoDTO detalleDTO) {
        log.info("Actualizando detalle de ingreso con ID: {}", id);

        DetalleIngreso detalleExistente = detalleIngresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de ingreso no encontrado con ID: " + id));

        // Validar que la materia prima existe y está activa
        MateriaPrima materiaPrima = materiaPrimaRepository.findById(detalleDTO.getMateriaPrimaId())
                .orElseThrow(() -> new RuntimeException("Materia prima no encontrada con ID: " + detalleDTO.getMateriaPrimaId()));

        if (!materiaPrima.isActiva()) {
            throw new RuntimeException("La materia prima está inactiva y no se puede actualizar");
        }

        // Actualizar los campos
        detalleExistente.setMateriaPrima(materiaPrima);
        detalleExistente.setCantidad(detalleDTO.getCantidad());
        detalleExistente.setCostoUnitario(detalleDTO.getCostoUnitario());

        DetalleIngreso detalleActualizado = detalleIngresoRepository.save(detalleExistente);
        log.info("Detalle de ingreso actualizado exitosamente con ID: {}", detalleActualizado.getId());

        return convertirADTO(detalleActualizado);
    }

    // Obtener detalle por ID
    @Transactional(readOnly = true)
    public DetalleIngresoDTO obtenerDetallePorId(Long id) {
        log.info("Buscando detalle de ingreso con ID: {}", id);

        DetalleIngreso detalle = detalleIngresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de ingreso no encontrado con ID: " + id));

        return convertirADTO(detalle);
    }

    // Obtener todos los detalles de un ingreso específico
    @Transactional(readOnly = true)
    public List<DetalleIngresoDTO> obtenerDetallesPorIngresoId(Long ingresoId) {
        log.info("Obteniendo detalles para ingreso ID: {}", ingresoId);

        List<DetalleIngreso> detalles = detalleIngresoRepository.findDetallesConMateriaPrimaByIngresoId(ingresoId);
        return detalles.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener todos los detalles de una materia prima específica
    @Transactional(readOnly = true)
    public List<DetalleIngresoDTO> obtenerDetallesPorMateriaPrimaId(Long materiaPrimaId) {
        log.info("Obteniendo detalles para materia prima ID: {}", materiaPrimaId);

        List<DetalleIngreso> detalles = detalleIngresoRepository.findByMateriaPrimaId(materiaPrimaId);
        return detalles.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener todos los detalles
    @Transactional(readOnly = true)
    public List<DetalleIngresoDTO> obtenerTodosLosDetalles() {
        log.info("Obteniendo todos los detalles de ingreso");

        List<DetalleIngreso> detalles = detalleIngresoRepository.findAllOrderByFechaDesc();
        return detalles.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Eliminar detalle por ID
    public void eliminarDetalle(Long id) {
        log.info("Eliminando detalle de ingreso con ID: {}", id);

        if (!detalleIngresoRepository.existsById(id)) {
            throw new RuntimeException("Detalle de ingreso no encontrado con ID: " + id);
        }

        detalleIngresoRepository.deleteById(id);
        log.info("Detalle de ingreso eliminado exitosamente con ID: {}", id);
    }

    // Eliminar todos los detalles de un ingreso
    public void eliminarDetallesPorIngresoId(Long ingresoId) {
        log.info("Eliminando todos los detalles para ingreso ID: {}", ingresoId);

        detalleIngresoRepository.deleteByIngresoMateriaPrimaId(ingresoId);
        log.info("Detalles eliminados exitosamente para ingreso ID: {}", ingresoId);
    }

    // Calcular costo total por ingreso
    @Transactional(readOnly = true)
    public BigDecimal calcularCostoTotalPorIngreso(Long ingresoId) {
        log.info("Calculando costo total para ingreso ID: {}", ingresoId);

        Double costoTotal = detalleIngresoRepository.getCostoTotalByIngresoId(ingresoId);
        return costoTotal != null ? BigDecimal.valueOf(costoTotal) : BigDecimal.ZERO;
    }

    // Calcular cantidad total ingresada por materia prima
    @Transactional(readOnly = true)
    public BigDecimal calcularCantidadTotalPorMateriaPrima(Long materiaPrimaId) {
        log.info("Calculando cantidad total ingresada para materia prima ID: {}", materiaPrimaId);

        Double cantidadTotal = detalleIngresoRepository.getCantidadTotalByMateriaPrimaId(materiaPrimaId);
        return cantidadTotal != null ? BigDecimal.valueOf(cantidadTotal) : BigDecimal.ZERO;
    }

    // Crear múltiples detalles de ingreso
    public List<DetalleIngresoDTO> crearMultiplesDetalles(List<DetalleIngresoDTO> detallesDTO) {
        log.info("Creando {} detalles de ingreso", detallesDTO.size());

        return detallesDTO.stream()
                .map(this::crearDetalleIngreso)
                .collect(Collectors.toList());
    }

    // Método privado para convertir entidad a DTO
    private DetalleIngresoDTO convertirADTO(DetalleIngreso detalle) {
        DetalleIngresoDTO dto = DetalleIngresoDTO.builder()
                .id(detalle.getId())
                .ingresoMateriaPrimaId(detalle.getIngresoMateriaPrima().getId())
                .materiaPrimaId(detalle.getMateriaPrima().getId())
                .cantidad(detalle.getCantidad())
                .costoUnitario(detalle.getCostoUnitario())
                .costoTotal(detalle.getCostoTotal())
                .build();

        // Agregar información de la materia prima
        MateriaPrima materiaPrima = detalle.getMateriaPrima();
        dto.setNombreMateriaPrima(materiaPrima.getNombre());
        dto.setDescripcionMateriaPrima(materiaPrima.getDescripcion());
        dto.setUnidadMedida(materiaPrima.getUnidadMedida());
        dto.setEstadoMateriaPrima(materiaPrima.getEstado());

        // Agregar información del ingreso
        IngresoMateriaPrima ingreso = detalle.getIngresoMateriaPrima();
        dto.setFechaIngreso(ingreso.getFecha());
        // Agregar más campos del ingreso según sea necesario
        // dto.setProveedorIngreso(ingreso.getProveedor());
        // dto.setNumeroDocumento(ingreso.getNumeroDocumento());

        return dto;
    }
}