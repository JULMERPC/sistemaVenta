import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {MatButton, MatButtonModule, MatIconButton} from "@angular/material/button";
import {MatIcon, MatIconModule} from "@angular/material/icon";
import {ProductoService} from "../../../services/producto.service";
import {Producto} from "../../../modelo/Producto";
import {MatFormField, MatFormFieldModule} from "@angular/material/form-field";
import {MatInput, MatInputModule} from "@angular/material/input";
import {CommonModule, NgIf} from "@angular/common";
import {MaterialModule} from "../../../material/material.module";
import {MatOption, MatSelect} from "@angular/material/select";
import {Categoria} from "../../../modelo/Categoria";
import {CategoriaService} from "../../../services/categoria.service";

@Component({
  selector: 'app-formproducto',
  standalone: true,
  imports: [
    FormsModule,
    MatIconButton,
    MatIcon,
    MatFormField,
    MatInput,
    NgIf,
    MatButton,
    MaterialModule,
    CommonModule,
    FormsModule,
    NgIf,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatSelect,
    MatOption
  ],
  templateUrl: './formproducto.component.html',
  styleUrl: './formproducto.component.css'
})
export class FormproductoComponent {


}
