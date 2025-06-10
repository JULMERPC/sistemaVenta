package com.example.msalmacen.service;

import com.example.msalmacen.client.ProveedorClient;
import com.example.msalmacen.dto.AlmacenDTO;
import com.example.msalmacen.dto.IngresoMateriaPrimaDTO;
import com.example.msalmacen.dto.ProveedorDTO;
import com.example.msalmacen.entity.Almacen;
import com.example.msalmacen.entity.IngresoMateriaPrima;
import com.example.msalmacen.repository.AlmacenRepository;
import com.example.msalmacen.repository.IngresoMateriaPrimaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IngresoMateriaPrimaService {

    private final IngresoMateriaPrimaRepository ingresoRepository;
    private final ProveedorClient proveedorClient;
    private final AlmacenRepository almacenRepository;

    public List<IngresoMateriaPrimaDTO> obtenerTodos() {
        log.info("Obteniendo todos los ingresos de materia prima");
        return ingresoRepository.findAllByOrderByFechaDescCreatedAtDesc()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<IngresoMateriaPrimaDTO> obtenerPorId(Long id) {
        log.info("Obteniendo ingreso por ID: {}", id);
        return ingresoRepository.findById(id)
                .map(this::convertirADTO);
    }

    public IngresoMateriaPrimaDTO crear(IngresoMateriaPrimaDTO dto) {
        log.info("Creando nuevo ingreso de materia prima");

        // Validar que no exista el número de documento
        if (ingresoRepository.existsByNroDocumento(dto.getNroDocumento())) {
            throw new RuntimeException("Ya existe un ingreso con el número de documento: " + dto.getNroDocumento());
        }

        // Validar que el proveedor existe
        try {
            ProveedorDTO proveedor = proveedorClient.obtenerProveedor(dto.getProveedorId());
            if (proveedor == null) {
                throw new RuntimeException("Proveedor no encontrado con ID: " + dto.getProveedorId());
            }
        } catch (Exception e) {
            log.error("Error al validar proveedor: {}", e.getMessage());
            throw new RuntimeException("Error al validar el proveedor: " + e.getMessage());
        }

        // Validar que el almacén existe
        if (!almacenRepository.existsById(dto.getAlmacenId())) {
            throw new RuntimeException("Almacén no encontrado con ID: " + dto.getAlmacenId());
        }

        IngresoMateriaPrima ingreso = convertirAEntidad(dto);
        IngresoMateriaPrima ingresoGuardado = ingresoRepository.save(ingreso);

        log.info("Ingreso creado exitosamente con ID: {}", ingresoGuardado.getId());
        return convertirADTO(ingresoGuardado);
    }

    public IngresoMateriaPrimaDTO actualizar(Long id, IngresoMateriaPrimaDTO dto) {
        log.info("Actualizando ingreso con ID: {}", id);

        IngresoMateriaPrima ingresoExistente = ingresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado con ID: " + id));

        // Validar número de documento único (si se cambió)
        if (!ingresoExistente.getNroDocumento().equals(dto.getNroDocumento()) &&
                ingresoRepository.existsByNroDocumento(dto.getNroDocumento())) {
            throw new RuntimeException("Ya existe un ingreso con el número de documento: " + dto.getNroDocumento());
        }

        // Validar proveedor si se cambió
        if (!ingresoExistente.getProveedorId().equals(dto.getProveedorId())) {
            try {
                ProveedorDTO proveedor = proveedorClient.obtenerProveedor(dto.getProveedorId());
                if (proveedor == null) {
                    throw new RuntimeException("Proveedor no encontrado con ID: " + dto.getProveedorId());
                }
            } catch (Exception e) {
                log.error("Error al validar proveedor: {}", e.getMessage());
                throw new RuntimeException("Error al validar el proveedor: " + e.getMessage());
            }
        }

        // Validar almacén si se cambió
        if (!ingresoExistente.getAlmacenId().equals(dto.getAlmacenId())) {
            if (!almacenRepository.existsById(dto.getAlmacenId())) {
                throw new RuntimeException("Almacén no encontrado con ID: " + dto.getAlmacenId());
            }
        }

        // Actualizar campos
        ingresoExistente.setProveedorId(dto.getProveedorId());
        ingresoExistente.setAlmacenId(dto.getAlmacenId());
        ingresoExistente.setFecha(dto.getFecha());
        ingresoExistente.setTipoDocumento(dto.getTipoDocumento());
        ingresoExistente.setNroDocumento(dto.getNroDocumento());
        ingresoExistente.setTotal(dto.getTotal());

        IngresoMateriaPrima ingresoActualizado = ingresoRepository.save(ingresoExistente);

        log.info("Ingreso actualizado exitosamente con ID: {}", id);
        return convertirADTO(ingresoActualizado);
    }

    public void eliminar(Long id) {
        log.info("Eliminando ingreso con ID: {}", id);

        if (!ingresoRepository.existsById(id)) {
            throw new RuntimeException("Ingreso no encontrado con ID: " + id);
        }

        ingresoRepository.deleteById(id);
        log.info("Ingreso eliminado exitosamente con ID: {}", id);
    }

    // Métodos de consulta específicos
    public List<IngresoMateriaPrimaDTO> obtenerPorProveedor(Long proveedorId) {
        log.info("Obteniendo ingresos por proveedor ID: {}", proveedorId);
        return ingresoRepository.findByProveedorId(proveedorId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<IngresoMateriaPrimaDTO> obtenerPorAlmacen(Long almacenId) {
        log.info("Obteniendo ingresos por almacén ID: {}", almacenId);
        return ingresoRepository.findByAlmacenId(almacenId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<IngresoMateriaPrimaDTO> obtenerPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        log.info("Obteniendo ingresos por rango de fechas: {} - {}", fechaInicio, fechaFin);
        return ingresoRepository.findByFechaBetween(fechaInicio, fechaFin)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<IngresoMateriaPrimaDTO> obtenerPorNumeroDocumento(String nroDocumento) {
        log.info("Obteniendo ingreso por número de documento: {}", nroDocumento);
        return ingresoRepository.findByNroDocumento(nroDocumento)
                .map(this::convertirADTO);
    }

    public BigDecimal obtenerTotalPorProveedorYFecha(Long proveedorId, LocalDate fechaInicio, LocalDate fechaFin) {
        log.info("Obteniendo total por proveedor {} y fechas: {} - {}", proveedorId, fechaInicio, fechaFin);
        BigDecimal total = ingresoRepository.sumTotalByProveedorAndFecha(proveedorId, fechaInicio, fechaFin);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal obtenerTotalPorAlmacenYFecha(Long almacenId, LocalDate fechaInicio, LocalDate fechaFin) {
        log.info("Obteniendo total por almacén {} y fechas: {} - {}", almacenId, fechaInicio, fechaFin);
        BigDecimal total = ingresoRepository.sumTotalByAlmacenAndFecha(almacenId, fechaInicio, fechaFin);
        return total != null ? total : BigDecimal.ZERO;
    }

    // Métodos de conversión
    private IngresoMateriaPrimaDTO convertirADTO(IngresoMateriaPrima ingreso) {
        IngresoMateriaPrimaDTO dto = IngresoMateriaPrimaDTO.builder()
                .id(ingreso.getId())
                .proveedorId(ingreso.getProveedorId())
                .almacenId(ingreso.getAlmacenId())
                .fecha(ingreso.getFecha())
                .tipoDocumento(ingreso.getTipoDocumento())
                .nroDocumento(ingreso.getNroDocumento())
                .total(ingreso.getTotal())
                .createdAt(ingreso.getCreatedAt())
                .updatedAt(ingreso.getUpdatedAt())
                .build();

        // Cargar información del proveedor
        try {
            ProveedorDTO proveedor = proveedorClient.obtenerProveedor(ingreso.getProveedorId());
            dto.setProveedor(proveedor);
        } catch (Exception e) {
            log.warn("No se pudo cargar información del proveedor {}: {}", ingreso.getProveedorId(), e.getMessage());
        }

        // Cargar información del almacén
        try {
            Optional<Almacen> almacenEntity = almacenRepository.findById(ingreso.getAlmacenId());
            if (almacenEntity.isPresent()) {
                Almacen almacen = almacenEntity.get();
                AlmacenDTO almacenDTO = AlmacenDTO.builder()
                        .id(almacen.getId())
                        .nombre(almacen.getNombre())
                        .ubicacion(almacen.getUbicacion())
                        .tipo(almacen.getTipo())
                        .estado(almacen.getEstado())
                        .fechaRegistro(almacen.getFechaRegistro())
                        .build();
                dto.setAlmacen(almacenDTO);
            }
        } catch (Exception e) {
            log.warn("No se pudo cargar información del almacén {}: {}", ingreso.getAlmacenId(), e.getMessage());
        }

        return dto;
    }

    private IngresoMateriaPrima convertirAEntidad(IngresoMateriaPrimaDTO dto) {
        return IngresoMateriaPrima.builder()
                .id(dto.getId())
                .proveedorId(dto.getProveedorId())
                .almacenId(dto.getAlmacenId())
                .fecha(dto.getFecha())
                .tipoDocumento(dto.getTipoDocumento())
                .nroDocumento(dto.getNroDocumento())
                .total(dto.getTotal())
                .build();
    }
}