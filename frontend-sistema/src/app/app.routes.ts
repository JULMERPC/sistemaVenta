import { Routes } from '@angular/router';
import {HomeLayoutComponent} from "./pages/home-layout.component";
import {VentasComponent} from "./pages/ventas/ventas.component";


export const routes: Routes = [
  {
    path: '',
    component: HomeLayoutComponent,
    children: [
      { path: 'ventas', component: VentasComponent },
      { path: '', redirectTo: 'ventas', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: '' }
];
