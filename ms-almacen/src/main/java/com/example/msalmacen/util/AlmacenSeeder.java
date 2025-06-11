package com.example.msalmacen.util;

import org.springframework.stereotype.Component;
import com.example.msalmacen.entity.Almacen;
import com.example.msalmacen.entity.IngresoMateriaPrima;
import com.example.msalmacen.repository.AlmacenRepository;
import com.example.msalmacen.repository.IngresoMateriaPrimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class AlmacenSeeder implements CommandLineRunner {

    @Autowired
    private AlmacenRepository almacenRepository;

    @Override
    public void run(String... args) {


        if (almacenRepository.count() == 0) {
            List<Almacen> almacenes = Arrays.asList(
                    crearAlmacen("Almacén Central", "Av. Los Próceres 123 - Lima", "Depósito", true),
                    crearAlmacen("Sucursal Norte", "Jr. Independencia 456 - Trujillo", "Tienda", true),
                    crearAlmacen("Taller de Reparaciones", "Parque Industrial Mz C Lt 7 - Arequipa", "Taller", true),
                    crearAlmacen("Depósito Sur", "Carretera Panamericana Sur Km 15 - Ica", "Depósito", false),
                    crearAlmacen("Sucursal Este", "Av. Universitaria 789 - Huancayo", "Tienda", true)
            );

            almacenRepository.saveAll(almacenes);
            System.out.println("Seeder de Almacen ejecutado correctamente.");
        }
    }

    private Almacen crearAlmacen(String nombre, String ubicacion, String tipo, Boolean estado) {
        return Almacen.builder()
                .nombre(nombre)
                .ubicacion(ubicacion)
                .tipo(tipo)
                .estado(estado)
                .fechaRegistro(LocalDateTime.now())
                .build();
    }
}
