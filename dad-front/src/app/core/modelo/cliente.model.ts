// src/app/core/models/cliente.model.ts
export interface Cliente {
  id?: number;
  nombre: string;
  apellido: string;
  dni: string;
  telefono?: string;
  direccion?: string;
  email?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface ClienteEstadisticas {
  total: number;
  conEmail: number;
  conTelefono: number;
}

export interface ClienteSimple {
  id: number;
  nombre: string;
  apellido: string;
}
