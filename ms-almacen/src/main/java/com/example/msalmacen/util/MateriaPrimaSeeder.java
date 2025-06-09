package com.example.msalmacen.util;

import com.example.msalmacen.entity.MateriaPrima;
import com.example.msalmacen.repository.MateriaPrimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import java.util.Arrays;
import java.util.List;

@Component
//public class MateriaPrimaSeeder implements CommandLineRunner {
    public class MateriaPrimaSeeder  {
//
//    @Autowired
//    private MateriaPrimaRepository materiaPrimaRepository;
//
//    @Override
//    public void run(String... args) {
//        if (materiaPrimaRepository.count() == 0) {
//            List<MateriaPrima> materiasPrimas = Arrays.asList(
//                    crearMateria("Tela Algodón Blanca", "Tela suave 100% algodón", "metros", new BigDecimal("50.00")),
//                    crearMateria("Tela Denim Azul", "Tela resistente para jeans", "metros", new BigDecimal("30.00")),
//                    crearMateria("Cierre Metálico", "Cierre metálico para chaquetas", "unidades", new BigDecimal("200")),
//                    crearMateria("Hilo Poliéster", "Hilo resistente para costura", "rollos", new BigDecimal("100")),
//                    crearMateria("Botones de Madera", "Botones decorativos naturales", "unidades", new BigDecimal("150")),
//                    crearMateria("Tela Lino Beige", "Tela ligera ideal para verano", "metros", new BigDecimal("40")),
//                    crearMateria("Tela Jersey", "Tela elástica para camisetas", "metros", new BigDecimal("25"))
//            );
//
//            materiaPrimaRepository.saveAll(materiasPrimas);
//            System.out.println("Seeder de MateriaPrima ejecutado correctamente con múltiples registros.");
//        }
//    }
//
//    private MateriaPrima crearMateria(String nombre, String descripcion, String unidad, BigDecimal stockMinimo) {
//        MateriaPrima mp = new MateriaPrima();
//        mp.setNombre(nombre);
//        mp.setDescripcion(descripcion);
//        mp.setUnidadMedida(unidad);
//        mp.setStockMinimo(stockMinimo);
//        mp.setEstado(true);
//        mp.setFechaRegistro(LocalDateTime.now());
//        return mp;
//    }
}
