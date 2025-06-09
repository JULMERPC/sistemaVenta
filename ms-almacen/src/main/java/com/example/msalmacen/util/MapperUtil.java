package com.example.msalmacen.util;

import com.example.msalmacen.dto.ComposicionDTO;
import com.example.msalmacen.dto.MateriaPrimaDTO;

import com.example.msalmacen.dto.ProductoTerminadoDTO;
import com.example.msalmacen.entity.ComposicionProducto;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.entity.ProductoTerminado;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperUtil {

    public MateriaPrimaDTO entityToDto(MateriaPrima entity) {
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

    public MateriaPrima dtoToEntity(MateriaPrimaDTO dto) {
        return MateriaPrima.builder()
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .tipoTela(dto.getTipoTela())
                .color(dto.getColor())
                .anchoCm(dto.getAnchoCm())
                .pesoGsm(dto.getPesoGsm())
                .precioMetro(dto.getPrecioMetro())
                .stockActual(dto.getStockActual())
                .stockMinimo(dto.getStockMinimo())
                .unidad(dto.getUnidad())
                .activo(dto.getActivo())
                .fechaRegistro(dto.getFechaRegistro())
                .build();
    }

    public ProductoTerminadoDTO mapProductoTerminadoToDTO(ProductoTerminado producto, List<ComposicionProducto> composiciones) {
        ProductoTerminadoDTO dto = new ProductoTerminadoDTO();
        dto.setId(producto.getId());
        dto.setCodigo(producto.getCodigo());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setStockActual(producto.getStockActual());   // ← asigna stockActual
        dto.setStockMinimo(producto.getStockMinimo());   // ← asigna stockMinimo
        dto.setComposiciones(
                composiciones.stream().map(comp -> {
                    ComposicionDTO cdto = new ComposicionDTO();
                    cdto.setMateriaPrimaId(comp.getMateriaPrima().getId());
                    cdto.setCantidadNecesaria(comp.getCantidadNecesaria());
                    return cdto;
                }).collect(Collectors.toList())
        );
        return dto;
    }

}

