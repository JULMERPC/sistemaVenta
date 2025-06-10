package com.example.ms.venta.repository;

import com.example.ms.venta.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar por DNI (campo único)
    Optional<Cliente> findByDni(String dni);

    // Verificar si existe un DNI (útil para validaciones)
    boolean existsByDni(String dni);

    // Buscar por email
    Optional<Cliente> findByEmail(String email);

    // Verificar si existe un email
    boolean existsByEmail(String email);

    // Buscar por nombre y apellido (búsqueda exacta)
    List<Cliente> findByNombreAndApellido(String nombre, String apellido);

    // Buscar por nombre o apellido conteniendo texto (búsqueda parcial)
    @Query("SELECT c FROM Cliente c WHERE " +
            "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
            "LOWER(c.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Cliente> findByNombreOrApellidoContaining(@Param("texto") String texto);

    // Buscar por teléfono
    Optional<Cliente> findByTelefono(String telefono);

    // Búsqueda general (nombre, apellido o DNI)
    @Query("SELECT c FROM Cliente c WHERE " +
            "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "LOWER(c.apellido) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
            "c.dni LIKE CONCAT('%', :busqueda, '%')")
    List<Cliente> findByBusquedaGeneral(@Param("busqueda") String busqueda);

    // Obtener clientes ordenados por nombre
    List<Cliente> findAllByOrderByNombreAscApellidoAsc();

    // Verificar si existe cliente con DNI diferente al actual (para actualizaciones)
    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.dni = :dni AND c.id <> :id")
    boolean existsByDniAndIdNot(@Param("dni") String dni, @Param("id") Long id);

    // Verificar si existe cliente con email diferente al actual (para actualizaciones)
    @Query("SELECT COUNT(c) > 0 FROM Cliente c WHERE c.email = :email AND c.id <> :id")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("id") Long id);
}