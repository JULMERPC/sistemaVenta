package com.example.msalmacen.service;

public interface ProduccionService {
    void producirProducto(Long productoTerminadoId, Integer cantidad) throws Exception;
}
