

package com.example.msalmacen.repository;

import com.example.msalmacen.entity.MateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaPrimaRepository extends JpaRepository<MateriaPrima, Long> {

    // Buscar materias primas activas
    List<MateriaPrima> findByEstadoTrue();

    // Buscar materias primas inactivas
    List<MateriaPrima> findByEstadoFalse();

    // Buscar por nombre (case insensitive)
    @Query("SELECT m FROM MateriaPrima m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<MateriaPrima> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);

    // Buscar por unidad de medida
    List<MateriaPrima> findByUnidadMedida(String unidadMedida);

    // Buscar materias primas activas por nombre
    @Query("SELECT m FROM MateriaPrima m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) AND m.estado = true")
    List<MateriaPrima> findByNombreContainingIgnoreCaseAndEstadoTrue(@Param("nombre") String nombre);

    // Verificar si existe una materia prima con el mismo nombre (para validación)
    boolean existsByNombreIgnoreCase(String nombre);

    // Verificar si existe una materia prima con el mismo nombre excluyendo un ID específico
    @Query("SELECT COUNT(m) > 0 FROM MateriaPrima m WHERE LOWER(m.nombre) = LOWER(:nombre) AND m.id != :id")
    boolean existsByNombreIgnoreCaseAndIdNot(@Param("nombre") String nombre, @Param("id") Long id);

    // Buscar materias primas con stock mínimo específico
    @Query("SELECT m FROM MateriaPrima m WHERE m.stockMinimo >= :stockMinimo AND m.estado = true")
    List<MateriaPrima> findByStockMinimoGreaterThanEqualAndEstadoTrue(@Param("stockMinimo") java.math.BigDecimal stockMinimo);
}