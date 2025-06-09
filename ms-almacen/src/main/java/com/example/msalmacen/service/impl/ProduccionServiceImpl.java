package com.example.msalmacen.service.impl;

import com.example.msalmacen.entity.ComposicionProducto;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.entity.ProductoTerminado;
import com.example.msalmacen.repository.ComposicionProductoRepository;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import com.example.msalmacen.repository.ProductoTerminadoRepository;
import com.example.msalmacen.service.ProduccionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProduccionServiceImpl implements ProduccionService {

    private final ProductoTerminadoRepository productoTerminadoRepository;
    private final ComposicionProductoRepository composicionProductoRepository;
    private final MateriaPrimaRepository materiaPrimaRepository;

    public ProduccionServiceImpl(ProductoTerminadoRepository productoTerminadoRepository,
                                 ComposicionProductoRepository composicionProductoRepository,
                                 MateriaPrimaRepository materiaPrimaRepository) {
        this.productoTerminadoRepository = productoTerminadoRepository;
        this.composicionProductoRepository = composicionProductoRepository;
        this.materiaPrimaRepository = materiaPrimaRepository;
    }

    @Override
    @Transactional
    public void producirProducto(Long productoTerminadoId, Integer cantidad) throws Exception {
        ProductoTerminado productoTerminado = productoTerminadoRepository.findById(productoTerminadoId)
                .orElseThrow(() -> new Exception("Producto terminado no encontrado"));

        List<ComposicionProducto> composiciones = composicionProductoRepository.findByProductoTerminadoId(productoTerminadoId);

        for (ComposicionProducto composicion : composiciones) {
            MateriaPrima materiaPrima = materiaPrimaRepository.findById(composicion.getMateriaPrima().getId())
                    .orElseThrow(() -> new Exception("Materia prima no encontrada: " + composicion.getMateriaPrima().getId()));

            double cantidadNecesaria = composicion.getCantidadNecesaria() * cantidad; // metros necesarios totales

            if (materiaPrima.getStockActual() < cantidadNecesaria) {
                throw new Exception("Stock insuficiente de materia prima: " + materiaPrima.getNombre());
            }

            // Descontar materia prima usada
            int nuevoStock = materiaPrima.getStockActual() - (int) cantidadNecesaria;
            materiaPrima.setStockActual(nuevoStock);
            materiaPrimaRepository.save(materiaPrima);
        }

        // Incrementar stock de producto terminado
        int nuevoStockProducto = productoTerminado.getStockActual() + cantidad;
        productoTerminado.setStockActual(nuevoStockProducto);
        productoTerminadoRepository.save(productoTerminado);
    }
}
