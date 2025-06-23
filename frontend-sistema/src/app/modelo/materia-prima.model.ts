export interface MateriaPrima {
  id: number;
  nombre: string;
  descripcion?: string;
  unidadMedida: string;
  stockMinimo: number;
  estado: boolean;
  fechaRegistro: string;
}
