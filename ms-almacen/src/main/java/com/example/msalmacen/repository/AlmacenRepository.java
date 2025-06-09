package com.example.msalmacen.repository;


import com.example.msalmacen.entity.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {

    // Buscar almacenes activos
    List<Almacen> findByEstadoTrue();

    // Buscar por nombre (ignorando mayúsculas/minúsculas)
    List<Almacen> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por tipo
    List<Almacen> findByTipoIgnoreCase(String tipo);

    // Buscar por tipo y estado
    List<Almacen> findByTipoIgnoreCaseAndEstado(String tipo, Boolean estado);

    // Verificar si existe un almacén con el mismo nombre
    boolean existsByNombreIgnoreCase(String nombre);

    // Buscar almacén por nombre exacto
    Optional<Almacen> findByNombreIgnoreCase(String nombre);

    // Query personalizada para obtener almacenes por ubicación
    @Query("SELECT a FROM Almacen a WHERE a.ubicacion LIKE %:ubicacion% AND a.estado = true")
    List<Almacen> findByUbicacionContaining(@Param("ubicacion") String ubicacion);
}