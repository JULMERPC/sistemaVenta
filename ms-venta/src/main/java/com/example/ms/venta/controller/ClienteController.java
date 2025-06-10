package com.example.ms.venta.controller;

import com.example.ms.venta.dto.ClienteDto;
import com.example.ms.venta.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService clienteService;

    // Crear cliente
    @PostMapping
    public ResponseEntity<?> crearCliente(@Valid @RequestBody ClienteDto clienteDto) {
        try {
            ClienteDto clienteCreado = clienteService.crearCliente(clienteDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<ClienteDto>> obtenerTodosLosClientes() {
        List<ClienteDto> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }

    // Obtener cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerClientePorId(@PathVariable Long id) {
        Optional<ClienteDto> cliente = clienteService.obtenerClientePorId(id);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Obtener cliente por DNI
    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> obtenerClientePorDni(@PathVariable String dni) {
        Optional<ClienteDto> cliente = clienteService.obtenerClientePorDni(dni);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Buscar clientes
    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteDto>> buscarClientes(@RequestParam String q) {
        List<ClienteDto> clientes = clienteService.buscarClientes(q);
        return ResponseEntity.ok(clientes);
    }

    // Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Long id,
                                               @Valid @RequestBody ClienteDto clienteDto) {
        try {
            ClienteDto clienteActualizado = clienteService.actualizarCliente(id, clienteDto);
            return ResponseEntity.ok(clienteActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Eliminar cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        try {
            clienteService.eliminarCliente(id);
            return ResponseEntity.ok().body("Cliente eliminado correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Verificar si existe cliente
    @GetMapping("/{id}/existe")
    public ResponseEntity<Boolean> existeCliente(@PathVariable Long id) {
        boolean existe = clienteService.existeCliente(id);
        return ResponseEntity.ok(existe);
    }

    // Verificar si existe DNI
    @GetMapping("/dni/{dni}/existe")
    public ResponseEntity<Boolean> existeDni(@PathVariable String dni) {
        boolean existe = clienteService.existeDni(dni);
        return ResponseEntity.ok(existe);
    }

    // Endpoints adicionales de utilidad

    // Obtener solo nombres y IDs (para dropdowns)
    @GetMapping("/simple")
    public ResponseEntity<List<ClienteDto>> obtenerClientesSimple() {
        List<ClienteDto> clientes = clienteService.obtenerTodosLosClientes();
        // Simplificar la respuesta manteniendo solo ID, nombre y apellido
        clientes.forEach(cliente -> {
            cliente.setTelefono(null);
            cliente.setDireccion(null);
            cliente.setEmail(null);
            cliente.setCreatedAt(null);
            cliente.setUpdatedAt(null);
        });
        return ResponseEntity.ok(clientes);
    }

    // Endpoint para obtener estadísticas básicas
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        try {
            List<ClienteDto> clientes = clienteService.obtenerTodosLosClientes();
            long totalClientes = clientes.size();
            long clientesConEmail = clientes.stream()
                    .filter(c -> c.getEmail() != null && !c.getEmail().trim().isEmpty())
                    .count();
            long clientesConTelefono = clientes.stream()
                    .filter(c -> c.getTelefono() != null && !c.getTelefono().trim().isEmpty())
                    .count();

            return ResponseEntity.ok(new Object() {
                public final long total = totalClientes;
                public final long conEmail = clientesConEmail;
                public final long conTelefono = clientesConTelefono;
            });
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener estadísticas: " + e.getMessage());
        }
    }
}