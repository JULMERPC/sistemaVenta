package com.example.msalmacen.util;

import com.example.msalmacen.entity.Almacen;
import com.example.msalmacen.entity.IngresoMateriaPrima;
import com.example.msalmacen.repository.AlmacenRepository;
import com.example.msalmacen.repository.IngresoMateriaPrimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class IngresoMateriaPrimaSeeder implements CommandLineRunner {

    @Autowired
    private IngresoMateriaPrimaRepository ingresoRepository;

    @Autowired
    private AlmacenRepository almacenRepository;

    @Override
    public void run(String... args) throws Exception {
        if (ingresoRepository.count() == 0) {
            Almacen almacen = almacenRepository.findById(1L).orElse(null);

            if (almacen != null) {
                IngresoMateriaPrima ingreso1 = new IngresoMateriaPrima();
                ingreso1.setIdProveedor(1L); // Asegúrate que este ID de proveedor existe
                ingreso1.setAlmacen(almacen);
                ingreso1.setFechaIngreso(LocalDateTime.now().minusDays(2));
                ingreso1.setDocumentoRef("Factura 001-123456");
                ingreso1.setObservaciones("Entrega puntual y completa.");

                IngresoMateriaPrima ingreso2 = new IngresoMateriaPrima();
                ingreso2.setIdProveedor(2L); // Otro proveedor existente
                ingreso2.setAlmacen(almacen);
                ingreso2.setFechaIngreso(LocalDateTime.now().minusDays(1));
                ingreso2.setDocumentoRef("Boleta 002-987654");
                ingreso2.setObservaciones("Entrega con faltantes reportados.");

                ingresoRepository.save(ingreso1);
                ingresoRepository.save(ingreso2);

                System.out.println("Seeder de ingresos de materia prima ejecutado correctamente.");
            } else {
                System.out.println("No se encontró el almacén con ID 1.");
            }
        }
    }
}
