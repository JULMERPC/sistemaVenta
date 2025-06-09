package com.example.msalmacen.repository;


import com.example.msalmacen.entity.IngresoMateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IngresoMateriaPrimaRepository extends JpaRepository<IngresoMateriaPrima, Long> {

    // Buscar por proveedor
    List<IngresoMateriaPrima> findByIdProveedor(Long idProveedor);

    // Buscar por almacén
    List<IngresoMateriaPrima> findByAlmacenId(Long idAlmacen);

    // Buscar por documento de referencia
    List<IngresoMateriaPrima> findByDocumentoRefContainingIgnoreCase(String documentoRef);

    // Buscar por rango de fechas
    @Query("SELECT i FROM IngresoMateriaPrima i WHERE i.fechaIngreso BETWEEN :fechaInicio AND :fechaFin")
    List<IngresoMateriaPrima> findByFechaIngresoBetween(@Param("fechaInicio") LocalDateTime fechaInicio,
                                                        @Param("fechaFin") LocalDateTime fechaFin);

    // Buscar por proveedor y almacén
    List<IngresoMateriaPrima> findByIdProveedorAndAlmacenId(Long idProveedor, Long idAlmacen);

    // Buscar por proveedor en un rango de fechas
    @Query("SELECT i FROM IngresoMateriaPrima i WHERE i.idProveedor = :idProveedor AND i.fechaIngreso BETWEEN :fechaInicio AND :fechaFin")
    List<IngresoMateriaPrima> findByProveedorAndFechaRange(@Param("idProveedor") Long idProveedor,
                                                           @Param("fechaInicio") LocalDateTime fechaInicio,
                                                           @Param("fechaFin") LocalDateTime fechaFin);

    // Buscar los últimos ingresos (ordenados por fecha descendente)
    @Query("SELECT i FROM IngresoMateriaPrima i ORDER BY i.fechaIngreso DESC")
    List<IngresoMateriaPrima> findAllOrderByFechaIngresoDesc();

    // Contar ingresos por proveedor
    @Query("SELECT COUNT(i) FROM IngresoMateriaPrima i WHERE i.idProveedor = :idProveedor")
    Long countByIdProveedor(@Param("idProveedor") Long idProveedor);

    // Contar ingresos por almacén
    @Query("SELECT COUNT(i) FROM IngresoMateriaPrima i WHERE i.almacen.id = :idAlmacen")
    Long countByAlmacenId(@Param("idAlmacen") Long idAlmacen);
}