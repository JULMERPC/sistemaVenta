// src/app/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { LoginRequest } from '../models/login-request.model';
import { LoginResponse } from '../models/login-response.model';
import { Token } from '../models/token.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = environment.HOST || 'http://localhost:8085';
  private tokenKey = environment.TOKEN_NAME || 'access_token';

  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasValidToken());
  public isAuthenticated$ = this.isAuthenticatedSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  // Login del usuario
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, credentials)
      .pipe(
        tap(response => {
          if (response && response.token) {
            this.setToken(response.token);
            this.isAuthenticatedSubject.next(true);
          }
        })
      );
  }

  // Validar token
  validateToken(token: string): Observable<Token> {
    return this.http.post<Token>(`${this.apiUrl}/auth/validate?token=${token}`, {});
  }

  // Crear usuario (registro)
  createUser(userData: LoginRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/create`, userData);
  }

  // Logout
  logout(): void {
    this.removeToken();
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  // Guardar token en localStorage
  private setToken(token: string): void {
    localStorage.setItem(this.tokenKey, token);
  }

  // Obtener token
  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  // Remover token
  private removeToken(): void {
    localStorage.removeItem(this.tokenKey);
  }

  // Verificar si hay token válido
  private hasValidToken(): boolean {
    const token = this.getToken();
    if (!token) return false;

    // Aquí podrías agregar lógica adicional para verificar la expiración del token
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const now = Math.floor(Date.now() / 1000);
      return payload.exp > now;
    } catch (error) {
      return false;
    }
  }

  // Verificar si está autenticado
  isAuthenticated(): boolean {
    return this.isAuthenticatedSubject.value;
  }

  // Obtener headers con autorización
  getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }
}
