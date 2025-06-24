import {Component, OnInit} from '@angular/core';
import {NgClass, NgForOf} from "@angular/common";
import {Cliente} from "../../modelo/Cliente";
import {ClienteService} from "../../services/cliente.service";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from "@angular/material/table";

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [
    NgForOf,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderCellDef,
    MatCell,
    MatCellDef,
    NgClass,
    MatHeaderRow,
    MatRow,
    MatHeaderRowDef,
    MatRowDef
  ],
  templateUrl: './clientes.component.html',
  styleUrl: './clientes.component.css'
})
export class ClientesComponent implements OnInit {
  clientes: Cliente[] = [];
  columnas: string[] = ['id', 'dni', 'nombre', 'apellido', 'direccion', 'telefono', 'email', 'activo'];

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.clienteService.listar().subscribe({
      next: data => this.clientes = data,
      error: err => console.error('Error al obtener clientes', err)
    });
  }
}
