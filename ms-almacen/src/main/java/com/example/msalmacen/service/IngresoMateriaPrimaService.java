package com.example.msalmacen.service;


import com.example.msalmacen.client.ProveedorClient;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class IngresoMateriaPrimaService {

    private final IngresoMateriaPrimaRepository ingresoRepository;
    private final AlmacenRepository almacenRepository;
    private final ProveedorClient proveedorClient;

    public List<IngresoMateriaPrimaDTO> obtenerTodos() {
        return ingresoRepository.findAllOrderByFechaIngresoDesc()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<IngresoMateriaPrimaDTO> obtenerPorId(Long id) {
        return ingresoRepository.findById(id)
                .map(this::convertirADTO);
    }

    public IngresoMateriaPrimaDTO crear(IngresoMateriaPrimaDTO dto) {
        // Validar que el almacén existe
        Almacen almacen = almacenRepository.findById(dto.getIdAlmacen())
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado con ID: " + dto.getIdAlmacen()));

        // Validar que el proveedor existe (llamada al microservicio)
        try {
            ProveedorDTO proveedor = proveedorClient.obtenerProveedor(dto.getIdProveedor());
            if (proveedor == null) {
                throw new RuntimeException("Proveedor no encontrado con ID: " + dto.getIdProveedor());
            }
        } catch (Exception e) {
            log.error("Error al validar proveedor: {}", e.getMessage());
            throw new RuntimeException("Error al validar el proveedor: " + e.getMessage());
        }

        IngresoMateriaPrima ingreso = convertirAEntidad(dto);
        ingreso.setAlmacen(almacen);

        IngresoMateriaPrima ingresoGuardado = ingresoRepository.save(ingreso);
        return convertirADTO(ingresoGuardado);
    }

    public IngresoMateriaPrimaDTO actualizar(Long id, IngresoMateriaPrimaDTO dto) {
        IngresoMateriaPrima ingresoExistente = ingresoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado con ID: " + id));

        // Validar almacén si cambió
        if (!ingresoExistente.getAlmacen().getId().equals(dto.getIdAlmacen())) {
            Almacen nuevoAlmacen = almacenRepository.findById(dto.getIdAlmacen())
                    .orElseThrow(() -> new RuntimeException("Almacén no encontrado con ID: " + dto.getIdAlmacen()));
            ingresoExistente.setAlmacen(nuevoAlmacen);
        }

        // Validar proveedor si cambió
        if (!ingresoExistente.getIdProveedor().equals(dto.getIdProveedor())) {
            try {
                ProveedorDTO proveedor = proveedorClient.obtenerProveedor(dto.getIdProveedor());
                if (proveedor == null) {
                    throw new RuntimeException("Proveedor no encontrado con ID: " + dto.getIdProveedor());
                }
            } catch (Exception e) {
                log.error("Error al validar proveedor: {}", e.getMessage());
                throw new RuntimeException("Error al validar el proveedor: " + e.getMessage());
            }
        }

        // Actualizar campos
        ingresoExistente.setIdProveedor(dto.getIdProveedor());
        ingresoExistente.setFechaIngreso(dto.getFechaIngreso());
        ingresoExistente.setDocumentoRef(dto.getDocumentoRef());
        ingresoExistente.setObservaciones(dto.getObservaciones());

        IngresoMateriaPrima ingresoActualizado = ingresoRepository.save(ingresoExistente);
        return convertirADTO(ingresoActualizado);
    }

    public void eliminar(Long id) {
        if (!ingresoRepository.existsById(id)) {
            throw new RuntimeException("Ingreso no encontrado con ID: " + id);
        }
        ingresoRepository.deleteById(id);
    }

    // Métodos de búsqueda específicos
    public List<IngresoMateriaPrimaDTO> obtenerPorProveedor(Long idProveedor) {
        return ingresoRepository.findByIdProveedor(idProveedor)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<IngresoMateriaPrimaDTO> obtenerPorAlmacen(Long idAlmacen) {
        return ingresoRepository.findByAlmacenId(idAlmacen)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<IngresoMateriaPrimaDTO> obtenerPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ingresoRepository.findByFechaIngresoBetween(fechaInicio, fechaFin)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<IngresoMateriaPrimaDTO> obtenerPorDocumento(String documento) {
        return ingresoRepository.findByDocumentoRefContainingIgnoreCase(documento)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Métodos de conversión
    private IngresoMateriaPrimaDTO convertirADTO(IngresoMateriaPrima ingreso) {
        IngresoMateriaPrimaDTO dto = IngresoMateriaPrimaDTO.builder()
                .id(ingreso.getId())
                .idProveedor(ingreso.getIdProveedor())
                .idAlmacen(ingreso.getAlmacen().getId())
                .fechaIngreso(ingreso.getFechaIngreso())
                .documentoRef(ingreso.getDocumentoRef())
                .observaciones(ingreso.getObservaciones())
                .nombreAlmacen(ingreso.getAlmacen().getNombre())
                .build();

        // Intentar obtener el nombre del proveedor
        try {
            ProveedorDTO proveedor = proveedorClient.obtenerProveedor(ingreso.getIdProveedor());
            if (proveedor != null) {
                dto.setNombreProveedor(proveedor.getNombreEmpresa());
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener información del proveedor {}: {}", ingreso.getIdProveedor(), e.getMessage());
            dto.setNombreProveedor("Proveedor no disponible");
        }

        return dto;
    }

    private IngresoMateriaPrima convertirAEntidad(IngresoMateriaPrimaDTO dto) {
        return IngresoMateriaPrima.builder()
                .id(dto.getId())
                .idProveedor(dto.getIdProveedor())
                .fechaIngreso(dto.getFechaIngreso())
                .documentoRef(dto.getDocumentoRef())
                .observaciones(dto.getObservaciones())
                .build();
    }
}