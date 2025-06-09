package com.example.msalmacen.util;

import com.example.msalmacen.entity.Almacen;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.entity.StockAlmacen;
import com.example.msalmacen.repository.AlmacenRepository;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import com.example.msalmacen.repository.StockAlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class StockAlmacenSeeder implements CommandLineRunner {

    @Autowired
    private StockAlmacenRepository stockAlmacenRepository;

    @Autowired
    private MateriaPrimaRepository materiaPrimaRepository;

    @Autowired
    private AlmacenRepository almacenRepository;

    @Override
    public void run(String... args) throws Exception {
        if (stockAlmacenRepository.count() == 0) {
            Optional<MateriaPrima> mp1 = materiaPrimaRepository.findById(1L);
            Optional<MateriaPrima> mp2 = materiaPrimaRepository.findById(2L);
            Optional<Almacen> almacen1 = almacenRepository.findById(1L);
            Optional<Almacen> almacen2 = almacenRepository.findById(2L);

            if (mp1.isPresent() && mp2.isPresent() && almacen1.isPresent() && almacen2.isPresent()) {
                StockAlmacen sa1 = new StockAlmacen(mp1.get(), almacen1.get(), new BigDecimal("150.00"));
                StockAlmacen sa2 = new StockAlmacen(mp2.get(), almacen1.get(), new BigDecimal("90.00"));
                StockAlmacen sa3 = new StockAlmacen(mp1.get(), almacen2.get(), new BigDecimal("60.00"));

                stockAlmacenRepository.save(sa1);
                stockAlmacenRepository.save(sa2);
                stockAlmacenRepository.save(sa3);

                System.out.println("Seeder de stock por almacén ejecutado correctamente.");
            } else {
                System.out.println("No se pudo ejecutar el seeder de StockAlmacen: faltan datos base (Almacen/MateriaPrima).");
            }
        }
    }
}
