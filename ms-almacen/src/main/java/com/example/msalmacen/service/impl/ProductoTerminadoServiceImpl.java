package com.example.msalmacen.service.impl;

import com.example.msalmacen.dto.ComposicionDTO;
import com.example.msalmacen.dto.ProductoTerminadoDTO;
import com.example.msalmacen.entity.ComposicionProducto;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.entity.ProductoTerminado;
import com.example.msalmacen.repository.ComposicionProductoRepository;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import com.example.msalmacen.repository.ProductoTerminadoRepository;
import com.example.msalmacen.service.ProductoTerminadoService;
import com.example.msalmacen.util.MapperUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoTerminadoServiceImpl implements ProductoTerminadoService {

    private final ProductoTerminadoRepository productoRepo;
    private final ComposicionProductoRepository composicionRepo;
    private final MateriaPrimaRepository materiaRepo;
    private final MapperUtil mapper;

    public ProductoTerminadoServiceImpl(ProductoTerminadoRepository productoRepo,
                                        ComposicionProductoRepository composicionRepo,
                                        MateriaPrimaRepository materiaRepo,
                                        MapperUtil mapper) {
        this.productoRepo = productoRepo;
        this.composicionRepo = composicionRepo;
        this.materiaRepo = materiaRepo;
        this.mapper = mapper;
    }

    @Override
    public ProductoTerminadoDTO crearProductoTerminado(ProductoTerminadoDTO dto) {
        ProductoTerminado producto = new ProductoTerminado();
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setStockActual(dto.getStockActual());
        producto.setStockMinimo(dto.getStockMinimo());
        producto = productoRepo.save(producto);

        List<ComposicionProducto> composiciones = new ArrayList<>();

        for (ComposicionDTO c : dto.getComposiciones()) {
            MateriaPrima mp = materiaRepo.findById(c.getMateriaPrimaId())
                    .orElseThrow(() -> new EntityNotFoundException("Materia prima no encontrada"));

            Double cantidadRequerida = c.getCantidadNecesaria();

            if (mp.getStockActual() < cantidadRequerida) {
                throw new IllegalArgumentException("Stock insuficiente de materia prima");
            }

            Integer nuevoStock = (int) (mp.getStockActual() - cantidadRequerida);
            mp.setStockActual(nuevoStock);
            materiaRepo.save(mp);

            ComposicionProducto composicion = new ComposicionProducto();
            composicion.setProductoTerminado(producto);
            composicion.setMateriaPrima(mp);
            composicion.setCantidadNecesaria(cantidadRequerida);

            composiciones.add(composicion);
        }

        composicionRepo.saveAll(composiciones);
        return mapper.mapProductoTerminadoToDTO(producto, composiciones);
    }

    @Override
    public ProductoTerminadoDTO obtenerProductoTerminado(Long id) {
        ProductoTerminado producto = productoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        List<ComposicionProducto> composiciones = composicionRepo.findByProductoTerminadoId(id);
        return mapper.mapProductoTerminadoToDTO(producto, composiciones);
    }

    @Override
    public List<ProductoTerminadoDTO> listarTodos() {
        return productoRepo.findAll().stream()
                .map(p -> {
                    List<ComposicionProducto> compos = composicionRepo.findByProductoTerminadoId(p.getId());
                    return mapper.mapProductoTerminadoToDTO(p, compos);
                }).collect(Collectors.toList());
    }

    @Override
    public ProductoTerminadoDTO actualizarProductoTerminado(Long id, ProductoTerminadoDTO dto) {
        ProductoTerminado producto = productoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        productoRepo.save(producto);

        composicionRepo.deleteAll(composicionRepo.findByProductoTerminadoId(id));

        List<ComposicionProducto> nuevas = dto.getComposiciones().stream()
                .map(c -> {
                    MateriaPrima mp = materiaRepo.findById(c.getMateriaPrimaId())
                            .orElseThrow(() -> new EntityNotFoundException("Materia prima no encontrada"));
                    return ComposicionProducto.builder()
                            .productoTerminado(producto)
                            .materiaPrima(mp)
                            .cantidadNecesaria(c.getCantidadNecesaria())
                            .build();
                }).collect(Collectors.toList());

        composicionRepo.saveAll(nuevas);
        return mapper.mapProductoTerminadoToDTO(producto, nuevas);
    }

    @Override
    public void eliminarProductoTerminado(Long id) {
        composicionRepo.deleteAll(composicionRepo.findByProductoTerminadoId(id));
        productoRepo.deleteById(id);
    }

    @Override
    public ProductoTerminadoDTO descontarStock(Long id, int cantidad) {
        ProductoTerminado producto = productoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto terminado no encontrado"));

        int stockActual = producto.getStockActual();
        if (stockActual < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para el producto terminado");
        }

        producto.setStockActual(stockActual - cantidad);
        producto = productoRepo.save(producto);

        List<ComposicionProducto> composiciones = composicionRepo.findByProductoTerminadoId(id);
        return mapper.mapProductoTerminadoToDTO(producto, composiciones);
    }
}
