package com.example.mscalidad.service;

import com.example.mscalidad.entity.InspeccionCalidad;
import java.util.List;
import java.util.Optional;

public interface InspeccionCalidadService {

    List<InspeccionCalidad> obtenerTodas();

    Optional<InspeccionCalidad> obtenerPorId(Long id);

    List<InspeccionCalidad> obtenerPorCodigoProducto(String codigoProducto);

    List<InspeccionCalidad> obtenerPorEstado(String estado);

    InspeccionCalidad registrar(InspeccionCalidad inspeccion);

    void eliminar(Long id);
}
