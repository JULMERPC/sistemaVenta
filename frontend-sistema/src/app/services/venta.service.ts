import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Venta} from "../modelo/venta.model";


@Injectable({
  providedIn: 'root'
})
export class VentaService {
  private apiUrl = 'http://localhost:8085/api/ventas'; // Ajusta el puerto si es necesario

  constructor(private http: HttpClient) {}

  obtenerVentas(): Observable<Venta[]> {
    return this.http.get<Venta[]>(this.apiUrl);
  }
}
