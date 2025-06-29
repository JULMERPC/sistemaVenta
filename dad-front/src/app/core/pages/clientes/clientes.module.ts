// src/app/pages/clientes/clientes.module.ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { ClientesRoutingModule } from './clientes-routing.module';
import {ClienteListComponent} from './cliente-list/cliente-list.component';
// import { ClienteListComponent } from './cliente-list/cliente-list.component';

@NgModule({
  declarations: [

  ],
  imports: [
    CommonModule,
    ClientesRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    ClienteListComponent
  ]
})
export class ClientesModule { }
