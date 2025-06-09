package com.example.msalmacen.service.impl;

import com.example.msalmacen.client.ProveedorClient;
import com.example.msalmacen.dto.MateriaPrimaDTO;
import com.example.msalmacen.dto.ProveedorDTO;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import com.example.msalmacen.service.MateriaPrimaService;
import com.example.msalmacen.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MateriaPrimaServiceImpl implements MateriaPrimaService {

    @Autowired
    private MateriaPrimaRepository repository;

    @Autowired
    private ProveedorClient proveedorClient;

    @Autowired
    private MapperUtil mapper;
    @Autowired
    private MateriaPrimaRepository productoFinalRepository;

    @Override
    public MateriaPrimaDTO guardar(MateriaPrimaDTO dto) {
        try {
            MateriaPrima producto = convertirAEntidad(dto);

            // Establecer fecha de registro si no existe
            if (producto.getFechaRegistro() == null) {
                producto.setFechaRegistro(LocalDateTime.now());
            }

            // Establecer activo por defecto
            if (producto.getActivo() == null) {
                producto.setActivo(true);
            }

            // Validar que el código no exista
            if (productoFinalRepository.existsByCodigo(dto.getCodigo())) {
                throw new RuntimeException("Ya existe un producto con el código: " + dto.getCodigo());
            }

            MateriaPrima productoGuardado = productoFinalRepository.save(producto);
            return convertirADTO(productoGuardado);

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el producto: " + e.getMessage());
        }
    }

    @Override
    public List<MateriaPrimaDTO> listar() {
        try {
            return productoFinalRepository.findAll()
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al listar productos: " + e.getMessage());
        }
    }

    @Override
    public MateriaPrimaDTO obtenerPorCodigo(String codigo) {
        try {
            MateriaPrima producto = productoFinalRepository.findByCodigo(codigo)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con código: " + codigo));
            return convertirADTO(producto);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener producto: " + e.getMessage());
        }
    }

    // Métodos adicionales que pueden ser útiles
    public List<MateriaPrimaDTO> listarActivos() {
        try {
            return productoFinalRepository.findByActivoTrue()
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al listar productos activos: " + e.getMessage());
        }
    }

    public List<MateriaPrimaDTO> obtenerProductosBajoStock() {
        try {
            return productoFinalRepository.findProductosBajoStock()
                    .stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener productos bajo stock: " + e.getMessage());
        }
    }

    public MateriaPrimaDTO actualizarStock(String codigo, Integer nuevoStock) {
        try {
            MateriaPrima producto = productoFinalRepository.findByCodigo(codigo)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            producto.setStockActual(nuevoStock);
            MateriaPrima productoActualizado = productoFinalRepository.save(producto);

            return convertirADTO(productoActualizado);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar stock: " + e.getMessage());
        }
    }

    // Método que integra información del proveedor
    public String obtenerInfoProductoConProveedor(String codigoProducto, Long proveedorId) {
        try {
            MateriaPrimaDTO producto = obtenerPorCodigo(codigoProducto);
            ProveedorDTO proveedor = proveedorClient.obtenerProveedor(proveedorId);

            return String.format(
                    "Producto: %s (%s) - Stock: %d %s - Proveedor: %s - Ciudad: %s",
                    producto.getNombre(),
                    producto.getCodigo(),
                    producto.getStockActual(),
                    producto.getUnidad(),
                    proveedor.getNombreEmpresa(),
                    proveedor.getCiudad()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener información completa: " + e.getMessage());
        }
    }

    // Método para verificar proveedores disponibles
    public List<ProveedorDTO> obtenerProveedoresDisponibles() {
        try {
            return proveedorClient.obtenerProveedoresActivos();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener proveedores: " + e.getMessage());
        }
    }

    // Métodos de conversión
    private MateriaPrima convertirAEntidad(MateriaPrimaDTO dto) {
        return MateriaPrima.builder()
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .tipoTela(dto.getTipoTela())
                .color(dto.getColor())
                .anchoCm(dto.getAnchoCm())
                .pesoGsm(dto.getPesoGsm())
                .precioMetro(dto.getPrecioMetro())
                .stockActual(dto.getStockActual() != null ? dto.getStockActual() : 0)
                .stockMinimo(dto.getStockMinimo() != null ? dto.getStockMinimo() : 0)
                .unidad(dto.getUnidad() != null ? dto.getUnidad() : "METROS")
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .fechaRegistro(dto.getFechaRegistro() != null ? dto.getFechaRegistro() : LocalDateTime.now())
                .build();
    }

    private MateriaPrimaDTO convertirADTO(MateriaPrima entity) {
        return MateriaPrimaDTO.builder()
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .tipoTela(entity.getTipoTela())
                .color(entity.getColor())
                .anchoCm(entity.getAnchoCm())
                .pesoGsm(entity.getPesoGsm())
                .precioMetro(entity.getPrecioMetro())
                .stockActual(entity.getStockActual())
                .stockMinimo(entity.getStockMinimo())
                .unidad(entity.getUnidad())
                .activo(entity.getActivo())
                .fechaRegistro(entity.getFechaRegistro())
                .build();
    }
}