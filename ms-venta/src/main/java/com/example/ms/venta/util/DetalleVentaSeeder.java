package com.example.ms.venta.util;

import com.example.ms.venta.client.MateriaPrimaClient;
import com.example.ms.venta.dto.MateriaPrimaDTO;
import com.example.ms.venta.entity.Cliente;
import com.example.ms.venta.entity.DetalleVenta;
import com.example.ms.venta.entity.Venta;
import com.example.ms.venta.repository.ClienteRepository;
import com.example.ms.venta.repository.DetalleVentaRepository;
import com.example.ms.venta.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


//@RequiredArgsConstructor
@Component
public class DetalleVentaSeeder implements CommandLineRunner {

    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaRepository ventaRepository;
    private final MateriaPrimaClient materiaPrimaClient;

    public DetalleVentaSeeder(DetalleVentaRepository detalleVentaRepository,
                              VentaRepository ventaRepository,
                              MateriaPrimaClient materiaPrimaClient) {
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaRepository = ventaRepository;
        this.materiaPrimaClient = materiaPrimaClient;
    }

    @Override
    public void run(String... args) throws Exception {
        if (detalleVentaRepository.count() == 0) {
            List<Venta> ventas = ventaRepository.findAll();
            List<MateriaPrimaDTO> materiasPrimas = materiaPrimaClient.obtenerMateriasPrimas();

            if (!ventas.isEmpty() && !materiasPrimas.isEmpty()) {
                Venta venta = ventas.get(0);
                MateriaPrimaDTO materiaPrima = materiasPrimas.get(0);
                Long materiaPrimaId = ((Number) materiaPrima.get("id")).longValue();

                DetalleVenta detalle = new DetalleVenta();
                detalle.setVentaId(venta.getId());
                detalle.setMateriaPrimaId(materiaPrimaId);
                detalle.setCantidad(10);
                detalle.setPrecioUnitario(new BigDecimal("15.00"));
                detalle.calcularSubtotal(); // también lo puedes omitir si confías en @PrePersist

                detalleVentaRepository.save(detalle);
                System.out.println("✅ Se insertó un registro en DetalleVenta correctamente.");
            } else {
                System.out.println("⚠️ No se encontraron ventas o materias primas para crear DetalleVenta.");
            }
        }
    }
}
