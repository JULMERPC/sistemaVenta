import { Routes } from '@angular/router';
import {ListarVentasComponent} from "./components/lista-ventas/lista-ventas.component";
// import { ListaVentasComponent } from './components/lista-ventas/lista-ventas.component';

export const routes: Routes = [
  { path: '', redirectTo: '/ventas', pathMatch: 'full' },
  { path: 'ventas', component: ListarVentasComponent },
  { path: '**', redirectTo: '/ventas' }
];
