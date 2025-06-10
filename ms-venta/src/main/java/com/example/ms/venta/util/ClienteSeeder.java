package com.example.ms.venta.util;

import com.example.ms.venta.entity.Cliente;
import com.example.ms.venta.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class ClienteSeeder implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void run(String... args) {
        if (clienteRepository.count() == 0) {
            List<Cliente> clientes = Arrays.asList(
                    crearCliente("Juan", "Pérez", "12345678", "987654321", "Av. Lima 123", "juan.perez@gmail.com"),
                    crearCliente("María", "López", "23456789", "912345678", "Calle Falsa 742", "maria.lopez@yahoo.com"),
                    crearCliente("Carlos", "Gonzales", "34567890", "900111222", "Jr. Amazonas 567", "carlos.gonzales@hotmail.com"),
                    crearCliente("Ana", "Torres", "45678901", "999888777", "Urb. El Sol Mz B Lt 4", "ana.torres@gmail.com"),
                    crearCliente("Luis", "Ramírez", "56789012", "933444555", "Pasaje Los Olivos 12", "luis.ramirez@outlook.com"),
                    crearCliente("Lucía", "Martínez", "67890123", "955666777", "Av. Brasil 800", "lucia.martinez@gmail.com")
            );

            clienteRepository.saveAll(clientes);
            System.out.println("Seeder de Cliente ejecutado correctamente.");
        }
    }

    private Cliente crearCliente(String nombre, String apellido, String dni, String telefono, String direccion, String email) {
        LocalDateTime now = LocalDateTime.now();
        return Cliente.builder()
                .nombre(nombre)
                .apellido(apellido)
                .dni(dni)
                .telefono(telefono)
                .direccion(direccion)
                .email(email)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
