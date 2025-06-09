package com.example.mscalidad.controller;

import com.example.mscalidad.DTO.InspeccionCalidadDTO;
import com.example.mscalidad.entity.InspeccionCalidad;
import com.example.mscalidad.mapper.InspeccionCalidadMapper;
import com.example.mscalidad.service.InspeccionCalidadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/calidad")
public class InspeccionCalidadController {

    private final InspeccionCalidadService service;

    public InspeccionCalidadController(InspeccionCalidadService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<InspeccionCalidadDTO>> obtenerTodas() {
        List<InspeccionCalidadDTO> listaDTO = service.obtenerTodas()
                .stream()
                .map(InspeccionCalidadMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(listaDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InspeccionCalidadDTO> obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id)
                .map(InspeccionCalidadMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InspeccionCalidadDTO> registrar(@RequestBody InspeccionCalidadDTO dto) {
        InspeccionCalidad entidad = InspeccionCalidadMapper.toEntity(dto);
        InspeccionCalidad guardado = service.registrar(entidad);
        return ResponseEntity.ok(InspeccionCalidadMapper.toDTO(guardado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
