package com.example.mscalidad.repository;

import com.example.mscalidad.entity.InspeccionCalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InspeccionCalidadRepository extends JpaRepository<InspeccionCalidad, Long> {

    List<InspeccionCalidad> findByCodigoProducto(String codigoProducto);

    List<InspeccionCalidad> findByEstado(String estado);
}
