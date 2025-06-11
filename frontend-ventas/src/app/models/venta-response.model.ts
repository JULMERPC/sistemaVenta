import {Venta} from "./venta.model";

export interface VentaResponse {
  content: Venta[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
