import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Producto} from "../modelo/Producto";
import {environment} from "../../environments/environment.development";

@Injectable({
  providedIn: 'root'
})
export class ProductoService {

  private readonly apiUrl = `${environment.HOST}/producto`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Producto[]> {
    return this.http.get<Producto[]>(this.apiUrl);
  }

  obtenerPorId(id: number): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/${id}`);
  }

  crear(producto: Producto): Observable<Producto> {
    return this.http.post<Producto>(this.apiUrl, producto);
  }

  actualizar(id: number, producto: Producto): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}`, producto);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  desactivar(id: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/desactivar`, {});
  }

  actualizarCantidad(id: number, nuevaCantidad: number): Observable<Producto> {
    return this.http.put<Producto>(`${this.apiUrl}/${id}/cantidad`, nuevaCantidad);
  }

  crearConImagen(producto: Producto, imagen?: File): Observable<Producto> {
    const formData = new FormData();
    formData.append('codigo', producto.codigo);
    formData.append('nombre', producto.nombre);
    formData.append('cantidad', producto.cantidad?.toString() || '0');
    formData.append('precioVenta', producto.precioVenta?.toString() || '0');
    formData.append('costoCompra', producto.costoCompra?.toString() || '0');
    formData.append('categoriaId', producto.categoria?.id?.toString() || '0');
    if (producto.descripcion) formData.append('descripcion', producto.descripcion);
    if (imagen) formData.append('imagen', imagen);

    return this.http.post<Producto>(`${this.apiUrl}/crear-con-imagen`, formData);
  }

  actualizarConImagen(id: number, producto: Producto, nuevaImagen?: File): Observable<Producto> {
    const formData = new FormData();
    formData.append('codigo', producto.codigo);
    formData.append('nombre', producto.nombre);
    formData.append('cantidad', producto.cantidad?.toString() || '0');
    formData.append('precioVenta', producto.precioVenta?.toString() || '0');
    formData.append('costoCompra', producto.costoCompra?.toString() || '0');
    formData.append('estado', producto.estado?.toString() || 'false');
    formData.append('categoriaId', producto.categoria?.id?.toString() || '0');
    if (producto.descripcion) formData.append('descripcion', producto.descripcion);
    if (nuevaImagen) formData.append('imagen', nuevaImagen);

    return this.http.put<Producto>(`${this.apiUrl}/${id}/actualizar-con-imagen`, formData);
  }

  buscarPorCodigo(codigo: string): Observable<Producto> {
    return this.http.get<Producto>(`${this.apiUrl}/buscar/codigo/${codigo}`);
  }
}
