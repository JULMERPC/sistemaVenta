package com.example.msalmacen.repository;


import com.example.msalmacen.entity.DetalleIngreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleIngresoRepository extends JpaRepository<DetalleIngreso, Long> {

    // Buscar detalles por ID de ingreso
    List<DetalleIngreso> findByIngresoMateriaPrimaId(Long idIngreso);

    // Buscar detalles por ID de materia prima
    List<DetalleIngreso> findByMateriaPrimaId(Long idMateriaPrima);

    // Buscar detalles por ingreso y materia prima específica
    DetalleIngreso findByIngresoMateriaPrimaIdAndMateriaPrimaId(Long idIngreso, Long idMateriaPrima);

    // Query personalizada para obtener detalles con información completa
    @Query("SELECT d FROM DetalleIngreso d " +
            "JOIN FETCH d.ingresoMateriaPrima i " +
            "JOIN FETCH d.materiaPrima m " +
            "WHERE d.ingresoMateriaPrima.id = :idIngreso")
    List<DetalleIngreso> findDetallesConInformacionCompleta(@Param("idIngreso") Long idIngreso);

    // Query para obtener el total de un ingreso
    @Query("SELECT SUM(d.cantidad * d.costoUnitario) FROM DetalleIngreso d " +
            "WHERE d.ingresoMateriaPrima.id = :idIngreso")
    Double getTotalIngreso(@Param("idIngreso") Long idIngreso);

    // Query para verificar si existe un detalle específico
    boolean existsByIngresoMateriaPrimaIdAndMateriaPrimaId(Long idIngreso, Long idMateriaPrima);

    // Eliminar detalles por ID de ingreso
    void deleteByIngresoMateriaPrimaId(Long idIngreso);

    // Query para obtener estadísticas de materia prima
    @Query("SELECT d.materiaPrima.id, d.materiaPrima.nombre, SUM(d.cantidad), AVG(d.costoUnitario) " +
            "FROM DetalleIngreso d " +
            "GROUP BY d.materiaPrima.id, d.materiaPrima.nombre")
    List<Object[]> getEstadisticasMateriaPrima();
}