package com.example.ms.venta.repository;

import com.example.ms.venta.dto.VentaDto;
import com.example.ms.venta.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Buscar ventas por cliente
    List<Venta> findByClienteId(Long clienteId);

    // Buscar ventas por rango de fechas
    List<Venta> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar ventas por forma de pago
    List<Venta> findByFormaPago(String formaPago);

    // Buscar ventas por cliente y rango de fechas
    List<Venta> findByClienteIdAndFechaBetween(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar ventas por fecha específica
    List<Venta> findByFecha(LocalDate fecha);

    // Buscar ventas con total mayor a un valor específico
    List<Venta> findByTotalGreaterThan(BigDecimal total);

    // Buscar ventas con total menor a un valor específico
    List<Venta> findByTotalLessThan(BigDecimal total);

    // Buscar ventas en un rango de totales
    List<Venta> findByTotalBetween(BigDecimal totalMin, BigDecimal totalMax);

    // Obtener ventas ordenadas por fecha descendente
    List<Venta> findAllByOrderByFechaDescCreatedAtDesc();

    // Obtener ventas ordenadas por total descendente
    List<Venta> findAllByOrderByTotalDesc();

    // Obtener suma total de ventas por cliente
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.clienteId = :clienteId")
    BigDecimal sumTotalByClienteId(@Param("clienteId") Long clienteId);

    // Obtener suma total de ventas por fecha
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fecha = :fecha")
    BigDecimal sumTotalByFecha(@Param("fecha") LocalDate fecha);

    // Obtener suma total de ventas por rango de fechas
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumTotalByFechaBetween(@Param("fechaInicio") LocalDate fechaInicio,
                                      @Param("fechaFin") LocalDate fechaFin);

    // Obtener suma total de ventas por forma de pago
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.formaPago = :formaPago")
    BigDecimal sumTotalByFormaPago(@Param("formaPago") String formaPago);

    // Contar ventas por cliente
    long countByClienteId(Long clienteId);

    // Contar ventas por fecha
    long countByFecha(LocalDate fecha);

    // Contar ventas por forma de pago
    long countByFormaPago(String formaPago);

    // Obtener las ventas más recientes (últimas N ventas)
    @Query("SELECT v FROM Venta v ORDER BY v.createdAt DESC")
    List<Venta> findRecentVentas();

    // Obtener ventas del día actual
    @Query("SELECT v FROM Venta v WHERE v.fecha = CURRENT_DATE ORDER BY v.createdAt DESC")
    List<Venta> findVentasHoy();

    // Obtener ventas del mes actual
    @Query("SELECT v FROM Venta v WHERE MONTH(v.fecha) = MONTH(CURRENT_DATE) AND YEAR(v.fecha) = YEAR(CURRENT_DATE)")
    List<Venta> findVentasMesActual();

    // Obtener ventas del año actual
    @Query("SELECT v FROM Venta v WHERE YEAR(v.fecha) = YEAR(CURRENT_DATE)")
    List<Venta> findVentasAnioActual();

    // Buscar ventas por múltiples formas de pago
    List<Venta> findByFormaPagoIn(List<String> formasPago);

    @Query("SELECT new com.example.ms.venta.dto.VentaDto(v.id, v.fecha, v.formaPago) FROM Venta v WHERE v.id = :ventaId")
    VentaDto findVentaDtoById(@Param("ventaId") Long ventaId);

}