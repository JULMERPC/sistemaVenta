// src/app/core/services/cliente.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Cliente, ClienteEstadisticas, ClienteSimple } from '../modelo/cliente.model';

@Injectable({
  providedIn: 'root'
})
export class ClienteService {
  private readonly apiUrl = '/api/clientes';

  constructor(private http: HttpClient) {}

  // Crear cliente
  crearCliente(cliente: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(this.apiUrl, cliente);
  }

  // Obtener todos los clientes
  obtenerTodosLosClientes(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(this.apiUrl);
  }

  // Obtener cliente por ID
  obtenerClientePorId(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.apiUrl}/${id}`);
  }

  // Obtener cliente por DNI
  obtenerClientePorDni(dni: string): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.apiUrl}/dni/${dni}`);
  }

  // Buscar clientes
  buscarClientes(query: string): Observable<Cliente[]> {
    const params = new HttpParams().set('q', query);
    return this.http.get<Cliente[]>(`${this.apiUrl}/buscar`, { params });
  }

  // Actualizar cliente
  actualizarCliente(id: number, cliente: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.apiUrl}/${id}`, cliente);
  }

  // Eliminar cliente
  eliminarCliente(id: number): Observable<string> {
    return this.http.delete<string>(`${this.apiUrl}/${id}`);
  }

  // Verificar si existe cliente
  existeCliente(id: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${id}/existe`);
  }

  // Verificar si existe DNI
  existeDni(dni: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/dni/${dni}/existe`);
  }

  // Obtener clientes simples (para dropdowns)
  obtenerClientesSimple(): Observable<ClienteSimple[]> {
    return this.http.get<ClienteSimple[]>(`${this.apiUrl}/simple`);
  }

  // Obtener estadísticas
  obtenerEstadisticas(): Observable<ClienteEstadisticas> {
    return this.http.get<ClienteEstadisticas>(`${this.apiUrl}/estadisticas`);
  }
}
