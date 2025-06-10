//package com.example.msalmacen.service;
//
//
//import com.example.msalmacen.dto.DetalleIngresoDTO;
//import com.example.msalmacen.entity.DetalleIngreso;
//import com.example.msalmacen.entity.IngresoMateriaPrima;
//import com.example.msalmacen.entity.MateriaPrima;
//import com.example.msalmacen.repository.DetalleIngresoRepository;
//import com.example.msalmacen.repository.IngresoMateriaPrimaRepository;
//import com.example.msalmacen.repository.MateriaPrimaRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class DetalleIngresoService {
//
//    private final DetalleIngresoRepository detalleIngresoRepository;
//    private final IngresoMateriaPrimaRepository ingresoMateriaPrimaRepository;
//    private final MateriaPrimaRepository materiaPrimaRepository;
//
//    // Crear un nuevo detalle de ingreso
//    public DetalleIngresoDTO crearDetalleIngreso(DetalleIngresoDTO detalleIngresoDTO) {
//        // Validar que el ingreso existe
//        IngresoMateriaPrima ingreso = ingresoMateriaPrimaRepository.findById(detalleIngresoDTO.getIdIngreso())
//                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado con ID: " + detalleIngresoDTO.getIdIngreso()));
//
//        // Validar que la materia prima existe
//        MateriaPrima materiaPrima = materiaPrimaRepository.findById(detalleIngresoDTO.getIdMateriaPrima())
//                .orElseThrow(() -> new RuntimeException("Materia prima no encontrada con ID: " + detalleIngresoDTO.getIdMateriaPrima()));
//
//        // Verificar que no exista ya un detalle para esta combinación
//        if (detalleIngresoRepository.existsByIngresoMateriaPrimaIdAndMateriaPrimaId(
//                detalleIngresoDTO.getIdIngreso(), detalleIngresoDTO.getIdMateriaPrima())) {
//            throw new RuntimeException("Ya existe un detalle para esta materia prima en este ingreso");
//        }
//
//        // Crear la entidad
//        DetalleIngreso detalleIngreso = DetalleIngreso.builder()
//                .ingresoMateriaPrima(ingreso)
//                .materiaPrima(materiaPrima)
//                .cantidad(detalleIngresoDTO.getCantidad())
//                .costoUnitario(detalleIngresoDTO.getCostoUnitario())
//                .build();
//
//        // Guardar
//        DetalleIngreso detalleGuardado = detalleIngresoRepository.save(detalleIngreso);
//
//        // Actualizar stock de materia prima
//        actualizarStockMateriaPrima(materiaPrima, detalleIngresoDTO.getCantidad());
//
//        return convertirADTO(detalleGuardado);
//    }
//
//    // Obtener todos los detalles
//    @Transactional(readOnly = true)
//    public List<DetalleIngresoDTO> obtenerTodosLosDetalles() {
//        return detalleIngresoRepository.findAll().stream()
//                .map(this::convertirADTO)
//                .collect(Collectors.toList());
//    }
//
//    // Obtener detalle por ID
//    @Transactional(readOnly = true)
//    public Optional<DetalleIngresoDTO> obtenerDetallePorId(Long id) {
//        return detalleIngresoRepository.findById(id)
//                .map(this::convertirADTO);
//    }
//
//    // Obtener detalles por ID de ingreso
//    @Transactional(readOnly = true)
//    public List<DetalleIngresoDTO> obtenerDetallesPorIngreso(Long idIngreso) {
//        return detalleIngresoRepository.findByIngresoMateriaPrimaId(idIngreso).stream()
//                .map(this::convertirADTO)
//                .collect(Collectors.toList());
//    }
//
//    // Obtener detalles por ID de materia prima
//    @Transactional(readOnly = true)
//    public List<DetalleIngresoDTO> obtenerDetallesPorMateriaPrima(Long idMateriaPrima) {
//        return detalleIngresoRepository.findByMateriaPrimaId(idMateriaPrima).stream()
//                .map(this::convertirADTO)
//                .collect(Collectors.toList());
//    }
//
//    // Actualizar detalle de ingreso
//    public DetalleIngresoDTO actualizarDetalleIngreso(Long id, DetalleIngresoDTO detalleIngresoDTO) {
//        DetalleIngreso detalleExistente = detalleIngresoRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Detalle de ingreso no encontrado con ID: " + id));
//
//        // Guardar cantidad anterior para ajustar stock
//        BigDecimal cantidadAnterior = detalleExistente.getCantidad();
//
//        // Actualizar campos
//        detalleExistente.setCantidad(detalleIngresoDTO.getCantidad());
//        detalleExistente.setCostoUnitario(detalleIngresoDTO.getCostoUnitario());
//
//        // Guardar cambios
//        DetalleIngreso detalleActualizado = detalleIngresoRepository.save(detalleExistente);
//
//        // Ajustar stock de materia prima
//        BigDecimal diferenciaCantidad = detalleIngresoDTO.getCantidad().subtract(cantidadAnterior);
//        if (diferenciaCantidad.compareTo(BigDecimal.ZERO) != 0) {
//            actualizarStockMateriaPrima(detalleExistente.getMateriaPrima(), diferenciaCantidad);
//        }
//
//        return convertirADTO(detalleActualizado);
//    }
//
//    // Eliminar detalle de ingreso
//    public void eliminarDetalleIngreso(Long id) {
//        DetalleIngreso detalle = detalleIngresoRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Detalle de ingreso no encontrado con ID: " + id));
//
//        // Restar la cantidad del stock
//        BigDecimal cantidadARestar = detalle.getCantidad().negate();
//        actualizarStockMateriaPrima(detalle.getMateriaPrima(), cantidadARestar);
//
//        detalleIngresoRepository.delete(detalle);
//    }
//
//    // Obtener total de un ingreso
//    @Transactional(readOnly = true)
//    public Double obtenerTotalIngreso(Long idIngreso) {
//        Double total = detalleIngresoRepository.getTotalIngreso(idIngreso);
//        return total != null ? total : 0.0;
//    }
//
//    // Eliminar todos los detalles de un ingreso
//    public void eliminarDetallesPorIngreso(Long idIngreso) {
//        List<DetalleIngreso> detalles = detalleIngresoRepository.findByIngresoMateriaPrimaId(idIngreso);
//
//        // Ajustar stock para cada detalle
//        for (DetalleIngreso detalle : detalles) {
//            BigDecimal cantidadARestar = detalle.getCantidad().negate();
//            actualizarStockMateriaPrima(detalle.getMateriaPrima(), cantidadARestar);
//        }
//
//        detalleIngresoRepository.deleteByIngresoMateriaPrimaId(idIngreso);
//    }
//
//    // Método privado para actualizar stock de materia prima
//    private void actualizarStockMateriaPrima(MateriaPrima materiaPrima, BigDecimal cantidad) {
//        Integer stockActual = materiaPrima.getStockActual();
//        Integer nuevaCantidad = cantidad.intValue();
//        materiaPrima.setStockActual(stockActual + nuevaCantidad);
//        materiaPrimaRepository.save(materiaPrima);
//    }
//
//    // Método privado para convertir entidad a DTO
//    private DetalleIngresoDTO convertirADTO(DetalleIngreso detalleIngreso) {
//        DetalleIngresoDTO dto = DetalleIngresoDTO.builder()
//                .id(detalleIngreso.getId())
//                .idIngreso(detalleIngreso.getIngresoMateriaPrima().getId())
//                .idMateriaPrima(detalleIngreso.getMateriaPrima().getId())
//                .cantidad(detalleIngreso.getCantidad())
//                .costoUnitario(detalleIngreso.getCostoUnitario())
//                .build();
//
//        // Agregar información adicional
//        dto.setNombreMateriaPrima(detalleIngreso.getMateriaPrima().getNombre());
//        dto.setCodigoMateriaPrima(detalleIngreso.getMateriaPrima().getCodigo());
//        dto.setUnidadMateriaPrima(detalleIngreso.getMateriaPrima().getUnidad());
//        dto.setCostoTotal(detalleIngreso.getCostoTotal());
//
//        return dto;
//    }
//}


//
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
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class DetalleIngresoService {
//
//    private final DetalleIngresoRepository detalleIngresoRepository;
//    private final IngresoMateriaPrimaRepository ingresoMateriaPrimaRepository;
//    private final MateriaPrimaRepository materiaPrimaRepository;
//
//    // Crear un nuevo detalle de ingreso
//    public DetalleIngresoDTO crearDetalleIngreso(DetalleIngresoDTO detalleIngresoDTO) {
//        IngresoMateriaPrima ingreso = ingresoMateriaPrimaRepository.findById(detalleIngresoDTO.getIdIngreso())
//                .orElseThrow(() -> new RuntimeException("Ingreso no encontrado con ID: " + detalleIngresoDTO.getIdIngreso()));
//
//        MateriaPrima materiaPrima = materiaPrimaRepository.findById(detalleIngresoDTO.getIdMateriaPrima())
//                .orElseThrow(() -> new RuntimeException("Materia prima no encontrada con ID: " + detalleIngresoDTO.getIdMateriaPrima()));
//
//        if (detalleIngresoRepository.existsByIngresoMateriaPrimaIdAndMateriaPrimaId(
//                detalleIngresoDTO.getIdIngreso(), detalleIngresoDTO.getIdMateriaPrima())) {
//            throw new RuntimeException("Ya existe un detalle para esta materia prima en este ingreso");
//        }
//
//        DetalleIngreso detalleIngreso = DetalleIngreso.builder()
//                .ingresoMateriaPrima(ingreso)
//                .materiaPrima(materiaPrima)
//                .cantidad(detalleIngresoDTO.getCantidad())
//                .costoUnitario(detalleIngresoDTO.getCostoUnitario())
//                .build();
//
//        DetalleIngreso detalleGuardado = detalleIngresoRepository.save(detalleIngreso);
//
//        actualizarStockMateriaPrima(materiaPrima, detalleIngresoDTO.getCantidad());
//
//        return convertirADTO(detalleGuardado);
//    }
//
//    @Transactional(readOnly = true)
//    public List<DetalleIngresoDTO> obtenerTodosLosDetalles() {
//        return detalleIngresoRepository.findAll().stream()
//                .map(this::convertirADTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public Optional<DetalleIngresoDTO> obtenerDetallePorId(Long id) {
//        return detalleIngresoRepository.findById(id)
//                .map(this::convertirADTO);
//    }
//
//    @Transactional(readOnly = true)
//    public List<DetalleIngresoDTO> obtenerDetallesPorIngreso(Long idIngreso) {
//        return detalleIngresoRepository.findByIngresoMateriaPrimaId(idIngreso).stream()
//                .map(this::convertirADTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional(readOnly = true)
//    public List<DetalleIngresoDTO> obtenerDetallesPorMateriaPrima(Long idMateriaPrima) {
//        return detalleIngresoRepository.findByMateriaPrimaId(idMateriaPrima).stream()
//                .map(this::convertirADTO)
//                .collect(Collectors.toList());
//    }
//
//    public DetalleIngresoDTO actualizarDetalleIngreso(Long id, DetalleIngresoDTO detalleIngresoDTO) {
//        DetalleIngreso detalleExistente = detalleIngresoRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Detalle de ingreso no encontrado con ID: " + id));
//
//        BigDecimal cantidadAnterior = detalleExistente.getCantidad();
//
//        detalleExistente.setCantidad(detalleIngresoDTO.getCantidad());
//        detalleExistente.setCostoUnitario(detalleIngresoDTO.getCostoUnitario());
//
//        DetalleIngreso detalleActualizado = detalleIngresoRepository.save(detalleExistente);
//
//        BigDecimal diferenciaCantidad = detalleIngresoDTO.getCantidad().subtract(cantidadAnterior);
//        if (diferenciaCantidad.compareTo(BigDecimal.ZERO) != 0) {
//            actualizarStockMateriaPrima(detalleExistente.getMateriaPrima(), diferenciaCantidad);
//        }
//
//        return convertirADTO(detalleActualizado);
//    }
//
//    public void eliminarDetalleIngreso(Long id) {
//        DetalleIngreso detalle = detalleIngresoRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Detalle de ingreso no encontrado con ID: " + id));
//
//        BigDecimal cantidadARestar = detalle.getCantidad().negate();
//        actualizarStockMateriaPrima(detalle.getMateriaPrima(), cantidadARestar);
//
//        detalleIngresoRepository.delete(detalle);
//    }
//
//    @Transactional(readOnly = true)
//    public Double obtenerTotalIngreso(Long idIngreso) {
//        Double total = detalleIngresoRepository.getTotalIngreso(idIngreso);
//        return total != null ? total : 0.0;
//    }
//
//    public void eliminarDetallesPorIngreso(Long idIngreso) {
//        List<DetalleIngreso> detalles = detalleIngresoRepository.findByIngresoMateriaPrimaId(idIngreso);
//
//        for (DetalleIngreso detalle : detalles) {
//            BigDecimal cantidadARestar = detalle.getCantidad().negate();
//            actualizarStockMateriaPrima(detalle.getMateriaPrima(), cantidadARestar);
//        }
//
//        detalleIngresoRepository.deleteByIngresoMateriaPrimaId(idIngreso);
//    }
//
//    private void actualizarStockMateriaPrima(MateriaPrima materiaPrima, BigDecimal cantidad) {
//        BigDecimal stockActual = materiaPrima.getStockActual();
//        BigDecimal nuevoStock = stockActual.add(cantidad);
//        materiaPrima.setStockActual(nuevoStock);
//        materiaPrimaRepository.save(materiaPrima);
//    }
//
//    private DetalleIngresoDTO convertirADTO(DetalleIngreso detalleIngreso) {
//        DetalleIngresoDTO dto = DetalleIngresoDTO.builder()
//                .id(detalleIngreso.getId())
//                .idIngreso(detalleIngreso.getIngresoMateriaPrima().getId())
//                .idMateriaPrima(detalleIngreso.getMateriaPrima().getId())
//                .cantidad(detalleIngreso.getCantidad())
//                .costoUnitario(detalleIngreso.getCostoUnitario())
//                .build();
//
//        dto.setNombreMateriaPrima(detalleIngreso.getMateriaPrima().getNombre());
//        dto.setUnidadMateriaPrima(detalleIngreso.getMateriaPrima().getUnidadMedida());
//        dto.setCostoTotal(detalleIngreso.getCostoTotal());
//
//        return dto;
//    }
//}

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
        dto.setFechaIngreso(ingreso.getFechaIngreso());
        // Agregar más campos del ingreso según sea necesario
        // dto.setProveedorIngreso(ingreso.getProveedor());
        // dto.setNumeroDocumento(ingreso.getNumeroDocumento());

        return dto;
    }
}