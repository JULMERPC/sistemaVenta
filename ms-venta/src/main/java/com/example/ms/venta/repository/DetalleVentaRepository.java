package com.example.ms.venta.repository;

import com.example.ms.venta.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    // Buscar todos los detalles de una venta específica
    List<DetalleVenta> findByVentaId(Long ventaId);

    // Buscar todos los detalles que contengan una materia prima específica
    List<DetalleVenta> findByMateriaPrimaId(Long materiaPrimaId);

    // Buscar un detalle específico por venta y materia prima
    Optional<DetalleVenta> findByVentaIdAndMateriaPrimaId(Long ventaId, Long materiaPrimaId);

    // Verificar si existe un detalle para una venta y materia prima específica
    boolean existsByVentaIdAndMateriaPrimaId(Long ventaId, Long materiaPrimaId);

    // Contar cuántos detalles tiene una venta
    long countByVentaId(Long ventaId);

    // Eliminar todos los detalles de una venta
    void deleteByVentaId(Long ventaId);

    // Calcular el total de una venta sumando todos sus detalles
    @Query("SELECT COALESCE(SUM(dv.subtotal), 0) FROM DetalleVenta dv WHERE dv.ventaId = :ventaId")
    java.math.BigDecimal calcularTotalVenta(@Param("ventaId") Long ventaId);

    // Buscar detalles ordenados por fecha de creación
    List<DetalleVenta> findByVentaIdOrderByCreatedAtAsc(Long ventaId);

    // Buscar los detalles más recientes de una venta
    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.ventaId = :ventaId ORDER BY dv.createdAt DESC")
    List<DetalleVenta> findDetallesRecientesByVenta(@Param("ventaId") Long ventaId);

    // Buscar detalles por rango de cantidad
    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.cantidad BETWEEN :cantidadMin AND :cantidadMax")
    List<DetalleVenta> findByCantidadBetween(@Param("cantidadMin") Integer cantidadMin,
                                             @Param("cantidadMax") Integer cantidadMax);

    // Buscar las materias primas más vendidas
    @Query("SELECT dv.materiaPrimaId, SUM(dv.cantidad) as totalVendido " +
            "FROM DetalleVenta dv " +
            "GROUP BY dv.materiaPrimaId " +
            "ORDER BY totalVendido DESC")
    List<Object[]> findMateriasPrimasMasVendidas();

    // Buscar detalles con subtotal mayor a un valor específico
    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.subtotal > :subtotalMinimo")
    List<DetalleVenta> findBySubtotalGreaterThan(@Param("subtotalMinimo") java.math.BigDecimal subtotalMinimo);
}