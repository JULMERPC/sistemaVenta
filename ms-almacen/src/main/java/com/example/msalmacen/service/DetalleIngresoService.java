//package com.example.msalmacen.service;
//
//import com.example.msalmacen.dto.DetalleIngresoDTO;
//import com.example.msalmacen.entity.DetalleIngreso;
//import com.example.msalmacen.entity.IngresoMateriaPrima;
//import com.example.msalmacen.entity.MateriaPrima;
//import com.example.msalmacen.repository.DetalleIngresoRepository;
//import com.example.msalmacen.repository.IngresoMateriaPrimaRepository;
//import com.example.msalmacen.repository.MateriaPrimaRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//@Transactional
//public class DetalleIngresoService {
//
//    private final DetalleIngresoRepository detalleIngresoRepository;
//    private final IngresoMateriaPrimaRepository ingresoMateriaPrimaRepository;
//    private final MateriaPrimaRepository materiaPrimaRepository;
//
//    // Crear un nuevo detalle de ingreso
//    public DetalleIngresoDTO crearDetalleIngreso(DetalleIngresoDTO detalleIngresoDTO) {
//        log.info("Creando detalle de ingreso para Ingreso ID: {} y Materia Prima ID: {}",
//                detalleIngresoDTO.getIngresoMateriaPrimaId(), detalleIngresoDTO.getMateriaPrimaId());
//
//        // Verificar que el ingreso existe
//        IngresoMateriaPrima ingreso = ingresoMateriaPrimaRepository
//                .findById(detalleIngresoDTO.getIngresoMateriaPrimaId())
//                .orElseThrow(() -> new RuntimeException("Ingreso de materia prima no encontrado"));
//
//        // Verificar que la materia prima existe
//        MateriaPrima materiaPrima = materiaPrimaRepository
//                .findById(detalleIngresoDTO.getMateriaPrimaId())
//                .orElseThrow(() -> new RuntimeException("Materia prima no encontrada"));
//
//        // Verificar que no existe ya un detalle para este ingreso y materia prima
//        if (detalleIngresoRepository.existsByIngresoAndMateriaPrima(
//                detalleIngresoDTO.getIngresoMateriaPrimaId(),
//                detalleIngresoDTO.getMateriaPrimaId())) {
//            throw new RuntimeException("Ya existe un detalle para este ingreso y materia prima");
//        }
//
//        DetalleIngreso detalleIngreso = DetalleIngreso.builder()
//                .ingresoMateriaPrima(ingreso)
//                .materiaPrima(materiaPrima)
//                .cantidad(detalleIngresoDTO.getCantidad())
//                .costoUnitario(detalleIngresoDTO.getCostoUnitario())
//                .build();
//
//        DetalleIngreso savedDetalle = detalleIngresoRepository.save(detalleIngreso);
//        log.info("Detalle de ingreso creado con ID: {}", savedDetalle.getId());
//
//        return convertToDTO(savedDetalle);
//    }
//
//    // Obtener todos los detalles de ingreso
//    @Transactional(readOnly = true)
//    public List<DetalleIngresoDTO> obtenerTodosLosDetalles() {
//        log.info("Obteniendo todos los detalles de ingreso");
//        return detalleIngresoRepository.findAll().stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    // Obtener un detalle por ID
//    @Transactional(readOnly = true)
//    public Optional<DetalleIngresoDTO> obtenerDetallePorId(Long id) {
//        log.info("Obteniendo detalle de ingreso con ID: {}", id);
//        return detalleIngresoRepository.findById(id)
//                .map(this::convertToDTO);
//    }
//
//    // Obtener detalles por ingreso
//    @Transactional(readOnly = true)
//    public List<DetalleIngresoDTO> obtenerDetallesPorIngreso(Long ingresoId) {
//        log.info("Obteniendo detalles para el ingreso ID: {}", ingresoId);
//        return detalleIngresoRepository.findByIngresoMateriaPrimaIdWithDetails(ingresoId).stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    // Obtener detalles por materia prima
//    @Transactional(readOnly = true)
//    public List<DetalleIngresoDTO> obtenerDetallesPorMateriaPrima(Long materiaPrimaId) {
//        log.info("Obteniendo detalles para la materia prima ID: {}", materiaPrimaId);
//        return detalleIngresoRepository.findByMateriaPrimaId(materiaPrimaId).stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    // Actualizar un detalle de ingreso
//    public DetalleIngresoDTO actualizarDetalleIngreso(Long id, DetalleIngresoDTO detalleIngresoDTO) {
//        log.info("Actualizando detalle de ingreso con ID: {}", id);
//
//        DetalleIngreso detalleExistente = detalleIngresoRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Detalle de ingreso no encontrado"));
//
//        // Actualizar campos
//        detalleExistente.setCantidad(detalleIngresoDTO.getCantidad());
//        detalleExistente.setCostoUnitario(detalleIngresoDTO.getCostoUnitario());
//
//        // Si se cambia la materia prima, verificar que exista
//        if (!detalleExistente.getMateriaPrima().getId().equals(detalleIngresoDTO.getMateriaPrimaId())) {
//            MateriaPrima nuevaMateriaPrima = materiaPrimaRepository
//                    .findById(detalleIngresoDTO.getMateriaPrimaId())
//                    .orElseThrow(() -> new RuntimeException("Materia prima no encontrada"));
//            detalleExistente.setMateriaPrima(nuevaMateriaPrima);
//        }
//
//        DetalleIngreso savedDetalle = detalleIngresoRepository.save(detalleExistente);
//        log.info("Detalle de ingreso actualizado con ID: {}", savedDetalle.getId());
//
//        return convertToDTO(savedDetalle);
//    }
//
//    // Eliminar un detalle de ingreso
//    public void eliminarDetalleIngreso(Long id) {
//        log.info("Eliminando detalle de ingreso con ID: {}", id);
//
//        if (!detalleIngresoRepository.existsById(id)) {
//            throw new RuntimeException("Detalle de ingreso no encontrado");
//        }
//
//        detalleIngresoRepository.deleteById(id);
//        log.info("Detalle de ingreso eliminado con ID: {}", id);
//    }
//
//    // Obtener total de cantidad por materia prima
//    @Transactional(readOnly = true)
//    public BigDecimal obtenerTotalCantidadPorMateriaPrima(Long materiaPrimaId) {
//        return detalleIngresoRepository.getTotalCantidadByMateriaPrima(materiaPrimaId);
//    }
//
//    // Obtener total de costo por ingreso
//    @Transactional(readOnly = true)
//    public BigDecimal obtenerTotalCostoPorIngreso(Long ingresoId) {
//        return detalleIngresoRepository.getTotalCostoByIngreso(ingresoId);
//    }
//
//    // Obtener detalles por rango de fechas
//    @Transactional(readOnly = true)
//    public List<DetalleIngresoDTO> obtenerDetallesPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
//        log.info("Obteniendo detalles entre {} y {}", fechaInicio, fechaFin);
//        return detalleIngresoRepository.findByFechaRange(fechaInicio, fechaFin).stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//    // Obtener último costo unitario de una materia prima
//    @Transactional(readOnly = true)
//    public Optional<BigDecimal> obtenerUltimoCostoUnitario(Long materiaPrimaId) {
//        List<BigDecimal> costos = detalleIngresoRepository.findLastCostoUnitarioByMateriaPrima(materiaPrimaId);
//        return costos.isEmpty() ? Optional.empty() : Optional.of(costos.get(0));
//    }
//
//    // Convertir entidad a DTO
//    private DetalleIngresoDTO convertToDTO(DetalleIngreso detalleIngreso) {
//        DetalleIngresoDTO dto = DetalleIngresoDTO.builder()
//                .id(detalleIngreso.getId())
//                .ingresoMateriaPrimaId(detalleIngreso.getIngresoMateriaPrima().getId())
//                .materiaPrimaId(detalleIngreso.getMateriaPrima().getId())
//                .cantidad(detalleIngreso.getCantidad())
//                .costoUnitario(detalleIngreso.getCostoUnitario())
//                .createdAt(detalleIngreso.getCreatedAt())
//                .updatedAt(detalleIngreso.getUpdatedAt())
//                .build();
//
//        return dto;
//    }
//
//    // Convertir DTO a entidad
////    private DetalleIngreso convertToEntity(DetalleIngresoDTO dto) {
////        DetalleIngreso detalleIngreso = new DetalleIngreso();
////        detalleIngreso.setId(dto.getId());
////        detalleIngreso.setCantidad(dto.getCantidad());
////        detalleIngreso.setCostoUnitario(dto.getCostoUnitario());
////
////        return detalleIngreso;
////    }
//
////    private DetalleIngreso convertToEntity(DetalleIngresoDTO dto) {
////        DetalleIngreso detalleIngreso = new DetalleIngreso();
////        detalleIngreso.setId(dto.getId());
////        detalleIngreso.setCantidad(dto.getCantidad());
////        detalleIngreso.setCostoUnitario(dto.getCostoUnitario());
////
////        // Asignar la entidad IngresoMateriaPrima
////        if (dto.getIngresoMateriaPrimaId() != null) {
////            IngresoMateriaPrima ingreso = ingresoMateriaPrimaRepository
////                    .findById(dto.getIngresoMateriaPrimaId())
////                    .orElseThrow(() -> new RuntimeException("Ingreso no encontrado"));
////            detalleIngreso.setIngresoMateriaPrima(ingreso);
////        }
////
////        // Asignar la entidad MateriaPrima
////        if (dto.getMateriaPrimaId() != null) {
////            MateriaPrima materiaPrima = materiaPrimaRepository
////                    .findById(dto.getMateriaPrimaId())
////                    .orElseThrow(() -> new RuntimeException("Materia Prima no encontrada"));
////            detalleIngreso.setMateriaPrima(materiaPrima);
////        }
////
////        return detalleIngreso;
////    }
//
//    private DetalleIngresoDTO convertToDTO(DetalleIngreso detalleIngreso) {
//        DetalleIngresoDTO dto = DetalleIngresoDTO.builder()
//                .id(detalleIngreso.getId())
//                .ingresoMateriaPrimaId(
//                        detalleIngreso.getIngresoMateriaPrima() != null
//                                ? detalleIngreso.getIngresoMateriaPrima().getId()
//                                : null
//                )
//                .materiaPrimaId(
//                        detalleIngreso.getMateriaPrima() != null
//                                ? detalleIngreso.getMateriaPrima().getId()
//                                : null
//                )
//                .cantidad(detalleIngreso.getCantidad())
//                .costoUnitario(detalleIngreso.getCostoUnitario())
//                .createdAt(detalleIngreso.getCreatedAt())
//                .updatedAt(detalleIngreso.getUpdatedAt())
//                // 🔽 Aquí convertimos las entidades relacionadas a DTO
//                .ingresoMateriaPrima(
//                        detalleIngreso.getIngresoMateriaPrima() != null
//                                ? convertIngresoMateriaPrimaToDTO(detalleIngreso.getIngresoMateriaPrima())
//                                : null
//                )
//                .materiaPrima(
//                        detalleIngreso.getMateriaPrima() != null
//                                ? convertMateriaPrimaToDTO(detalleIngreso.getMateriaPrima())
//                                : null
//                )
//                .build();
//
//        return dto;
//    }
//
//
//}

package com.example.msalmacen.service;

import com.example.msalmacen.client.ProveedorClient;
import com.example.msalmacen.dto.*;
import com.example.msalmacen.entity.DetalleIngreso;
import com.example.msalmacen.entity.IngresoMateriaPrima;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.repository.DetalleIngresoRepository;
import com.example.msalmacen.repository.IngresoMateriaPrimaRepository;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DetalleIngresoService {

    private final DetalleIngresoRepository detalleIngresoRepository;
    private final IngresoMateriaPrimaRepository ingresoMateriaPrimaRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;

    private final ProveedorClient proveedorClient;
    private final AlmacenService almacenService;


    /**
     * Crear un nuevo detalle de ingreso
     */
    public DetalleIngresoDTO crear(DetalleIngresoDTO detalleIngresoDTO) {
        log.info("Creando detalle de ingreso para ingreso ID: {} y materia prima ID: {}",
                detalleIngresoDTO.getIngresoMateriaPrimaId(), detalleIngresoDTO.getMateriaPrimaId());

        // Validar que el ingreso de materia prima existe
        IngresoMateriaPrima ingresoMateriaPrima = ingresoMateriaPrimaRepository
                .findById(detalleIngresoDTO.getIngresoMateriaPrimaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ingreso de materia prima no encontrado con ID: " + detalleIngresoDTO.getIngresoMateriaPrimaId()));

        // Validar que la materia prima existe
        MateriaPrima materiaPrima = materiaPrimaRepository
                .findById(detalleIngresoDTO.getMateriaPrimaId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Materia prima no encontrada con ID: " + detalleIngresoDTO.getMateriaPrimaId()));

        // Verificar que no existe ya un detalle para la misma materia prima en el mismo ingreso
        if (detalleIngresoRepository.existsByIngresoMateriaPrimaIdAndMateriaPrimaId(
                detalleIngresoDTO.getIngresoMateriaPrimaId(), detalleIngresoDTO.getMateriaPrimaId())) {
            throw new IllegalArgumentException(
                    "Ya existe un detalle para esta materia prima en el ingreso especificado");
        }

        // Convertir DTO a entidad
        DetalleIngreso detalleIngreso = DetalleIngreso.builder()
                .ingresoMateriaPrima(ingresoMateriaPrima)
                .materiaPrima(materiaPrima)
                .cantidad(detalleIngresoDTO.getCantidad())
                .costoUnitario(detalleIngresoDTO.getCostoUnitario())
                .build();

        // Guardar
        DetalleIngreso detalleGuardado = detalleIngresoRepository.save(detalleIngreso);

        log.info("Detalle de ingreso creado con ID: {}", detalleGuardado.getId());

        return convertirADTO(detalleGuardado);
    }

    /**
     * Obtener todos los detalles de ingreso
     */
    @Transactional(readOnly = true)
    public List<DetalleIngresoDTO> obtenerTodos() {
        log.info("Obteniendo todos los detalles de ingreso");

        return detalleIngresoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener detalle de ingreso por ID
     */
    @Transactional(readOnly = true)
    public DetalleIngresoDTO obtenerPorId(Long id) {
        log.info("Obteniendo detalle de ingreso por ID: {}", id);

        DetalleIngreso detalleIngreso = detalleIngresoRepository.findByIdWithDetails(id);
        if (detalleIngreso == null) {
            throw new EntityNotFoundException("Detalle de ingreso no encontrado con ID: " + id);
        }

        return convertirADTO(detalleIngreso);
    }

    /**
     * Obtener detalles por ID de ingreso de materia prima
     */
    @Transactional(readOnly = true)
    public List<DetalleIngresoDTO> obtenerPorIngresoMateriaPrima(Long ingresoMateriaPrimaId) {
        log.info("Obteniendo detalles de ingreso por ingreso ID: {}", ingresoMateriaPrimaId);

        List<DetalleIngreso> detalles = detalleIngresoRepository.findByIngresoMateriaPrimaIdWithDetails(ingresoMateriaPrimaId);

        return detalles.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener detalles por ID de materia prima
     */
    @Transactional(readOnly = true)
    public List<DetalleIngresoDTO> obtenerPorMateriaPrima(Long materiaPrimaId) {
        log.info("Obteniendo detalles de ingreso por materia prima ID: {}", materiaPrimaId);

        return detalleIngresoRepository.findByMateriaPrimaId(materiaPrimaId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar detalle de ingreso
     */
    public DetalleIngresoDTO actualizar(Long id, DetalleIngresoDTO detalleIngresoDTO) {
        log.info("Actualizando detalle de ingreso con ID: {}", id);

        DetalleIngreso detalleExistente = detalleIngresoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Detalle de ingreso no encontrado con ID: " + id));

        // Actualizar campos
        detalleExistente.setCantidad(detalleIngresoDTO.getCantidad());
        detalleExistente.setCostoUnitario(detalleIngresoDTO.getCostoUnitario());

        // Si se cambia la materia prima, validar que existe
        if (!detalleExistente.getMateriaPrima().getId().equals(detalleIngresoDTO.getMateriaPrimaId())) {
            MateriaPrima nuevaMateriaPrima = materiaPrimaRepository
                    .findById(detalleIngresoDTO.getMateriaPrimaId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Materia prima no encontrada con ID: " + detalleIngresoDTO.getMateriaPrimaId()));

            detalleExistente.setMateriaPrima(nuevaMateriaPrima);
        }

        DetalleIngreso detalleActualizado = detalleIngresoRepository.save(detalleExistente);

        log.info("Detalle de ingreso actualizado con ID: {}", detalleActualizado.getId());

        return convertirADTO(detalleActualizado);
    }

    /**
     * Eliminar detalle de ingreso por ID
     */
    public void eliminar(Long id) {
        log.info("Eliminando detalle de ingreso con ID: {}", id);

        if (!detalleIngresoRepository.existsById(id)) {
            throw new EntityNotFoundException("Detalle de ingreso no encontrado con ID: " + id);
        }

        detalleIngresoRepository.deleteById(id);
        log.info("Detalle de ingreso eliminado con ID: {}", id);
    }

    /**
     * Crear múltiples detalles de ingreso
     */
    public List<DetalleIngresoDTO> crearMultiples(List<DetalleIngresoDTO> detallesDTO) {
        log.info("Creando {} detalles de ingreso", detallesDTO.size());

        return detallesDTO.stream()
                .map(this::crear)
                .collect(Collectors.toList());
    }

    /**
     * Eliminar todos los detalles de un ingreso específico
     */
    public void eliminarPorIngresoMateriaPrima(Long ingresoMateriaPrimaId) {
        log.info("Eliminando todos los detalles del ingreso ID: {}", ingresoMateriaPrimaId);

        detalleIngresoRepository.deleteByIngresoMateriaPrimaId(ingresoMateriaPrimaId);
        log.info("Detalles eliminados para el ingreso ID: {}", ingresoMateriaPrimaId);
    }

    /**
     * Contar detalles por ingreso
     */
    @Transactional(readOnly = true)
    public Long contarPorIngresoMateriaPrima(Long ingresoMateriaPrimaId) {
        return detalleIngresoRepository.countByIngresoMateriaPrimaId(ingresoMateriaPrimaId);
    }

    /**
     * Convertir entidad a DTO
     */
//    private DetalleIngresoDTO convertirADTO(DetalleIngreso detalleIngreso) {
//        DetalleIngresoDTO dto = DetalleIngresoDTO.builder()
//                .id(detalleIngreso.getId())
//                .ingresoMateriaPrimaId(detalleIngreso.getIngresoMateriaPrima().getId())
//                .materiaPrimaId(detalleIngreso.getMateriaPrima().getId())
//                .cantidad(detalleIngreso.getCantidad())
//                .costoUnitario(detalleIngreso.getCostoUnitario())
//                .createdAt(detalleIngreso.getCreatedAt())
//                .updatedAt(detalleIngreso.getUpdatedAt())
//                .build();
//
//        // Incluir información adicional si las entidades están cargadas
//        if (detalleIngreso.getIngresoMateriaPrima() != null) {
//            IngresoMateriaPrima ingreso = detalleIngreso.getIngresoMateriaPrima();
//            dto.setIngresoMateriaPrima(IngresoMateriaPrimaDTO.builder()
//                    .id(ingreso.getId())
//                    .proveedorId(ingreso.getProveedorId())
//                    .almacenId(ingreso.getAlmacenId())
//                    .fecha(ingreso.getFecha())
//                    .tipoDocumento(ingreso.getTipoDocumento())
//                    .nroDocumento(ingreso.getNroDocumento())
//                    .total(ingreso.getTotal())
//                    .createdAt(ingreso.getCreatedAt())
//                    .updatedAt(ingreso.getUpdatedAt())
//                    .build());
//        }
//
//        if (detalleIngreso.getMateriaPrima() != null) {
//            MateriaPrima materia = detalleIngreso.getMateriaPrima();
//            dto.setMateriaPrima(MateriaPrimaDTO.builder()
//                    .id(materia.getId())
//                    .nombre(materia.getNombre())
//                    .descripcion(materia.getDescripcion())
//                    .unidadMedida(materia.getUnidadMedida())
//                    .stockMinimo(materia.getStockMinimo())
//                    .estado(materia.getEstado())
//                    .fechaRegistro(materia.getFechaRegistro())
//                    .build());
//        }
//
//        return dto;
//    }


    private DetalleIngresoDTO convertirADTO(DetalleIngreso detalleIngreso) {
        DetalleIngresoDTO.DetalleIngresoDTOBuilder dtoBuilder = DetalleIngresoDTO.builder()
                .id(detalleIngreso.getId())
                .ingresoMateriaPrimaId(detalleIngreso.getIngresoMateriaPrima().getId())
                .materiaPrimaId(detalleIngreso.getMateriaPrima().getId())
                .cantidad(detalleIngreso.getCantidad())
                .costoUnitario(detalleIngreso.getCostoUnitario())
                .createdAt(detalleIngreso.getCreatedAt())
                .updatedAt(detalleIngreso.getUpdatedAt());

        // Incluir ingresoMateriaPrima si está cargado
        if (detalleIngreso.getIngresoMateriaPrima() != null) {
            IngresoMateriaPrima ingreso = detalleIngreso.getIngresoMateriaPrima();

            IngresoMateriaPrimaDTO.IngresoMateriaPrimaDTOBuilder ingresoBuilder = IngresoMateriaPrimaDTO.builder()
                    .id(ingreso.getId())
                    .proveedorId(ingreso.getProveedorId())
                    .almacenId(ingreso.getAlmacenId())
                    .fecha(ingreso.getFecha())
                    .tipoDocumento(ingreso.getTipoDocumento())
                    .nroDocumento(ingreso.getNroDocumento())
                    .total(ingreso.getTotal())
                    .createdAt(ingreso.getCreatedAt())
                    .updatedAt(ingreso.getUpdatedAt());


            try {
                // Llama al microservicio de proveedor para poblar proveedorDTO
                ProveedorDTO proveedorDTO = proveedorClient.obtenerProveedor(ingreso.getProveedorId());
                ingresoBuilder.proveedor(proveedorDTO);
            } catch (Exception e) {
                // Opcional: puedes loguear el error
                ingresoBuilder.proveedor(null); // o simplemente omitir esta línea
            }
            if (ingreso.getAlmacen() != null) {
                ingresoBuilder.almacen(AlmacenDTO.builder()
                        .id(ingreso.getAlmacen().getId())
                        .nombre(ingreso.getAlmacen().getNombre())
                        .ubicacion(ingreso.getAlmacen().getUbicacion())
                        .tipo(ingreso.getAlmacen().getTipo())
                        .estado(ingreso.getAlmacen().getEstado())
                        .fechaRegistro(ingreso.getAlmacen().getFechaRegistro())
                        .build());
            }

            dtoBuilder.ingresoMateriaPrima(ingresoBuilder.build());
        }

        // Incluir materiaPrima si está cargada
        if (detalleIngreso.getMateriaPrima() != null) {
            MateriaPrima materia = detalleIngreso.getMateriaPrima();
            dtoBuilder.materiaPrima(MateriaPrimaDTO.builder()
                    .id(materia.getId())
                    .nombre(materia.getNombre())
                    .descripcion(materia.getDescripcion())
                    .unidadMedida(materia.getUnidadMedida())
                    .stockMinimo(materia.getStockMinimo())
                    .estado(materia.getEstado())
                    .fechaRegistro(materia.getFechaRegistro())
                    .build());
        }

        return dtoBuilder.build();
    }

}