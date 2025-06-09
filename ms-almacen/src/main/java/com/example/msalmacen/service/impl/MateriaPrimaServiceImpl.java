//package com.example.msalmacen.service.impl;
//
//import com.example.msalmacen.client.ProveedorClient;
//import com.example.msalmacen.dto.MateriaPrimaDTO;
//import com.example.msalmacen.entity.MateriaPrima;
//import com.example.msalmacen.repository.MateriaPrimaRepository;
//import com.example.msalmacen.service.MateriaPrimaService;
//import com.example.msalmacen.util.MapperUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class MateriaPrimaServiceImpl implements MateriaPrimaService {
//
//    @Autowired
//    private MateriaPrimaRepository repository;
//
//    @Autowired
//    private ProveedorClient proveedorClient;
//
//    @Autowired
//    private MapperUtil mapper;
//    @Autowired
//    private MateriaPrimaRepository productoFinalRepository;
//
//    @Override
//    public MateriaPrimaDTO guardar(MateriaPrimaDTO dto) {
//        try {
//            MateriaPrima producto = convertirAEntidad(dto);
//
//            // Establecer fecha de registro si no existe
//            if (producto.getFechaRegistro() == null) {
//                producto.setFechaRegistro(LocalDateTime.now());
//            }
//
//            // Establecer activo por defecto
//            if (producto.getActivo() == null) {
//                producto.setActivo(true);
//            }
//
//            // Validar que el código no exista
//            if (productoFinalRepository.existsByCodigo(dto.getCodigo())) {
//                throw new RuntimeException("Ya existe un producto con el código: " + dto.getCodigo());
//            }
//
//            MateriaPrima productoGuardado = productoFinalRepository.save(producto);
//            return convertirADTO(productoGuardado);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error al guardar el producto: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public List<MateriaPrimaDTO> listar() {
//        try {
//            return productoFinalRepository.findAll()
//                    .stream()
//                    .map(this::convertirADTO)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            throw new RuntimeException("Error al listar productos: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public MateriaPrimaDTO obtenerPorCodigo(String codigo) {
//        try {
//            MateriaPrima producto = productoFinalRepository.findByCodigo(codigo)
//                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con código: " + codigo));
//            return convertirADTO(producto);
//        } catch (Exception e) {
//            throw new RuntimeException("Error al obtener producto: " + e.getMessage());
//        }
//    }
//
//    // Métodos adicionales que pueden ser útiles
//    public List<MateriaPrimaDTO> listarActivos() {
//        try {
//            return productoFinalRepository.findByActivoTrue()
//                    .stream()
//                    .map(this::convertirADTO)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            throw new RuntimeException("Error al listar productos activos: " + e.getMessage());
//        }
//    }
//
//    public List<MateriaPrimaDTO> obtenerProductosBajoStock() {
//        try {
//            return productoFinalRepository.findProductosBajoStock()
//                    .stream()
//                    .map(this::convertirADTO)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            throw new RuntimeException("Error al obtener productos bajo stock: " + e.getMessage());
//        }
//    }
//
//    public MateriaPrimaDTO actualizarStock(String codigo, Integer nuevoStock) {
//        try {
//            MateriaPrima producto = productoFinalRepository.findByCodigo(codigo)
//                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
//
//            producto.setStockActual(nuevoStock);
//            MateriaPrima productoActualizado = productoFinalRepository.save(producto);
//
//            return convertirADTO(productoActualizado);
//        } catch (Exception e) {
//            throw new RuntimeException("Error al actualizar stock: " + e.getMessage());
//        }
//    }
//
//    // Método que integra información del proveedor
//    public String obtenerInfoProductoConProveedor(String codigoProducto, Long proveedorId) {
//        try {
//            MateriaPrimaDTO producto = obtenerPorCodigo(codigoProducto);
//            ProveedorDTO proveedor = proveedorClient.obtenerProveedor(proveedorId);
//
//            return String.format(
//                    "Producto: %s (%s) - Stock: %d %s - Proveedor: %s - Ciudad: %s",
//                    producto.getNombre(),
//                    producto.getCodigo(),
//                    producto.getStockActual(),
//                    producto.getUnidad(),
//                    proveedor.getNombreEmpresa(),
//                    proveedor.getCiudad()
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Error al obtener información completa: " + e.getMessage());
//        }
//    }
//
//    // Método para verificar proveedores disponibles
//    public List<ProveedorDTO> obtenerProveedoresDisponibles() {
//        try {
//            return proveedorClient.obtenerProveedoresActivos();
//        } catch (Exception e) {
//            throw new RuntimeException("Error al obtener proveedores: " + e.getMessage());
//        }
//    }
//
//    // Métodos de conversión
//    private MateriaPrima convertirAEntidad(MateriaPrimaDTO dto) {
//        return MateriaPrima.builder()
//                .codigo(dto.getCodigo())
//                .nombre(dto.getNombre())
//                .descripcion(dto.getDescripcion())
//                .tipoTela(dto.getTipoTela())
//                .color(dto.getColor())
//                .anchoCm(dto.getAnchoCm())
//                .pesoGsm(dto.getPesoGsm())
//                .precioMetro(dto.getPrecioMetro())
//                .stockActual(dto.getStockActual() != null ? dto.getStockActual() : 0)
//                .stockMinimo(dto.getStockMinimo() != null ? dto.getStockMinimo() : 0)
//                .unidad(dto.getUnidad() != null ? dto.getUnidad() : "METROS")
//                .activo(dto.getActivo() != null ? dto.getActivo() : true)
//                .fechaRegistro(dto.getFechaRegistro() != null ? dto.getFechaRegistro() : LocalDateTime.now())
//                .build();
//    }
//
//    private MateriaPrimaDTO convertirADTO(MateriaPrima entity) {
//        return MateriaPrimaDTO.builder()
//                .codigo(entity.getCodigo())
//                .nombre(entity.getNombre())
//                .descripcion(entity.getDescripcion())
//                .tipoTela(entity.getTipoTela())
//                .color(entity.getColor())
//                .anchoCm(entity.getAnchoCm())
//                .pesoGsm(entity.getPesoGsm())
//                .precioMetro(entity.getPrecioMetro())
//                .stockActual(entity.getStockActual())
//                .stockMinimo(entity.getStockMinimo())
//                .unidad(entity.getUnidad())
//                .activo(entity.getActivo())
//                .fechaRegistro(entity.getFechaRegistro())
//                .build();
//    }
//}

package com.example.msalmacen.service.impl;

import com.example.msalmacen.dto.MateriaPrimaDTO;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import com.example.msalmacen.service.MateriaPrimaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MateriaPrimaServiceImpl implements MateriaPrimaService {

    private final MateriaPrimaRepository materiaPrimaRepository;

    // Obtener todas las materias primas
    @Override
    public List<MateriaPrimaDTO> obtenerTodas() {
        log.info("Obteniendo todas las materias primas");
        List<MateriaPrima> materiasPrimas = materiaPrimaRepository.findAll();
        return materiasPrimas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener materias primas activas
    @Override
    public List<MateriaPrimaDTO> obtenerActivas() {
        log.info("Obteniendo materias primas activas");
        List<MateriaPrima> materiasPrimas = materiaPrimaRepository.findByEstadoTrue();
        return materiasPrimas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener materia prima por ID
    @Override
    public Optional<MateriaPrimaDTO> obtenerPorId(Long id) {
        log.info("Obteniendo materia prima con ID: {}", id);
        return materiaPrimaRepository.findById(id)
                .map(this::convertirADTO);
    }

    // Crear nueva materia prima
    @Override
    public MateriaPrimaDTO crear(MateriaPrimaDTO materiaPrimaDTO) {
        log.info("Creando nueva materia prima: {}", materiaPrimaDTO.getNombre());

        // Validar que no exista una materia prima con el mismo nombre
        if (materiaPrimaRepository.existsByNombreIgnoreCase(materiaPrimaDTO.getNombre())) {
            throw new IllegalArgumentException("Ya existe una materia prima con el nombre: " + materiaPrimaDTO.getNombre());
        }

        // Validaciones de negocio
        validarDatos(materiaPrimaDTO);

        MateriaPrima materiaPrima = convertirAEntidad(materiaPrimaDTO);
        materiaPrima.setFechaRegistro(LocalDateTime.now());

        MateriaPrima materiaPrimaGuardada = materiaPrimaRepository.save(materiaPrima);
        log.info("Materia prima creada exitosamente con ID: {}", materiaPrimaGuardada.getId());

        return convertirADTO(materiaPrimaGuardada);
    }

    // Actualizar materia prima
    @Override
    public MateriaPrimaDTO actualizar(Long id, MateriaPrimaDTO materiaPrimaDTO) {
        log.info("Actualizando materia prima con ID: {}", id);

        MateriaPrima materiaPrimaExistente = materiaPrimaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Materia prima no encontrada con ID: " + id));

        // Validar que no exista otra materia prima con el mismo nombre
        if (materiaPrimaRepository.existsByNombreIgnoreCaseAndIdNot(materiaPrimaDTO.getNombre(), id)) {
            throw new IllegalArgumentException("Ya existe otra materia prima con el nombre: " + materiaPrimaDTO.getNombre());
        }

        // Validaciones de negocio
        validarDatos(materiaPrimaDTO);

        // Actualizar campos
        materiaPrimaExistente.setNombre(materiaPrimaDTO.getNombre());
        materiaPrimaExistente.setDescripcion(materiaPrimaDTO.getDescripcion());
        materiaPrimaExistente.setUnidadMedida(materiaPrimaDTO.getUnidadMedida());
        materiaPrimaExistente.setStockMinimo(materiaPrimaDTO.getStockMinimo());
        materiaPrimaExistente.setEstado(materiaPrimaDTO.getEstado());

        MateriaPrima materiaPrimaActualizada = materiaPrimaRepository.save(materiaPrimaExistente);
        log.info("Materia prima actualizada exitosamente");

        return convertirADTO(materiaPrimaActualizada);
    }

    // Eliminar materia prima (eliminación lógica)
    @Override
    public void eliminar(Long id) {
        log.info("Eliminando materia prima con ID: {}", id);

        MateriaPrima materiaPrima = materiaPrimaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Materia prima no encontrada con ID: " + id));

        materiaPrima.setEstado(false);
        materiaPrimaRepository.save(materiaPrima);
        log.info("Materia prima eliminada exitosamente");
    }

    // Buscar por nombre
    @Override
    public List<MateriaPrimaDTO> buscarPorNombre(String nombre) {
        log.info("Buscando materias primas por nombre: {}", nombre);
        List<MateriaPrima> materiasPrimas = materiaPrimaRepository.findByNombreContainingIgnoreCase(nombre);
        return materiasPrimas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Buscar por unidad de medida
    @Override
    public List<MateriaPrimaDTO> buscarPorUnidadMedida(String unidadMedida) {
        log.info("Buscando materias primas por unidad de medida: {}", unidadMedida);
        List<MateriaPrima> materiasPrimas = materiaPrimaRepository.findByUnidadMedida(unidadMedida);
        return materiasPrimas.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Activar materia prima
    @Override
    public MateriaPrimaDTO activar(Long id) {
        log.info("Activando materia prima con ID: {}", id);

        MateriaPrima materiaPrima = materiaPrimaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Materia prima no encontrada con ID: " + id));

        materiaPrima.setEstado(true);
        MateriaPrima materiaPrimaActivada = materiaPrimaRepository.save(materiaPrima);
        log.info("Materia prima activada exitosamente");

        return convertirADTO(materiaPrimaActivada);
    }

    // Métodos de utilidad privados
    private void validarDatos(MateriaPrimaDTO materiaPrimaDTO) {
        if (materiaPrimaDTO.getNombre() == null || materiaPrimaDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la materia prima es obligatorio");
        }

        if (materiaPrimaDTO.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre de la materia prima no puede exceder 100 caracteres");
        }

        if (materiaPrimaDTO.getUnidadMedida() == null || materiaPrimaDTO.getUnidadMedida().trim().isEmpty()) {
            throw new IllegalArgumentException("La unidad de medida es obligatoria");
        }

        if (materiaPrimaDTO.getUnidadMedida().length() > 20) {
            throw new IllegalArgumentException("La unidad de medida no puede exceder 20 caracteres");
        }

        if (materiaPrimaDTO.getStockMinimo() == null) {
            throw new IllegalArgumentException("El stock mínimo es obligatorio");
        }

        if (materiaPrimaDTO.getStockMinimo().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
        }

        if (materiaPrimaDTO.getEstado() == null) {
            materiaPrimaDTO.setEstado(true); // Valor por defecto
        }
    }

    private MateriaPrimaDTO convertirADTO(MateriaPrima materiaPrima) {
        return MateriaPrimaDTO.builder()
                .id(materiaPrima.getId())
                .nombre(materiaPrima.getNombre())
                .descripcion(materiaPrima.getDescripcion())
                .unidadMedida(materiaPrima.getUnidadMedida())
                .stockMinimo(materiaPrima.getStockMinimo())
                .estado(materiaPrima.getEstado())
                .fechaRegistro(materiaPrima.getFechaRegistro())
                .build();
    }

    private MateriaPrima convertirAEntidad(MateriaPrimaDTO materiaPrimaDTO) {
        return MateriaPrima.builder()
                .id(materiaPrimaDTO.getId())
                .nombre(materiaPrimaDTO.getNombre())
                .descripcion(materiaPrimaDTO.getDescripcion())
                .unidadMedida(materiaPrimaDTO.getUnidadMedida())
                .stockMinimo(materiaPrimaDTO.getStockMinimo() != null ? materiaPrimaDTO.getStockMinimo() : BigDecimal.ZERO)
                .estado(materiaPrimaDTO.getEstado() != null ? materiaPrimaDTO.getEstado() : true)
                .fechaRegistro(materiaPrimaDTO.getFechaRegistro())
                .build();
    }
}