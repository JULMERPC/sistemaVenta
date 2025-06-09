package com.example.msalmacen.util;

import com.example.msalmacen.entity.Almacen;
import com.example.msalmacen.repository.AlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class AlmacenSeeder implements CommandLineRunner {

    @Autowired
    private AlmacenRepository almacenRepository;

    @Override
    public void run(String... args) throws Exception {
        if (almacenRepository.count() == 0) {
            Almacen almacen1 = new Almacen();
            almacen1.setNombre("Almacén Central");
            almacen1.setUbicacion("Av. Los Próceres 123, Lima");
            almacen1.setTipo("Depósito");
            almacen1.setEstado(true);
            almacen1.setFechaRegistro(LocalDateTime.now());

            Almacen almacen2 = new Almacen();
            almacen2.setNombre("Taller Sur");
            almacen2.setUbicacion("Jr. Las Industrias 456, Arequipa");
            almacen2.setTipo("Taller");
            almacen2.setEstado(true);
            almacen2.setFechaRegistro(LocalDateTime.now());

            almacenRepository.save(almacen1);
            almacenRepository.save(almacen2);

            System.out.println("Seeder de almacenes ejecutado correctamente.");
        }
    }
}
