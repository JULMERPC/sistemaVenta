package com.example.msalmacen.service.impl;


import com.example.msalmacen.dto.AlmacenDTO;
import com.example.msalmacen.entity.Almacen;
import com.example.msalmacen.repository.AlmacenRepository;
import com.example.msalmacen.service.AlmacenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AlmacenServiceImpl implements AlmacenService {

    private static final Logger log = LoggerFactory.getLogger(AlmacenServiceImpl.class);
    private final AlmacenRepository almacenRepository;

    @Override
    public AlmacenDTO crear(AlmacenDTO almacenDTO) {
        log.info("Creando nuevo almacén: {}", almacenDTO.getNombre());

        // Validar que no exista un almacén con el mismo nombre
        if (almacenRepository.existsByNombreIgnoreCase(almacenDTO.getNombre())) {
            throw new RuntimeException("Ya existe un almacén con el nombre: " + almacenDTO.getNombre());
        }

        Almacen almacen = new Almacen();
        BeanUtils.copyProperties(almacenDTO, almacen);

        Almacen almacenGuardado = almacenRepository.save(almacen);
        log.info("Almacén creado con ID: {}", almacenGuardado.getId());

        return convertirADTO(almacenGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public AlmacenDTO obtenerPorId(Long id) {
        log.info("Obteniendo almacén por ID: {}", id);

        Almacen almacen = almacenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado con ID: " + id));

        return convertirADTO(almacen);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlmacenDTO> listarTodos() {
        log.info("Listando todos los almacenes");

        List<Almacen> almacenes = almacenRepository.findAll();
        return almacenes.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlmacenDTO> listarActivos() {
        log.info("Listando almacenes activos");

        List<Almacen> almacenes = almacenRepository.findByEstadoTrue();
        return almacenes.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    public AlmacenDTO actualizar(Long id, AlmacenDTO almacenDTO) {
        log.info("Actualizando almacén con ID: {}", id);

        Almacen almacenExistente = almacenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado con ID: " + id));

        // Validar nombre único si se está cambiando
        if (!almacenExistente.getNombre().equalsIgnoreCase(almacenDTO.getNombre()) &&
                almacenRepository.existsByNombreIgnoreCase(almacenDTO.getNombre())) {
            throw new RuntimeException("Ya existe un almacén con el nombre: " + almacenDTO.getNombre());
        }

        // Actualizar campos
        almacenExistente.setNombre(almacenDTO.getNombre());
        almacenExistente.setUbicacion(almacenDTO.getUbicacion());
        almacenExistente.setTipo(almacenDTO.getTipo());

        if (almacenDTO.getEstado() != null) {
            almacenExistente.setEstado(almacenDTO.getEstado());
        }

        Almacen almacenActualizado = almacenRepository.save(almacenExistente);
        log.info("Almacén actualizado: {}", almacenActualizado.getNombre());

        return convertirADTO(almacenActualizado);
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando almacén con ID: {}", id);

        if (!almacenRepository.existsById(id)) {
            throw new RuntimeException("Almacén no encontrado con ID: " + id);
        }

        almacenRepository.deleteById(id);
        log.info("Almacén eliminado con ID: {}", id);
    }

    @Override
    public void activar(Long id) {
        log.info("Activando almacén con ID: {}", id);

        Almacen almacen = almacenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado con ID: " + id));

        almacen.setEstado(true);
        almacenRepository.save(almacen);
        log.info("Almacén activado: {}", almacen.getNombre());
    }

    @Override
    public void desactivar(Long id) {
        log.info("Desactivando almacén con ID: {}", id);

        Almacen almacen = almacenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Almacén no encontrado con ID: " + id));

        almacen.setEstado(false);
        almacenRepository.save(almacen);
        log.info("Almacén desactivado: {}", almacen.getNombre());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlmacenDTO> buscarPorNombre(String nombre) {
        log.info("Buscando almacenes por nombre: {}", nombre);

        List<Almacen> almacenes = almacenRepository.findByNombreContainingIgnoreCase(nombre);
        return almacenes.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlmacenDTO> buscarPorTipo(String tipo) {
        log.info("Buscando almacenes por tipo: {}", tipo);

        List<Almacen> almacenes = almacenRepository.findByTipoIgnoreCaseAndEstado(tipo, true);
        return almacenes.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlmacenDTO> buscarPorUbicacion(String ubicacion) {
        log.info("Buscando almacenes por ubicación: {}", ubicacion);

        List<Almacen> almacenes = almacenRepository.findByUbicacionContaining(ubicacion);
        return almacenes.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    private AlmacenDTO convertirADTO(Almacen almacen) {
        return AlmacenDTO.builder()
                .id(almacen.getId())
                .nombre(almacen.getNombre())
                .ubicacion(almacen.getUbicacion())
                .tipo(almacen.getTipo())
                .estado(almacen.getEstado())
                .fechaRegistro(almacen.getFechaRegistro())
                .build();
    }
}