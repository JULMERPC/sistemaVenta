import {Component, OnInit} from '@angular/core';
import {Producto} from "../../modelo/Producto";
import {ProductoService} from "../../services/producto.service";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable
} from "@angular/material/table";
import {DecimalPipe, NgClass, NgIf} from "@angular/common";
import {MatDialog} from "@angular/material/dialog";
import {FormproductoComponent} from "./formproducto/formproducto.component";
import {MatButton} from "@angular/material/button";
import {MaterialModule} from "../../material/material.module";

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [
    MatTable,
    NgIf,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatCellDef,
    MatHeaderCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    DecimalPipe,
    NgClass,
    MatButton,
    MaterialModule
  ],
  templateUrl: './productos.component.html',
  styleUrl: './productos.component.css'
})
export class ProductosComponent implements OnInit {
  productos: Producto[] = [];
  columnas: string[] = ['id', 'codigo', 'nombre', 'imagen', 'cantidad', 'precioVenta', 'estado', 'categoria'];

  constructor(private productoService: ProductoService, public dialog: MatDialog) {}

  ngOnInit(): void {
    this.productoService.listar().subscribe({
      next: data => {
        this.productos = data.map(producto => {
          if (producto.imagen) {
            // Asegúrate de que sea el nombre correcto del campo (¿imagen o nombreArchivo?)
            producto.imagen = `http://localhost:8085/imagenes/${encodeURIComponent(producto.imagen)}`;
          } else {
            producto.imagen = '';
          }
          return producto;
        });
      },
      error: err => console.error('Error al obtener productos', err)
    });
  }

  abrirDialogoNuevoProducto(): void {
    const dialogRef = this.dialog.open(FormproductoComponent, {
      width: '900px',
      // Puedes pasar data si quieres
      // data: { ... }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('El diálogo se cerró');
      // Aquí podrías refrescar la lista si guardaron algo
    });
  }
}
