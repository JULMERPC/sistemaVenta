import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {VentasComponent} from "./pages/ventas/ventas.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, VentasComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend-sistema';
}
