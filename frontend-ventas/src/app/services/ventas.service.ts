import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
// import { Venta, VentaRespons } from '../models/venta.model';
import { environment } from "../../environments/environment";
import {VentaResponse} from "../models/venta-response.model";
import {Venta} from "../models/venta.model";

@Injectable({
  providedIn: 'root'
})
export class VentaService {
  private apiUrl = `http://localhost:8085/ventas`; // Ajusta según tu configuración

  constructor(private http: HttpClient) { }

  // Obtener todas las ventas con paginación
  getVentas(page: number = 0, size: number = 10): Observable<VentaResponse> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<VentaResponse>(this.apiUrl, { params });
  }

  // Obtener venta por ID
  getVentaById(id: number): Observable<Venta> {
    return this.http.get<Venta>(`${this.apiUrl}/${id}`);
  }

  // Obtener ventas por cliente
  getVentasByCliente(clienteId: number): Observable<Venta[]> {
    return this.http.get<Venta[]>(`${this.apiUrl}/cliente/${clienteId}`);
  }

  // Obtener ventas por rango de fechas
  getVentasByFecha(fechaInicio: string, fechaFin: string): Observable<Venta[]> {
    const params = new HttpParams()
      .set('fechaInicio', fechaInicio)
      .set('fechaFin', fechaFin);

    return this.http.get<Venta[]>(`${this.apiUrl}/fecha`, { params });
  }

  // Crear nueva venta
  createVenta(venta: Partial<Venta>): Observable<Venta> {
    return this.http.post<Venta>(this.apiUrl, venta);
  }

  // Actualizar venta
  updateVenta(id: number, venta: Partial<Venta>): Observable<Venta> {
    return this.http.put<Venta>(`${this.apiUrl}/${id}`, venta);
  }

  // Eliminar venta
  deleteVenta(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

export class VentasService {
}
