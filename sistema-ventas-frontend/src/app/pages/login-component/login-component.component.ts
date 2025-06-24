import { Component } from '@angular/core';
import {AuthService} from "../../services/auth-service.service";
import { Router } from '@angular/router';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-login-component',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './login-component.component.html',
  styleUrl: './login-component.component.css'
})
export class LoginComponent {
  userName: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  login(): void {
    this.authService.login({ userName: this.userName, password: this.password }).subscribe({
      next: (res) => {
        const token = res.token;
        localStorage.setItem('token', token);

        try {
          const payload = JSON.parse(atob(token.split('.')[1]));
          localStorage.setItem('userId', payload.id);
          localStorage.setItem('userName', res.userName);
          localStorage.setItem('accesos', JSON.stringify(res.accesos));
        } catch (e) {
          console.error('Error al decodificar el token', e);
        }

        this.router.navigate(['/dashboard']);
      },
      error: () => {
        alert('Credenciales inválidas');
      }
    });
  }

}
