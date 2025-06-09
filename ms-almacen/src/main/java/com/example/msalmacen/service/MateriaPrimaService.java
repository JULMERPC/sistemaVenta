package com.example.msalmacen.service;

import com.example.msalmacen.dto.MateriaPrimaDTO;

import java.util.List;

public interface MateriaPrimaService {
    MateriaPrimaDTO guardar(MateriaPrimaDTO dto);
    List<MateriaPrimaDTO> listar();
    MateriaPrimaDTO obtenerPorCodigo(String codigo);
}
