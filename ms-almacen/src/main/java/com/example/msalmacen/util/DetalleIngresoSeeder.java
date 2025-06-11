//package com.example.msalmacen.util;
//
//import com.example.msalmacen.entity.DetalleIngreso;
//import com.example.msalmacen.entity.IngresoMateriaPrima;
//import com.example.msalmacen.entity.MateriaPrima;
//import com.example.msalmacen.repository.DetalleIngresoRepository;
//import com.example.msalmacen.repository.IngresoMateriaPrimaRepository;
//import com.example.msalmacen.repository.MateriaPrimaRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//
//@Component
//public class DetalleIngresoSeeder implements CommandLineRunner {
//
//    @Autowired
//    private DetalleIngresoRepository detalleIngresoRepository;
//
//    @Autowired
//    private IngresoMateriaPrimaRepository ingresoMateriaPrimaRepository;
//
//    @Autowired
//    private MateriaPrimaRepository materiaPrimaRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (detalleIngresoRepository.count() == 0) {
//            // Asegúrate de que estos IDs existan o adáptalos a los reales
//            IngresoMateriaPrima ingreso1 = ingresoMateriaPrimaRepository.findById(1L).orElse(null);
//            MateriaPrima materia1 = materiaPrimaRepository.findById(1L).orElse(null);
//            MateriaPrima materia2 = materiaPrimaRepository.findById(2L).orElse(null);
//
//            if (ingreso1 != null && materia1 != null && materia2 != null) {
//                DetalleIngreso detalle1 = new DetalleIngreso();
//                detalle1.setIngresoMateriaPrima(ingreso1);
//                detalle1.setMateriaPrima(materia1);
//                detalle1.setCantidad(new BigDecimal("50.00"));
//                detalle1.setCostoUnitario(new BigDecimal("12.50"));
//
//                DetalleIngreso detalle2 = new DetalleIngreso();
//                detalle2.setIngresoMateriaPrima(ingreso1);
//                detalle2.setMateriaPrima(materia2);
//                detalle2.setCantidad(new BigDecimal("30.00"));
//                detalle2.setCostoUnitario(new BigDecimal("9.75"));
//
//                detalleIngresoRepository.save(detalle1);
//                detalleIngresoRepository.save(detalle2);
//
//                System.out.println("Seeder de detalles de ingreso ejecutado correctamente.");
//            } else {
//                System.out.println("No se encontraron los datos necesarios en IngresoMateriaPrima o MateriaPrima.");
//            }
//        }
//    }
//}


package com.example.msalmacen.util;

import com.example.msalmacen.entity.DetalleIngreso;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.repository.DetalleIngresoRepository;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import org.springframework.core.annotation.Order;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Order(4)
//public class DetalleIngresoSeeder implements CommandLineRunner {
//
//    private final DetalleIngresoRepository detalleIngresoRepository;
//    private final IngresoMateriaPrimaRepository ingresoMateriaPrimaRepository;
//    private final MateriaPrimaRepository materiaPrimaRepository;
//
//    public DetalleIngresoSeeder(
//            DetalleIngresoRepository detalleIngresoRepository,
//            IngresoMateriaPrimaRepository ingresoMateriaPrimaRepository,
//            MateriaPrimaRepository materiaPrimaRepository
//    ) {
//        this.detalleIngresoRepository = detalleIngresoRepository;
//        this.ingresoMateriaPrimaRepository = ingresoMateriaPrimaRepository;
//        this.materiaPrimaRepository = materiaPrimaRepository;
//    }
//
//    @Override
//    public void run(String... args) {
//        if (detalleIngresoRepository.count() == 0) {
//
//            Optional<IngresoMateriaPrima> ingresoOpt = ingresoMateriaPrimaRepository.findById(1L);
//            Optional<MateriaPrima> materiaOpt = materiaPrimaRepository.findById(1L);
//
//            if (ingresoOpt.isPresent() && materiaOpt.isPresent()) {
//                DetalleIngreso detalle = new DetalleIngreso();
//                detalle.setIngresoMateriaPrima(ingresoOpt.get());
//                detalle.setMateriaPrima(materiaOpt.get());
//                detalle.setCantidad(new BigDecimal("15.50"));
//                detalle.setCostoUnitario(new BigDecimal("8.75"));
//                detalle.setCreatedAt(LocalDateTime.now());
//                detalle.setUpdatedAt(LocalDateTime.now());
//
//                detalleIngresoRepository.save(detalle);
//
//                System.out.println("✅ DetalleIngreso de prueba insertado correctamente.");
//            } else {
//                System.out.println("⚠️ No se encontraron IngresoMateriaPrima o MateriaPrima con ID 1. Seeder cancelado.");
//            }
//        }
//    }
//}



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
            List<IngresoMateriaPrima> ingresos = ingresoMateriaPrimaRepository.findAll();
            List<MateriaPrima> materias = materiaPrimaRepository.findAll();

            if (ingresos.size() < 1 || materias.size() < 5) {
                System.out.println("Por favor, asegúrate de tener al menos 1 ingreso y 5 materias primas registradas.");
                return;
            }

            for (int i = 0; i < 5; i++) {
                DetalleIngreso detalle = new DetalleIngreso();
                detalle.setIngresoMateriaPrima(ingresos.get(0));
                detalle.setMateriaPrima(materias.get(i));
                detalle.setCantidad(BigDecimal.valueOf(10 + i));
                detalle.setCostoUnitario(BigDecimal.valueOf(5.25 + i));
                detalle.setCreatedAt(LocalDateTime.now());
                detalle.setUpdatedAt(LocalDateTime.now());
                detalleIngresoRepository.save(detalle);
            }

            System.out.println("✅ Seeder de DetalleIngreso ejecutado correctamente.");
        }
    }
}