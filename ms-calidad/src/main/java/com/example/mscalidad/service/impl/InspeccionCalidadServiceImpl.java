package com.example.mscalidad.service.impl;

import com.example.mscalidad.entity.InspeccionCalidad;
import com.example.mscalidad.repository.InspeccionCalidadRepository;
import com.example.mscalidad.service.InspeccionCalidadService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InspeccionCalidadServiceImpl implements InspeccionCalidadService {

    private final InspeccionCalidadRepository repository;

    public InspeccionCalidadServiceImpl(InspeccionCalidadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<InspeccionCalidad> obtenerTodas() {
        return repository.findAll();
    }

    @Override
    public Optional<InspeccionCalidad> obtenerPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<InspeccionCalidad> obtenerPorCodigoProducto(String codigoProducto) {
        return repository.findByCodigoProducto(codigoProducto);
    }

    @Override
    public List<InspeccionCalidad> obtenerPorEstado(String estado) {
        return repository.findByEstado(estado);
    }

    @Override
    public InspeccionCalidad registrar(InspeccionCalidad inspeccion) {
        int totalPreguntas = 6;
        int aprobadas = 0;
        if(inspeccion.isPregunta1()) aprobadas++;
        if(inspeccion.isPregunta2()) aprobadas++;
        if(inspeccion.isPregunta3()) aprobadas++;
        if(inspeccion.isPregunta4()) aprobadas++;
        if(inspeccion.isPregunta5()) aprobadas++;
        if(inspeccion.isPregunta6()) aprobadas++;

        double porcentaje = (aprobadas * 100.0) / totalPreguntas;

        if(porcentaje >= 70.0) {
            inspeccion.setEstado("✅ Aprobado");
            inspeccion.setResultado("Aprobado");
        } else {
            inspeccion.setEstado("❌ Rechazado");
            inspeccion.setResultado("Rechazado");
        }

        if (inspeccion.getFechaInspeccion() == null) {
            inspeccion.setFechaInspeccion(LocalDateTime.now());
        }

        return repository.save(inspeccion);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
