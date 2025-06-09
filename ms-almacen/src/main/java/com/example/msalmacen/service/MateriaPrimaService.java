//package com.example.msalmacen.service;
//
//import com.example.msalmacen.dto.MateriaPrimaDTO;
//
//import java.util.List;
//
//public interface MateriaPrimaService {
//    MateriaPrimaDTO guardar(MateriaPrimaDTO dto);
//    List<MateriaPrimaDTO> listar();
//    MateriaPrimaDTO obtenerPorCodigo(String codigo);
//}

package com.example.msalmacen.service;

import com.example.msalmacen.dto.MateriaPrimaDTO;

import java.util.List;
import java.util.Optional;

public interface MateriaPrimaService {

    // Obtener todas las materias primas
    List<MateriaPrimaDTO> obtenerTodas();

    // Obtener materias primas activas
    List<MateriaPrimaDTO> obtenerActivas();

    // Obtener materia prima por ID
    Optional<MateriaPrimaDTO> obtenerPorId(Long id);

    // Crear nueva materia prima
    MateriaPrimaDTO crear(MateriaPrimaDTO materiaPrimaDTO);

    // Actualizar materia prima
    MateriaPrimaDTO actualizar(Long id, MateriaPrimaDTO materiaPrimaDTO);

    // Eliminar materia prima (eliminación lógica)
    void eliminar(Long id);

    // Buscar por nombre
    List<MateriaPrimaDTO> buscarPorNombre(String nombre);

    // Buscar por unidad de medida
    List<MateriaPrimaDTO> buscarPorUnidadMedida(String unidadMedida);

    // Activar materia prima
    MateriaPrimaDTO activar(Long id);
}