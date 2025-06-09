package com.example.msalmacen.service;

import com.example.msalmacen.dto.ProductoTerminadoDTO;
import java.util.List;

public interface ProductoTerminadoService {
    ProductoTerminadoDTO crearProductoTerminado(ProductoTerminadoDTO dto);
    ProductoTerminadoDTO obtenerProductoTerminado(Long id);
    List<ProductoTerminadoDTO> listarTodos();
    ProductoTerminadoDTO actualizarProductoTerminado(Long id, ProductoTerminadoDTO dto);
    void eliminarProductoTerminado(Long id);

    ProductoTerminadoDTO descontarStock(Long id, int cantidad);
}
