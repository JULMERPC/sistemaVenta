//package com.example.msalmacen.repository;
//
//import com.example.msalmacen.entity.ProductoFinal;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface ProductoFinalRepository extends JpaRepository<ProductoFinal, Long> {
//    Optional<ProductoFinal> findByCodigo(String codigo);
//}

package com.example.msalmacen.repository;

import com.example.msalmacen.entity.MateriaPrima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaPrimaRepository extends JpaRepository<MateriaPrima, Long> {

    // Buscar por código
    Optional<MateriaPrima> findByCodigo(String codigo);

    // Verificar si existe por código
    boolean existsByCodigo(String codigo);

    // Buscar productos activos
    List<MateriaPrima> findByActivoTrue();

    // Buscar por nombre (ignorando mayúsculas/minúsculas)
    List<MateriaPrima> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por tipo de tela
    List<MateriaPrima> findByTipoTelaIgnoreCase(String tipoTela);

    // Buscar por color
    List<MateriaPrima> findByColorIgnoreCase(String color);

    // Buscar productos con stock bajo (stock actual <= stock mínimo)
    @Query("SELECT p FROM MateriaPrima p WHERE p.stockActual <= p.stockMinimo AND p.activo = true")
    List<MateriaPrima> findProductosBajoStock();

    // Buscar productos por rango de precio
    @Query("SELECT p FROM MateriaPrima p WHERE p.precioMetro BETWEEN :precioMin AND :precioMax AND p.activo = true")
    List<MateriaPrima> findByRangoPrecio(@Param("precioMin") Double precioMin, @Param("precioMax") Double precioMax);

    // Buscar productos con stock mayor a cierta cantidad
    @Query("SELECT p FROM MateriaPrima p WHERE p.stockActual > :stockMinimo AND p.activo = true")
    List<MateriaPrima> findByStockMayorA(@Param("stockMinimo") Integer stockMinimo);

    // Buscar productos por múltiples criterios
    @Query("SELECT p FROM MateriaPrima p WHERE " +
            "(:tipoTela IS NULL OR p.tipoTela = :tipoTela) AND " +
            "(:color IS NULL OR p.color = :color) AND " +
            "(:activo IS NULL OR p.activo = :activo)")
    List<MateriaPrima> findByMultiplesCriterios(@Param("tipoTela") String tipoTela,
                                                 @Param("color") String color,
                                                 @Param("activo") Boolean activo);

    // Contar productos por tipo de tela
    @Query("SELECT COUNT(p) FROM MateriaPrima p WHERE p.tipoTela = :tipoTela AND p.activo = true")
    Long countByTipoTela(@Param("tipoTela") String tipoTela);

    // Obtener productos ordenados por stock ascendente
    @Query("SELECT p FROM MateriaPrima p WHERE p.activo = true ORDER BY p.stockActual ASC")
    List<MateriaPrima> findAllOrderByStockAsc();

    // Obtener productos más recientes
    @Query("SELECT p FROM MateriaPrima p WHERE p.activo = true ORDER BY p.fechaRegistro DESC")
    List<MateriaPrima> findAllOrderByFechaRegistroDesc();
}