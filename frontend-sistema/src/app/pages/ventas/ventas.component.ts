import {Component, OnInit} from '@angular/core';
import {VentaService} from "../../services/venta.service";
import {Venta} from "../../modelo/venta.model";
import {CurrencyPipe, DatePipe, NgForOf} from "@angular/common";

@Component({
  selector: 'app-ventas',
  standalone: true,
  imports: [
    NgForOf,
    DatePipe,
    CurrencyPipe
  ],
  templateUrl: './ventas.component.html',
  styleUrl: './ventas.component.css'
})
export class VentasComponent implements OnInit {
  ventas: Venta[] = [];

  constructor(private ventaService: VentaService) {}

  ngOnInit(): void {
    this.ventaService.obtenerVentas().subscribe({
      next: (data) => this.ventas = data,
      error: (err) => console.error('Error al obtener ventas:', err)
    });
  }
}
