package com.example.msalmacen.repository;

import com.example.msalmacen.entity.DetalleIngreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DetalleIngresoRepository extends JpaRepository<DetalleIngreso, Long> {

    // Buscar todos los detalles de un ingreso específico
    @Query("SELECT di FROM DetalleIngreso di WHERE di.ingresoMateriaPrima.id = :ingresoId")
    List<DetalleIngreso> findByIngresoMateriaPrimaId(@Param("ingresoId") Long ingresoId);

    // Buscar todos los detalles de una materia prima específica
    @Query("SELECT di FROM DetalleIngreso di WHERE di.materiaPrima.id = :materiaPrimaId")
    List<DetalleIngreso> findByMateriaPrimaId(@Param("materiaPrimaId") Long materiaPrimaId);

    // Calcular el total de cantidad ingresada de una materia prima
    @Query("SELECT COALESCE(SUM(di.cantidad), 0) FROM DetalleIngreso di WHERE di.materiaPrima.id = :materiaPrimaId")
    BigDecimal getTotalCantidadByMateriaPrima(@Param("materiaPrimaId") Long materiaPrimaId);

    // Calcular el total de costo de un ingreso específico
    @Query("SELECT COALESCE(SUM(di.cantidad * di.costoUnitario), 0) FROM DetalleIngreso di WHERE di.ingresoMateriaPrima.id = :ingresoId")
    BigDecimal getTotalCostoByIngreso(@Param("ingresoId") Long ingresoId);

    // Buscar detalles con join fetch para evitar lazy loading
    @Query("SELECT di FROM DetalleIngreso di " +
            "JOIN FETCH di.ingresoMateriaPrima " +
            "JOIN FETCH di.materiaPrima " +
            "WHERE di.ingresoMateriaPrima.id = :ingresoId")
    List<DetalleIngreso> findByIngresoMateriaPrimaIdWithDetails(@Param("ingresoId") Long ingresoId);

    // Buscar detalles por rango de fechas
    @Query("SELECT di FROM DetalleIngreso di " +
            "JOIN di.ingresoMateriaPrima imp " +
            "WHERE imp.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<DetalleIngreso> findByFechaRange(@Param("fechaInicio") java.time.LocalDate fechaInicio,
                                          @Param("fechaFin") java.time.LocalDate fechaFin);

    // Verificar si existe un detalle para un ingreso y materia prima específicos
    @Query("SELECT COUNT(di) > 0 FROM DetalleIngreso di " +
            "WHERE di.ingresoMateriaPrima.id = :ingresoId AND di.materiaPrima.id = :materiaPrimaId")
    boolean existsByIngresoAndMateriaPrima(@Param("ingresoId") Long ingresoId,
                                           @Param("materiaPrimaId") Long materiaPrimaId);

    // Obtener el último costo unitario de una materia prima
    @Query("SELECT di.costoUnitario FROM DetalleIngreso di " +
            "JOIN di.ingresoMateriaPrima imp " +
            "WHERE di.materiaPrima.id = :materiaPrimaId " +
            "ORDER BY imp.fecha DESC, di.createdAt DESC")
    List<BigDecimal> findLastCostoUnitarioByMateriaPrima(@Param("materiaPrimaId") Long materiaPrimaId);

    // Buscar los detalles más recientes de una materia prima
    @Query("SELECT di FROM DetalleIngreso di " +
            "JOIN di.ingresoMateriaPrima imp " +
            "WHERE di.materiaPrima.id = :materiaPrimaId " +
            "ORDER BY imp.fecha DESC, di.createdAt DESC")
    List<DetalleIngreso> findRecentDetailsByMateriaPrima(@Param("materiaPrimaId") Long materiaPrimaId);
}