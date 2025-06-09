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

    // Buscar stock por materia prima y almacén específicos
    Optional<StockAlmacen> findByMateriaPrimaIdAndAlmacenId(Long materiaPrimaId, Long almacenId);

    // Obtener todos los stocks de una materia prima en todos los almacenes
    List<StockAlmacen> findByMateriaPrimaId(Long materiaPrimaId);

    // Obtener todos los stocks de un almacén específico
    List<StockAlmacen> findByAlmacenId(Long almacenId);

    // Obtener stocks con cantidad mayor a cero
    @Query("SELECT s FROM StockAlmacen s WHERE s.stockActual > 0")
    List<StockAlmacen> findStocksConExistencias();

    // Obtener stocks con cantidad mayor a cero por almacén
    @Query("SELECT s FROM StockAlmacen s WHERE s.almacen.id = :almacenId AND s.stockActual > 0")
    List<StockAlmacen> findStocksConExistenciasByAlmacen(@Param("almacenId") Long almacenId);

    // Obtener stocks bajo mínimo (comparando con stock mínimo de materia prima)
    @Query("SELECT s FROM StockAlmacen s WHERE s.stockActual < s.materiaPrima.stockMinimo")
    List<StockAlmacen> findStocksBajoMinimo();

    // Obtener stocks bajo mínimo por almacén
    @Query("SELECT s FROM StockAlmacen s WHERE s.almacen.id = :almacenId AND s.stockActual < s.materiaPrima.stockMinimo")
    List<StockAlmacen> findStocksBajoMinimoPorAlmacen(@Param("almacenId") Long almacenId);

    // Verificar si existe stock suficiente para una materia prima en un almacén
    @Query("SELECT CASE WHEN s.stockActual >= :cantidadRequerida THEN true ELSE false END " +
            "FROM StockAlmacen s WHERE s.materiaPrima.id = :materiaPrimaId AND s.almacen.id = :almacenId")
    Boolean existeStockSuficiente(@Param("materiaPrimaId") Long materiaPrimaId,
                                  @Param("almacenId") Long almacenId,
                                  @Param("cantidadRequerida") BigDecimal cantidadRequerida);

    // Obtener stock total de una materia prima en todos los almacenes
    @Query("SELECT COALESCE(SUM(s.stockActual), 0) FROM StockAlmacen s WHERE s.materiaPrima.id = :materiaPrimaId")
    BigDecimal obtenerStockTotalMateriaPrima(@Param("materiaPrimaId") Long materiaPrimaId);

    // Obtener información completa de stocks con JOIN para evitar N+1 queries
    @Query("SELECT s FROM StockAlmacen s " +
            "JOIN FETCH s.materiaPrima m " +
            "JOIN FETCH s.almacen a " +
            "WHERE s.stockActual > 0 " +
            "ORDER BY m.nombre, a.nombre")
    List<StockAlmacen> findStocksConDetallesCompletos();

    // Obtener información completa de stocks por almacén
    @Query("SELECT s FROM StockAlmacen s " +
            "JOIN FETCH s.materiaPrima m " +
            "JOIN FETCH s.almacen a " +
            "WHERE a.id = :almacenId " +
            "ORDER BY m.nombre")
    List<StockAlmacen> findStocksConDetallesPorAlmacen(@Param("almacenId") Long almacenId);

    // Obtener stocks de materias primas activas
    @Query("SELECT s FROM StockAlmacen s " +
            "JOIN FETCH s.materiaPrima m " +
            "JOIN FETCH s.almacen a " +
            "WHERE m.activo = true AND a.estado = true " +
            "ORDER BY m.nombre, a.nombre")
    List<StockAlmacen> findStocksActivosConDetalles();

    // Buscar stocks por código o nombre de materia prima
    @Query("SELECT s FROM StockAlmacen s " +
            "JOIN FETCH s.materiaPrima m " +
            "JOIN FETCH s.almacen a " +
            "WHERE (LOWER(m.codigo) LIKE LOWER(CONCAT('%', :busqueda, '%')) " +
            "OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%'))) " +
            "AND m.activo = true " +
            "ORDER BY m.nombre, a.nombre")
    List<StockAlmacen> buscarStocksPorMateriaPrima(@Param("busqueda") String busqueda);

    // Eliminar registros con stock cero (para limpieza)
    @Query("DELETE FROM StockAlmacen s WHERE s.stockActual = 0")
    void eliminarStocksCero();

    // Verificar si existe registro para materia prima y almacén
    boolean existsByMateriaPrimaIdAndAlmacenId(Long materiaPrimaId, Long almacenId);
}