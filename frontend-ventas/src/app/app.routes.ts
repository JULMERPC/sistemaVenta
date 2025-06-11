import { Routes } from '@angular/router';
import { ListaVentasComponent } from './components/lista-ventas/lista-ventas.component';

export const routes: Routes = [
  { path: '', redirectTo: '/ventas', pathMatch: 'full' },
  { path: 'ventas', component: ListaVentasComponent },
  { path: '**', redirectTo: '/ventas' }
];
