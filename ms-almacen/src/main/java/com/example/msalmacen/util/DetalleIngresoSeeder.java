package com.example.msalmacen.util;

import com.example.msalmacen.entity.DetalleIngreso;
import com.example.msalmacen.entity.IngresoMateriaPrima;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.repository.DetalleIngresoRepository;
import com.example.msalmacen.repository.IngresoMateriaPrimaRepository;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DetalleIngresoSeeder implements CommandLineRunner {

    @Autowired
    private DetalleIngresoRepository detalleIngresoRepository;

    @Autowired
    private IngresoMateriaPrimaRepository ingresoMateriaPrimaRepository;

    @Autowired
    private MateriaPrimaRepository materiaPrimaRepository;

    @Override
    public void run(String... args) throws Exception {
        if (detalleIngresoRepository.count() == 0) {
            // Asegúrate de que estos IDs existan o adáptalos a los reales
            IngresoMateriaPrima ingreso1 = ingresoMateriaPrimaRepository.findById(1L).orElse(null);
            MateriaPrima materia1 = materiaPrimaRepository.findById(1L).orElse(null);
            MateriaPrima materia2 = materiaPrimaRepository.findById(2L).orElse(null);

            if (ingreso1 != null && materia1 != null && materia2 != null) {
                DetalleIngreso detalle1 = new DetalleIngreso();
                detalle1.setIngresoMateriaPrima(ingreso1);
                detalle1.setMateriaPrima(materia1);
                detalle1.setCantidad(new BigDecimal("50.00"));
                detalle1.setCostoUnitario(new BigDecimal("12.50"));

                DetalleIngreso detalle2 = new DetalleIngreso();
                detalle2.setIngresoMateriaPrima(ingreso1);
                detalle2.setMateriaPrima(materia2);
                detalle2.setCantidad(new BigDecimal("30.00"));
                detalle2.setCostoUnitario(new BigDecimal("9.75"));

                detalleIngresoRepository.save(detalle1);
                detalleIngresoRepository.save(detalle2);

                System.out.println("Seeder de detalles de ingreso ejecutado correctamente.");
            } else {
                System.out.println("No se encontraron los datos necesarios en IngresoMateriaPrima o MateriaPrima.");
            }
        }
    }
}
