package com.example.msalmacen.service;


import com.example.msalmacen.dto.AlmacenDTO;
import java.util.List;

public interface AlmacenService {

    AlmacenDTO crear(AlmacenDTO almacenDTO);

    AlmacenDTO obtenerPorId(Long id);

    List<AlmacenDTO> listarTodos();

    List<AlmacenDTO> listarActivos();

    AlmacenDTO actualizar(Long id, AlmacenDTO almacenDTO);

    void eliminar(Long id);

    void activar(Long id);

    void desactivar(Long id);

    List<AlmacenDTO> buscarPorNombre(String nombre);

    List<AlmacenDTO> buscarPorTipo(String tipo);

    List<AlmacenDTO> buscarPorUbicacion(String ubicacion);
}