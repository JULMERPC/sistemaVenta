import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
// import { VentaService } from '../../services/venta.service';
import {VentaResponse} from "../../models/venta-response.model";
import {Venta} from "../../models/venta.model";
import {VentaService} from "../../services/ventas.service";
// import { Venta, VentaResponse } from '../../models/venta.model';

@Component({
  selector: 'app-lista-ventas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lista-ventas.component.html',
  styleUrls: ['./lista-ventas.component.css']
})
export class ListaVentasComponent implements OnInit {
  ventas: Venta[] = [];
  loading = false;
  error: string | null = null;

  // Paginación
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  // Filtros
  fechaInicio = '';
  fechaFin = '';
  filtroCliente = '';

  constructor(private ventaService: VentaService) { }

  ngOnInit(): void {
    this.cargarVentas();
  }

  cargarVentas(): void {
    this.loading = true;
    this.error = null;

    this.ventaService.getVentas(this.currentPage, this.pageSize).subscribe({
      next: (response: VentaResponse) => {
        this.ventas = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar las ventas';
        this.loading = false;
        console.error('Error:', error);
      }
    });
  }

  // Navegación de páginas
  irAPagina(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.cargarVentas();
    }
  }

  paginaAnterior(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.cargarVentas();
    }
  }

  paginaSiguiente(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.cargarVentas();
    }
  }

  // Filtros
  filtrarPorFecha(): void {
    if (this.fechaInicio && this.fechaFin) {
      this.loading = true;
      this.ventaService.getVentasByFecha(this.fechaInicio, this.fechaFin).subscribe({
        next: (ventas) => {
          this.ventas = ventas;
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Error al filtrar por fecha';
          this.loading = false;
          console.error('Error:', error);
        }
      });
    } else {
      this.cargarVentas();
    }
  }

  limpiarFiltros(): void {
    this.fechaInicio = '';
    this.fechaFin = '';
    this.filtroCliente = '';
    this.currentPage = 0;
    this.cargarVentas();
  }

  // Formatear fecha
  formatearFecha(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-ES');
  }

  // Formatear moneda
  formatearMoneda(valor: number): string {
    return new Intl.NumberFormat('es-PE', {
      style: 'currency',
      currency: 'PEN'
    }).format(valor);
  }

  // Ver detalles de venta
  verDetalles(ventaId: number): void {
    // Aquí puedes navegar a un componente de detalles o abrir un modal
    console.log('Ver detalles de venta:', ventaId);
  }

  // Eliminar venta
  eliminarVenta(ventaId: number): void {
    if (confirm('¿Está seguro de que desea eliminar esta venta?')) {
      this.ventaService.deleteVenta(ventaId).subscribe({
        next: () => {
          this.cargarVentas();
        },
        error: (error) => {
          this.error = 'Error al eliminar la venta';
          console.error('Error:', error);
        }
      });
    }
  }

  // Obtener páginas para mostrar en paginación
  getPaginas(): number[] {
    const paginas: number[] = [];
    const inicio = Math.max(0, this.currentPage - 2);
    const fin = Math.min(this.totalPages, inicio + 5);

    for (let i = inicio; i < fin; i++) {
      paginas.push(i);
    }
    return paginas;
  }

  protected readonly Math = Math;
}
