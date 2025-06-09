package com.example.msalmacen.entity;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_ingreso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleIngreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ingreso", nullable = false)
    private IngresoMateriaPrima ingresoMateriaPrima;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_materia_prima", nullable = false)
    private MateriaPrima materiaPrima;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Column(name = "costo_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal costoUnitario;

    // Método para calcular el costo total
    public BigDecimal getCostoTotal() {
        if (cantidad != null && costoUnitario != null) {
            return cantidad.multiply(costoUnitario);
        }
        return BigDecimal.ZERO;
    }

    // Getters y Setters explícitos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IngresoMateriaPrima getIngresoMateriaPrima() {
        return ingresoMateriaPrima;
    }

    public void setIngresoMateriaPrima(IngresoMateriaPrima ingresoMateriaPrima) {
        this.ingresoMateriaPrima = ingresoMateriaPrima;
    }

    public MateriaPrima getMateriaPrima() {
        return materiaPrima;
    }

    public void setMateriaPrima(MateriaPrima materiaPrima) {
        this.materiaPrima = materiaPrima;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }
}