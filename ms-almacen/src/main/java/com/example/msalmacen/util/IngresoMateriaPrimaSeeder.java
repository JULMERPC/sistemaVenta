package com.example.msalmacen.util;

import com.example.msalmacen.entity.Almacen;
import com.example.msalmacen.entity.IngresoMateriaPrima;
import com.example.msalmacen.repository.AlmacenRepository;
import com.example.msalmacen.repository.IngresoMateriaPrimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@Order(3)
public class IngresoMateriaPrimaSeeder implements CommandLineRunner {

    @Autowired
    private IngresoMateriaPrimaRepository ingresoMateriaPrimaRepository;

    @Override
    public void run(String... args) {
        if (ingresoMateriaPrimaRepository.count() == 0) {
            List<IngresoMateriaPrima> ingresos = Arrays.asList(
                    crearIngreso(1L, 4L, LocalDate.of(2024, 6, 1), "Factura", "F001-000123", new BigDecimal("1500.00")),
                    crearIngreso(2L, 5L, LocalDate.of(2024, 6, 3), "Boleta", "B001-000234", new BigDecimal("750.50")),
                    crearIngreso(3L, 5L, LocalDate.of(2024, 6, 2), "Factura", "F002-000345", new BigDecimal("2200.00")),
                    crearIngreso(1L, 6L, LocalDate.of(2024, 6, 4), "Boleta", "B003-000456", new BigDecimal("500.00")),
                    crearIngreso(2L, 7L, LocalDate.of(2024, 6, 3), "Factura", "F004-000567", new BigDecimal("1800.75"))
            );

            ingresoMateriaPrimaRepository.saveAll(ingresos);
            System.out.println("Seeder de IngresoMateriaPrima ejecutado correctamente.");
        }
    }

    private IngresoMateriaPrima crearIngreso(Long proveedorId, Long almacenId, LocalDate fecha,
                                             String tipoDocumento, String nroDocumento, BigDecimal total) {
        LocalDateTime now = LocalDateTime.now();
        return IngresoMateriaPrima.builder()
                .proveedorId(proveedorId)
                .almacenId(almacenId)
                .fecha(fecha)
                .tipoDocumento(tipoDocumento)
                .nroDocumento(nroDocumento)
                .total(total)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}
