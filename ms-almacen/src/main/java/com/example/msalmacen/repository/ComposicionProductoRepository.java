package com.example.msalmacen.repository;

import com.example.msalmacen.entity.ComposicionProducto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComposicionProductoRepository extends JpaRepository<ComposicionProducto, Long> {
    List<ComposicionProducto> findByProductoTerminadoId(Long productoId);
}
