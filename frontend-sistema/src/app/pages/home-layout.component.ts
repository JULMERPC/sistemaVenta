import { Component } from '@angular/core';
import {VentasComponent} from "./ventas/ventas.component";
import {RouterModule} from "@angular/router";


@Component({
  selector: 'app-home-layout',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './home-layout.component.html',
  styleUrls: ['./home-layout.component.css']
})
export class HomeLayoutComponent {
  userName = 'Administrador';
}
