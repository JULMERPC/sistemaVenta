package com.example.msalmacen.repository;

import com.example.msalmacen.entity.IngresoMateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface IngresoMateriaPrimaRepository extends JpaRepository<IngresoMateriaPrima, Long> {

    // Buscar por proveedor
    List<IngresoMateriaPrima> findByProveedorId(Long proveedorId);

    // Buscar por almacén
    List<IngresoMateriaPrima> findByAlmacenId(Long almacenId);

    // Buscar por rango de fechas
    List<IngresoMateriaPrima> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar por tipo de documento
    List<IngresoMateriaPrima> findByTipoDocumento(String tipoDocumento);

    // Buscar por número de documento (único)
    Optional<IngresoMateriaPrima> findByNroDocumento(String nroDocumento);



    // Verificar si existe un número de documento
    boolean existsByNroDocumento(String nroDocumento);

    // Buscar por proveedor y rango de fechas
    List<IngresoMateriaPrima> findByProveedorIdAndFechaBetween(
            Long proveedorId, LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar por almacén y rango de fechas
    List<IngresoMateriaPrima> findByAlmacenIdAndFechaBetween(
            Long almacenId, LocalDate fechaInicio, LocalDate fechaFin);

    // Obtener total de ingresos por proveedor en un período
    @Query("SELECT SUM(i.total) FROM IngresoMateriaPrima i WHERE i.proveedorId = :proveedorId AND i.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumTotalByProveedorAndFecha(
            @Param("proveedorId") Long proveedorId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    // Obtener total de ingresos por almacén en un período
    @Query("SELECT SUM(i.total) FROM IngresoMateriaPrima i WHERE i.almacenId = :almacenId AND i.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumTotalByAlmacenAndFecha(
            @Param("almacenId") Long almacenId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    // Últimos ingresos por fecha
    List<IngresoMateriaPrima> findTop10ByOrderByFechaDescCreatedAtDesc();

    // Ingresos ordenados por fecha descendente
    List<IngresoMateriaPrima> findAllByOrderByFechaDescCreatedAtDesc();
}