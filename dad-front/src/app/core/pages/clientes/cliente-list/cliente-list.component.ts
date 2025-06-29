// src/app/pages/clientes/cliente-list/cliente-list.component.ts
import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
// import { ClienteService } from '../../..//services/cliente.service';
import Swal from 'sweetalert2';
import {Cliente, ClienteEstadisticas} from '../../../modelo/cliente.model';
import {ClienteService} from '../../../services/cliente.service';
import {DatePipe} from '@angular/common';

declare var bootstrap: any;

@Component({
  selector: 'app-cliente-list',
  templateUrl: './cliente-list.component.html',
  imports: [
    ReactiveFormsModule,
    DatePipe,
    FormsModule
  ],
  styleUrls: ['./cliente-list.component.scss']
})
export class ClienteListComponent implements OnInit {
  clientes: Cliente[] = [];
  estadisticas: ClienteEstadisticas | null = null;
  clienteForm: FormGroup;
  terminoBusqueda: string = '';
  cargando: boolean = false;
  guardando: boolean = false;
  modoEdicion: boolean = false;
  clienteEditando: Cliente | null = null;

  private modal: any;

  constructor(
    private clienteService: ClienteService,
    private fb: FormBuilder
  ) {
    this.clienteForm = this.crearFormulario();
  }

  ngOnInit(): void {
    this.cargarClientes();
    this.cargarEstadisticas();
  }

  private crearFormulario(): FormGroup {
    return this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(100)]],
      apellido: ['', [Validators.required, Validators.maxLength(100)]],
      dni: ['', [Validators.required, Validators.maxLength(15)]],
      telefono: ['', [Validators.maxLength(20)]],
      direccion: ['', [Validators.maxLength(255)]],
      email: ['', [Validators.email, Validators.maxLength(100)]]
    });
  }

  cargarClientes(): void {
    this.cargando = true;
    this.clienteService.obtenerTodosLosClientes().subscribe({
      next: (clientes) => {
        this.clientes = clientes;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error al cargar clientes:', error);
        this.cargando = false;
        this.mostrarError('No se pudieron cargar los clientes');
      }
    });
  }

  cargarEstadisticas(): void {
    this.clienteService.obtenerEstadisticas().subscribe({
      next: (stats) => {
        this.estadisticas = stats;
      },
      error: (error) => {
        console.error('Error al cargar estadísticas:', error);
      }
    });
  }

  buscarClientes(): void {
    if (this.terminoBusqueda.trim() === '') {
      this.cargarClientes();
      return;
    }

    this.cargando = true;
    this.clienteService.buscarClientes(this.terminoBusqueda).subscribe({
      next: (clientes) => {
        this.clientes = clientes;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error en búsqueda:', error);
        this.cargando = false;
        this.mostrarError('Error al buscar clientes');
      }
    });
  }

  limpiarBusqueda(): void {
    this.terminoBusqueda = '';
    this.cargarClientes();
  }

  abrirFormulario(): void {
    this.modoEdicion = false;
    this.clienteEditando = null;
    this.clienteForm.reset();
    this.abrirModal();
  }

  editarCliente(cliente: Cliente): void {
    this.modoEdicion = true;
    this.clienteEditando = cliente;
    this.clienteForm.patchValue(cliente);
    this.abrirModal();
  }

  private abrirModal(): void {
    const modalElement = document.getElementById('clienteModal');
    if (modalElement) {
      this.modal = new bootstrap.Modal(modalElement);
      this.modal.show();
    }
  }

  private cerrarModal(): void {
    if (this.modal) {
      this.modal.hide();
    }
  }

  guardarCliente(): void {
    if (this.clienteForm.invalid) {
      this.clienteForm.markAllAsTouched();
      return;
    }

    this.guardando = true;
    const clienteData = this.clienteForm.value;

    const operacion = this.modoEdicion && this.clienteEditando
      ? this.clienteService.actualizarCliente(this.clienteEditando.id!, clienteData)
      : this.clienteService.crearCliente(clienteData);

    operacion.subscribe({
      next: (response) => {
        this.guardando = false;
        this.cerrarModal();
        this.cargarClientes();
        this.cargarEstadisticas();

        const mensaje = this.modoEdicion ? 'Cliente actualizado correctamente' : 'Cliente creado correctamente';
        this.mostrarExito(mensaje);
      },
      error: (error) => {
        this.guardando = false;
        console.error('Error al guardar cliente:', error);

        let mensaje = 'Error al guardar el cliente';
        if (error.error && typeof error.error === 'string') {
          mensaje = error.error;
        }
        this.mostrarError(mensaje);
      }
    });
  }

  confirmarEliminar(cliente: Cliente): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: `¿Deseas eliminar al cliente ${cliente.nombre} ${cliente.apellido}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.eliminarCliente(cliente.id!);
      }
    });
  }

  private eliminarCliente(id: number): void {
    this.clienteService.eliminarCliente(id).subscribe({
      next: () => {
        this.cargarClientes();
        this.cargarEstadisticas();
        this.mostrarExito('Cliente eliminado correctamente');
      },
      error: (error) => {
        console.error('Error al eliminar cliente:', error);

        let mensaje = 'Error al eliminar el cliente';
        if (error.error && typeof error.error === 'string') {
          mensaje = error.error;
        }
        this.mostrarError(mensaje);
      }
    });
  }

  verDetalle(cliente: Cliente): void {
    Swal.fire({
      title: `${cliente.nombre} ${cliente.apellido}`,
      html: `
        <div class="text-start">
          <p><strong>DNI:</strong> ${cliente.dni}</p>
          <p><strong>Teléfono:</strong> ${cliente.telefono || 'No especificado'}</p>
          <p><strong>Email:</strong> ${cliente.email || 'No especificado'}</p>
          <p><strong>Dirección:</strong> ${cliente.direccion || 'No especificada'}</p>
          <p><strong>Fecha de registro:</strong> ${new Date(cliente.createdAt!).toLocaleDateString()}</p>
          ${cliente.updatedAt !== cliente.createdAt ?
        `<p><strong>Última actualización:</strong> ${new Date(cliente.updatedAt!).toLocaleDateString()}</p>` :
        ''
      }
        </div>
      `,
      icon: 'info',
      confirmButtonText: 'Cerrar'
    });
  }

  exportarExcel(): void {
    // Implementar exportación a Excel
    this.mostrarInfo('Función de exportación en desarrollo');
  }

  private mostrarExito(mensaje: string): void {
    Swal.fire({
      icon: 'success',
      title: '¡Éxito!',
      text: mensaje,
      timer: 2000,
      showConfirmButton: false
    });
  }

  private mostrarError(mensaje: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: mensaje
    });
  }

  private mostrarInfo(mensaje: string): void {
    Swal.fire({
      icon: 'info',
      title: 'Información',
      text: mensaje
    });
  }
}
