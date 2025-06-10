package com.example.ms.venta.util;

import com.example.ms.venta.entity.Venta;
import com.example.ms.venta.repository.ClienteRepository;
import com.example.ms.venta.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class VentaSeeder implements CommandLineRunner {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void run(String... args) {
        if (ventaRepository.count() == 0 && clienteRepository.count() > 0) {
            List<Long> clienteIds = clienteRepository.findAll()
                    .stream()
                    .map(cliente -> cliente.getId())
                    .toList();

            List<String> formasPago = Arrays.asList("Efectivo", "Tarjeta", "Transferencia");

            Random random = new Random();

            for (int i = 0; i < 10; i++) {
                Long clienteId = clienteIds.get(random.nextInt(clienteIds.size()));
                BigDecimal total = BigDecimal.valueOf(50 + (500 * random.nextDouble()))
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                String formaPago = formasPago.get(random.nextInt(formasPago.size()));
                LocalDate fecha = LocalDate.now().minusDays(random.nextInt(30));
                LocalDateTime timestamp = LocalDateTime.now();

                Venta venta = Venta.builder()
                        .clienteId(clienteId)
                        .fecha(fecha)
                        .total(total)
                        .formaPago(formaPago)
                        .createdAt(timestamp)
                        .updatedAt(timestamp)
                        .build();

                ventaRepository.save(venta);
            }

            System.out.println("Seeder de Venta ejecutado correctamente.");
        }
    }
}
