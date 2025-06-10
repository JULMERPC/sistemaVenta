

package com.example.msalmacen.repository;

import com.example.msalmacen.entity.StockAlmacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockAlmacenRepository extends JpaRepository<StockAlmacen, Long> {

    // Buscar stock por materia prima y almacén
    Optional<StockAlmacen> findByMateriaPrimaIdAndAlmacenId(Long materiaPrimaId, Long almacenId);

    // Buscar todos los stocks de un almacén
    List<StockAlmacen> findByAlmacenId(Long almacenId);

    // Buscar todos los stocks de una materia prima
    List<StockAlmacen> findByMateriaPrimaId(Long materiaPrimaId);

    // Buscar stocks con stock actual mayor a cero
    @Query("SELECT s FROM StockAlmacen s WHERE s.stockActual > 0")
    List<StockAlmacen> findStocksDisponibles();

    // Buscar stocks bajos (donde stock actual < stock mínimo)
    @Query("SELECT s FROM StockAlmacen s WHERE s.stockActual < s.materiaPrima.stockMinimo")
    List<StockAlmacen> findStocksBajos();

    // Buscar stocks de un almacén con stock disponible
    @Query("SELECT s FROM StockAlmacen s WHERE s.almacen.id = :almacenId AND s.stockActual > 0")
    List<StockAlmacen> findStocksDisponiblesPorAlmacen(@Param("almacenId") Long almacenId);

    // Buscar stocks bajos de un almacén específico
    @Query("SELECT s FROM StockAlmacen s WHERE s.almacen.id = :almacenId AND s.stockActual < s.materiaPrima.stockMinimo")
    List<StockAlmacen> findStocksBajosPorAlmacen(@Param("almacenId") Long almacenId);

    // Verificar si existe stock para una combinación materia prima - almacén
    boolean existsByMateriaPrimaIdAndAlmacenId(Long materiaPrimaId, Long almacenId);

    // Obtener stock total de una materia prima en todos los almacenes
    @Query("SELECT COALESCE(SUM(s.stockActual), 0) FROM StockAlmacen s WHERE s.materiaPrima.id = :materiaPrimaId")
    BigDecimal getTotalStockMateriaPrima(@Param("materiaPrimaId") Long materiaPrimaId);

    // Buscar stocks con información completa (JOIN FETCH para evitar N+1)
    @Query("SELECT s FROM StockAlmacen s " +
            "JOIN FETCH s.materiaPrima " +
            "JOIN FETCH s.almacen " +
            "WHERE s.almacen.id = :almacenId")
    List<StockAlmacen> findByAlmacenIdWithDetails(@Param("almacenId") Long almacenId);

    // Buscar todos los stocks con información completa
    @Query("SELECT s FROM StockAlmacen s " +
            "JOIN FETCH s.materiaPrima " +
            "JOIN FETCH s.almacen")
    List<StockAlmacen> findAllWithDetails();

    // Buscar stocks por nombre de materia prima (búsqueda parcial)
    @Query("SELECT s FROM StockAlmacen s " +
            "JOIN FETCH s.materiaPrima mp " +
            "JOIN FETCH s.almacen " +
            "WHERE LOWER(mp.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<StockAlmacen> findByMateriaPrimaNombreContainingIgnoreCase(@Param("nombre") String nombre);

    // Buscar stocks por almacén activo
    @Query("SELECT s FROM StockAlmacen s " +
            "JOIN FETCH s.materiaPrima " +
            "JOIN FETCH s.almacen a " +
            "WHERE a.estado = true")
    List<StockAlmacen> findByAlmacenesActivos();

    // Buscar stocks de materias primas activas
    @Query("SELECT s FROM StockAlmacen s " +
            "JOIN FETCH s.materiaPrima mp " +
            "JOIN FETCH s.almacen " +
            "WHERE mp.estado = true")
    List<StockAlmacen> findByMateriasPrimasActivas();
}