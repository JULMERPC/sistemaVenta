package com.example.msalmacen.util;

import com.example.msalmacen.entity.Almacen;
import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.entity.StockAlmacen;
import com.example.msalmacen.repository.AlmacenRepository;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import com.example.msalmacen.repository.StockAlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Order(5)
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
            List<MateriaPrima> materias = materiaPrimaRepository.findAll();
            List<Almacen> almacenes = almacenRepository.findAll();

            if (materias.size() < 5 || almacenes.isEmpty()) {
                System.out.println("⚠️ Se necesitan al menos 5 materias primas y al menos 1 almacén.");
                return;
            }

            Almacen almacen = almacenes.get(0); // puedes usar varios si quieres diversificar

            for (int i = 0; i < 5; i++) {
                MateriaPrima mp = materias.get(i);

                StockAlmacen stock = new StockAlmacen(
                        mp,
                        almacen,
                        BigDecimal.valueOf(50 + i * 10)
                );

                stock.setUltimaActualizacion(LocalDateTime.now());

                stockAlmacenRepository.save(stock);
            }

            System.out.println("✅ Seeder de StockAlmacen ejecutado correctamente.");
        }
    }
}