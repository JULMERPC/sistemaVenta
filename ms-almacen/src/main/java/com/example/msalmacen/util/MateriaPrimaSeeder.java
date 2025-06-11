package com.example.msalmacen.util;

import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@Component
@Order(2)
public class MateriaPrimaSeeder implements CommandLineRunner {

    @Autowired
    private MateriaPrimaRepository materiaPrimaRepository;

    @Override
    public void run(String... args) {
        if (materiaPrimaRepository.count() == 0) {
            List<MateriaPrima> productosTerminados = Arrays.asList(
                    crearProducto("Polo Deportivo", "Polo de algodón con estampado deportivo", "unidades", new BigDecimal("20")),
                    crearProducto("Jean Slim Fit", "Jean azul ajustado, talla estándar", "unidades", new BigDecimal("15")),
                    crearProducto("Casaca Impermeable", "Casaca con capucha, ideal para invierno", "unidades", new BigDecimal("10")),
                    crearProducto("Vestido de Fiesta", "Vestido elegante para eventos formales", "unidades", new BigDecimal("5")),
                    crearProducto("Camisa Casual", "Camisa manga larga, ideal para oficina", "unidades", new BigDecimal("25"))
            );

            materiaPrimaRepository.saveAll(productosTerminados);
            System.out.println("Seeder de MateriaPrima (ropa ya hecha) ejecutado correctamente.");
        }
    }

    private MateriaPrima crearProducto(String nombre, String descripcion, String unidadMedida, BigDecimal stockMinimo) {
        return MateriaPrima.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .unidadMedida(unidadMedida)
                .stockMinimo(stockMinimo)
                .estado(true)
                .fechaRegistro(LocalDateTime.now())
                .build();
    }
}
