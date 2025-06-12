// src/app/services/venta.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Venta } from '../models/venta.model';
import { VentaResponse } from '../models/venta-response.model';

@Injectable({
  providedIn: 'root'
})
export class VentaService {
  private apiUrl = `${environment.apiUrl}/api/ventas`;

  constructor(private http: HttpClient) { }

  // Obtener todas las ventas
  obtenerVentas(): Observable<Venta[]> {
    return this.http.get<Venta[]>(this.apiUrl);
  }

  // Obtener ventas con paginación
  obtenerVentasPaginadas(page: number = 0, size: number = 10): Observable<VentaResponse> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<VentaResponse>(`${this.apiUrl}/paginadas`, { params });
  }

  // Obtener venta por ID
  obtenerVentaPorId(id: number): Observable<Venta> {
    return this.http.get<Venta>(`${this.apiUrl}/${id}`);
  }

  // Crear nueva venta
  crearVenta(venta: Venta): Observable<Venta> {
    return this.http.post<Venta>(this.apiUrl, venta);
  }

  // Actualizar venta
  actualizarVenta(id: number, venta: Venta): Observable<Venta> {
    return this.http.put<Venta>(`${this.apiUrl}/${id}`, venta);
  }

  // Eliminar venta
  eliminarVenta(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Obtener ventas por cliente
  obtenerVentasPorCliente(clienteId: number): Observable<Venta[]> {
    return this.http.get<Venta[]>(`${this.apiUrl}/cliente/${clienteId}`);
  }

  // Obtener ventas por fecha
  obtenerVentasPorFechas(fechaInicio: string, fechaFin: string): Observable<Venta[]> {
    const params = new HttpParams()
      .set('fechaInicio', fechaInicio)
      .set('fechaFin', fechaFin);

    return this.http.get<Venta[]>(`${this.apiUrl}/fechas`, { params });
  }

  // Obtener ventas de hoy
  obtenerVentasHoy(): Observable<Venta[]> {
    return this.http.get<Venta[]>(`${this.apiUrl}/hoy`);
  }

  // Obtener total de ventas por cliente
  obtenerTotalVentasPorCliente(clienteId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/total/cliente/${clienteId}`);
  }
}

export class VentasService {
}
