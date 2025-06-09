package com.example.mscalidad.mapper;

import com.example.mscalidad.entity.InspeccionCalidad;
import com.example.mscalidad.DTO.InspeccionCalidadDTO;

public class InspeccionCalidadMapper {

    public static InspeccionCalidadDTO toDTO(InspeccionCalidad entity) {
        InspeccionCalidadDTO dto = new InspeccionCalidadDTO();
        dto.setId(entity.getId());
        dto.setCodigoProducto(entity.getCodigoProducto());
        dto.setInspector(entity.getInspector());
        dto.setResultado(entity.getResultado());
        dto.setObservaciones(entity.getObservaciones());
        dto.setFechaInspeccion(entity.getFechaInspeccion());

        dto.setPregunta1(entity.isPregunta1());
        dto.setPregunta2(entity.isPregunta2());
        dto.setPregunta3(entity.isPregunta3());
        dto.setPregunta4(entity.isPregunta4());
        dto.setPregunta5(entity.isPregunta5());
        dto.setPregunta6(entity.isPregunta6());

        dto.setEstado(entity.getEstado());

        return dto;
    }

    public static InspeccionCalidad toEntity(InspeccionCalidadDTO dto) {
        InspeccionCalidad entity = new InspeccionCalidad();
        entity.setId(dto.getId());
        entity.setCodigoProducto(dto.getCodigoProducto());
        entity.setInspector(dto.getInspector());
        entity.setResultado(dto.getResultado());
        entity.setObservaciones(dto.getObservaciones());
        entity.setFechaInspeccion(dto.getFechaInspeccion());

        entity.setPregunta1(dto.isPregunta1());
        entity.setPregunta2(dto.isPregunta2());
        entity.setPregunta3(dto.isPregunta3());
        entity.setPregunta4(dto.isPregunta4());
        entity.setPregunta5(dto.isPregunta5());
        entity.setPregunta6(dto.isPregunta6());

        entity.setEstado(dto.getEstado());

        return entity;
    }
}
