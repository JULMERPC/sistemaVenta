package com.example.ms.venta.service;

import com.example.ms.venta.dto.ClienteDto;
import com.example.ms.venta.entity.Cliente;
import com.example.ms.venta.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // Crear cliente
    public ClienteDto crearCliente(ClienteDto clienteDto) {
        // Validar que no exista DNI duplicado
        if (clienteRepository.existsByDni(clienteDto.getDni())) {
            throw new RuntimeException("Ya existe un cliente con el DNI: " + clienteDto.getDni());
        }

        // Validar email único si se proporciona
        if (clienteDto.getEmail() != null && !clienteDto.getEmail().trim().isEmpty()) {
            if (clienteRepository.existsByEmail(clienteDto.getEmail())) {
                throw new RuntimeException("Ya existe un cliente con el email: " + clienteDto.getEmail());
            }
        }

        Cliente cliente = convertirDtoAEntity(clienteDto);
        Cliente clienteGuardado = clienteRepository.save(cliente);
        return convertirEntityADto(clienteGuardado);
    }

    // Obtener todos los clientes
    @Transactional(readOnly = true)
    public List<ClienteDto> obtenerTodosLosClientes() {
        return clienteRepository.findAllByOrderByNombreAscApellidoAsc()
                .stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    // Obtener cliente por ID
    @Transactional(readOnly = true)
    public Optional<ClienteDto> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .map(this::convertirEntityADto);
    }

    // Obtener cliente por DNI
    @Transactional(readOnly = true)
    public Optional<ClienteDto> obtenerClientePorDni(String dni) {
        return clienteRepository.findByDni(dni)
                .map(this::convertirEntityADto);
    }

    // Buscar clientes por texto (nombre, apellido o DNI)
    @Transactional(readOnly = true)
    public List<ClienteDto> buscarClientes(String busqueda) {
        return clienteRepository.findByBusquedaGeneral(busqueda)
                .stream()
                .map(this::convertirEntityADto)
                .collect(Collectors.toList());
    }

    // Actualizar cliente
    public ClienteDto actualizarCliente(Long id, ClienteDto clienteDto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));

        // Validar DNI único (excluyendo el cliente actual)
        if (!cliente.getDni().equals(clienteDto.getDni()) &&
                clienteRepository.existsByDniAndIdNot(clienteDto.getDni(), id)) {
            throw new RuntimeException("Ya existe otro cliente con el DNI: " + clienteDto.getDni());
        }

        // Validar email único si se proporciona (excluyendo el cliente actual)
        if (clienteDto.getEmail() != null && !clienteDto.getEmail().trim().isEmpty()) {
            if (!clienteDto.getEmail().equals(cliente.getEmail()) &&
                    clienteRepository.existsByEmailAndIdNot(clienteDto.getEmail(), id)) {
                throw new RuntimeException("Ya existe otro cliente con el email: " + clienteDto.getEmail());
            }
        }

        // Actualizar datos
        cliente.setNombre(clienteDto.getNombre());
        cliente.setApellido(clienteDto.getApellido());
        cliente.setDni(clienteDto.getDni());
        cliente.setTelefono(clienteDto.getTelefono());
        cliente.setDireccion(clienteDto.getDireccion());
        cliente.setEmail(clienteDto.getEmail());

        Cliente clienteActualizado = clienteRepository.save(cliente);
        return convertirEntityADto(clienteActualizado);
    }

    // Eliminar cliente
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    // Verificar si existe cliente
    @Transactional(readOnly = true)
    public boolean existeCliente(Long id) {
        return clienteRepository.existsById(id);
    }

    // Verificar si existe DNI
    @Transactional(readOnly = true)
    public boolean existeDni(String dni) {
        return clienteRepository.existsByDni(dni);
    }

    // Métodos de conversión
    private ClienteDto convertirEntityADto(Cliente cliente) {
        return ClienteDto.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .dni(cliente.getDni())
                .telefono(cliente.getTelefono())
                .direccion(cliente.getDireccion())
                .email(cliente.getEmail())
                .createdAt(cliente.getCreatedAt())
                .updatedAt(cliente.getUpdatedAt())
                .build();
    }

    private Cliente convertirDtoAEntity(ClienteDto clienteDto) {
        return Cliente.builder()
                .id(clienteDto.getId())
                .nombre(clienteDto.getNombre())
                .apellido(clienteDto.getApellido())
                .dni(clienteDto.getDni())
                .telefono(clienteDto.getTelefono())
                .direccion(clienteDto.getDireccion())
                .email(clienteDto.getEmail())
                .build();
    }
}