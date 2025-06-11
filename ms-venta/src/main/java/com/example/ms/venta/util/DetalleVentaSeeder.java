package com.example.ms.venta.util;
import com.example.ms.venta.client.MateriaPrimaClient;
import com.example.ms.venta.dto.MateriaPrimaDTO;
import com.example.ms.venta.entity.DetalleVenta;
import com.example.ms.venta.entity.Venta;
import com.example.ms.venta.repository.DetalleVentaRepository;
import com.example.ms.venta.repository.VentaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
public class DetalleVentaSeeder implements CommandLineRunner {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private MateriaPrimaClient materiaPrimaClient;

    @Override
    public void run(String... args) throws Exception {
        if (detalleVentaRepository.count() == 0) {
            List<Venta> ventas = ventaRepository.findAll();
            List<MateriaPrimaDTO> materias = materiaPrimaClient.obtenerTodas();

            if (ventas.isEmpty() || materias.size() < 5) {
                System.out.println("⚠️ Se necesitan ventas y al menos 5 materias primas desde el microservicio.");
                return;
            }

            Random random = new Random();

            for (int i = 0; i < 5; i++) {
                Venta venta = ventas.get(i % ventas.size());
                MateriaPrimaDTO mp = materias.get(i);

                Integer cantidad = 1 + random.nextInt(10);
                BigDecimal precioUnitario = BigDecimal.valueOf(5 + i * 2);
                BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

                DetalleVenta detalle = new DetalleVenta();
                detalle.setVentaId(venta.getId());
                detalle.setMateriaPrimaId(mp.getId());
                detalle.setCantidad(cantidad);
                detalle.setPrecioUnitario(precioUnitario);
                detalle.setSubtotal(subtotal);
                detalle.setCreatedAt(LocalDateTime.now());
                detalle.setUpdatedAt(LocalDateTime.now());

                detalleVentaRepository.save(detalle);
            }

            System.out.println("✅ Seeder de DetalleVenta ejecutado correctamente con datos de MateriaPrima vía OpenFeign.");
        }
    }
}
