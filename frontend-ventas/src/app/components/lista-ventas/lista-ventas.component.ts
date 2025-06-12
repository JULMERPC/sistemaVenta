// src/app/components/ventas/listar-ventas/listar-ventas.component.ts
import { Component, OnInit } from '@angular/core';
import {VentaResponse} from "../../models/venta-response.model";
import {Venta} from "../../models/venta.model";
import {VentaService} from "../../services/ventas.service";
// import { VentaService } from '../../../services/venta.service';
// import { Venta } from '../../../models/venta.model';
// import { VentaResponse } from '../../../models/venta-response.model';

@Component({
  selector: 'app-listar-ventas',
  templateUrl: './listar-ventas.component.html',
  standalone: true,
  styleUrls: ['./listar-ventas.component.css']
})
export class ListarVentasComponent implements OnInit {
  ventas: Venta[] = [];
  ventasResponse?: VentaResponse;

  // Variables para paginación
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  // Variables para filtros
  filtroCliente = '';
  fechaInicio = '';
  fechaFin = '';

  // Variables de estado
  loading = false;
  error = '';

  // Variables para búsqueda
  terminoBusqueda = '';

  constructor(private ventaService: VentaService) { }

  ngOnInit(): void {
    this.cargarVentas();
  }

  // Cargar ventas con paginación
  cargarVentas(): void {
    this.loading = true;
    this.error = '';

    this.ventaService.obtenerVentasPaginadas(this.currentPage, this.pageSize)
      .subscribe({
        next: (response: VentaResponse) => {
          this.ventasResponse = response;
          this.ventas = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error al cargar ventas:', error);
          this.error = 'Error al cargar las ventas. Por favor, intente nuevamente.';
          this.loading = false;
        }
      });
  }

  // Cargar todas las ventas (sin paginación)
  cargarTodasLasVentas(): void {
    this.loading = true;
    this.error = '';

    this.ventaService.obtenerVentas()
      .subscribe({
        next: (ventas: Venta[]) => {
          this.ventas = ventas;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error al cargar ventas:', error);
          this.error = 'Error al cargar las ventas. Por favor, intente nuevamente.';
          this.loading = false;
        }
      });
  }

  // Cambiar página
  cambiarPagina(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.cargarVentas();
    }
  }

  // Ir a la primera página
  irPrimeraPagina(): void {
    this.cambiarPagina(0);
  }

  // Ir a la página anterior
  irPaginaAnterior(): void {
    this.cambiarPagina(this.currentPage - 1);
  }

  // Ir a la página siguiente
  irPaginaSiguiente(): void {
    this.cambiarPagina(this.currentPage + 1);
  }

  // Ir a la última página
  irUltimaPagina(): void {
    this.cambiarPagina(this.totalPages - 1);
  }

  // Filtrar por fechas
  filtrarPorFechas(): void {
    if (this.fechaInicio && this.fechaFin) {
      this.loading = true;
      this.ventaService.obtenerVentasPorFechas(this.fechaInicio, this.fechaFin)
        .subscribe({
          next: (ventas: Venta[]) => {
            this.ventas = ventas;
            this.loading = false;
          },
          error: (error) => {
            console.error('Error al filtrar ventas:', error);
            this.error = 'Error al filtrar las ventas por fechas.';
            this.loading = false;
          }
        });
    }
  }

  // Limpiar filtros
  limpiarFiltros(): void {
    this.fechaInicio = '';
    this.fechaFin = '';
    this.filtroCliente = '';
    this.terminoBusqueda = '';
    this.currentPage = 0;
    this.cargarVentas();
  }

  // Buscar ventas (filtro local)
  buscarVentas(): Venta[] {
    if (!this.terminoBusqueda) {
      return this.ventas;
    }

    const termino = this.terminoBusqueda.toLowerCase();
    return this.ventas.filter(venta =>
      venta.cliente?.nombre?.toLowerCase().includes(termino) ||
      venta.cliente?.apellido?.toLowerCase().includes(termino) ||
      venta.cliente?.dni?.includes(termino) ||
      venta.formaPago.toLowerCase().includes(termino) ||
      venta.id.toString().includes(termino)
    );
  }

  // Eliminar venta
  eliminarVenta(venta: Venta): void {
    if (confirm(`¿Está seguro de eliminar la venta #${venta.id}?`)) {
      this.ventaService.eliminarVenta(venta.id)
        .subscribe({
          next: () => {
            this.cargarVentas(); // Recargar la lista
          },
          error: (error) => {
            console.error('Error al eliminar venta:', error);
            this.error = 'Error al eliminar la venta.';
          }
        });
    }
  }

  // Formatear fecha
  formatearFecha(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-PE');
  }

  // Formatear moneda
  formatearMoneda(monto: number): string {
    return new Intl.NumberFormat('es-PE', {
      style: 'currency',
      currency: 'PEN'
    }).format(monto);
  }

  // Obtener nombre completo del cliente
  obtenerNombreCliente(venta: Venta): string {
    if (venta.cliente) {
      return `${venta.cliente.nombre} ${venta.cliente.apellido}`;
    }
    return 'Cliente no disponible';
  }

  // Obtener número de página para mostrar
  obtenerPaginasVisibles(): number[] {
    const paginas: number[] = [];
    const inicio = Math.max(0, this.currentPage - 2);
    const fin = Math.min(this.totalPages - 1, this.currentPage + 2);

    for (let i = inicio; i <= fin; i++) {
      paginas.push(i);
    }

    return paginas;
  }
}
