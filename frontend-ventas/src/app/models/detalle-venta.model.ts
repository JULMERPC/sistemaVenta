import {MateriaPrima} from "./materia-prima.model";

export interface DetalleVenta {
  id: number;
  ventaId: number;
  materiaPrimaId: number;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  createdAt: string;
  updatedAt: string;
  // Campo adicional para mostrar
  materiaPrima?: MateriaPrima;
}
