import {Cliente} from "./cliente.model";
import {DetalleVenta} from "./detalle-venta.model";

export interface Venta {
  id: number;
  clienteId: number;
  fecha: string;
  total: number;
  formaPago: string;
  createdAt: string;
  updatedAt: string;
  // Campos adicionales para mostrar
  cliente?: Cliente;
  detalles?: DetalleVenta[];
}
