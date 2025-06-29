// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/clientes',
    pathMatch: 'full'
  },
  {
    path: 'clientes',
    loadChildren: () => import('./pages/clientes/clientes.module').then(m => m.ClientesModule)
  },
  // Aquí puedes agregar más rutas para otros módulos
  // {
  //   path: 'productos',
  //   loadChildren: () => import('./pages/productos/productos.module').then(m => m.ProductosModule)
  // },
  // {
  //   path: 'ventas',
  //   loadChildren: () => import('./pages/ventas/ventas.module').then(m => m.VentasModule)
  // },
  {
    path: '**',
    redirectTo: '/clientes'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
