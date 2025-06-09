package com.example.msalmacen.util;

import com.example.msalmacen.entity.ProductoTerminado;
import com.example.msalmacen.repository.ProductoTerminadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProductoTerminadoSeeder implements CommandLineRunner {

    @Autowired
    private ProductoTerminadoRepository productoTerminadoRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productoTerminadoRepository.count() == 0) {
            ProductoTerminado pt1 = new ProductoTerminado();
            pt1.setCodigo("PT001");
            pt1.setNombre("Polo Clásico");
            pt1.setDescripcion("Polo de algodón para uso diario.");
            pt1.setStockActual(120);
            pt1.setStockMinimo(20);
            pt1.setActivo(true);

            ProductoTerminado pt2 = new ProductoTerminado();
            pt2.setCodigo("PT002");
            pt2.setNombre("Pantalón Deportivo");
            pt2.setDescripcion("Pantalón ligero de poliéster para entrenamientos.");
            pt2.setStockActual(80);
            pt2.setStockMinimo(15);
            pt2.setActivo(true);

            productoTerminadoRepository.save(pt1);
            productoTerminadoRepository.save(pt2);

            System.out.println("Seeder de productos terminados ejecutado correctamente.");
        }
    }
}
