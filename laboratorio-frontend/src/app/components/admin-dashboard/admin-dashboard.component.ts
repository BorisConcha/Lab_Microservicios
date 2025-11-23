import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

declare var bootstrap: any;

interface Laboratorio {
  id: number;
  nombre: string;
  tipo: string;
  direccion: string;
  telefono: string;
  email: string;
  especialidades: string;
  horarioAtencion: string;
  capacidadDiaria: number;
  activo: boolean;
}

interface Resultado {
  id: number;
  paciente: string;
  laboratorio: string;
  tipoAnalisis: string;
  estado: string;
  fecha: Date;
}

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit{

  vistaActual = 'dashboard';
  usuario: any = {};
  laboratorioForm!: FormGroup;
  laboratorioSeleccionado: Laboratorio | null = null;
  
  laboratorios: Laboratorio[] = [
    {
      id: 1,
      nombre: 'Lab Central',
      tipo: 'Hematología',
      direccion: 'Av. Principal 123',
      telefono: '+56912345678',
      email: 'central@lab.com',
      especialidades: 'Hemograma, Coagulación',
      horarioAtencion: 'Lun-Vie 8:00-18:00',
      capacidadDiaria: 100,
      activo: true
    },
    {
      id: 2,
      nombre: 'Lab Bioquímica Plus',
      tipo: 'Bioquímica',
      direccion: 'Calle Segunda 456',
      telefono: '+56987654321',
      email: 'bio@lab.com',
      especialidades: 'Glucosa, Perfil lipídico',
      horarioAtencion: 'Lun-Sab 7:00-20:00',
      capacidadDiaria: 150,
      activo: true
    },
    {
      id: 3,
      nombre: 'Lab Micro',
      tipo: 'Microbiología',
      direccion: 'Av. Tercera 789',
      telefono: '+56923456789',
      email: 'micro@lab.com',
      especialidades: 'Cultivos, Antibiogramas',
      horarioAtencion: 'Lun-Vie 9:00-17:00',
      capacidadDiaria: 80,
      activo: false
    }
  ];
  
  resultados: Resultado[] = [
    {
      id: 1,
      paciente: 'María González',
      laboratorio: 'Lab Central',
      tipoAnalisis: 'Hemograma',
      estado: 'Completado',
      fecha: new Date('2024-11-20')
    },
    {
      id: 2,
      paciente: 'Juan Pérez',
      laboratorio: 'Lab Bioquímica Plus',
      tipoAnalisis: 'Perfil Lipídico',
      estado: 'Pendiente',
      fecha: new Date('2024-11-22')
    }
  ];
  
  actividadReciente = [
    {
      titulo: 'Nuevo resultado disponible',
      descripcion: 'Hemograma completo para paciente María González',
      usuario: 'Dr. Juan Pérez',
      fecha: new Date()
    },
    {
      titulo: 'Laboratorio agregado',
      descripcion: 'Se agregó Lab Micro al sistema',
      usuario: 'Admin Sistema',
      fecha: new Date(Date.now() - 3600000)
    }
  ];
  
  constructor(
    private fb: FormBuilder,
    private router: Router
  ) { }
  
  ngOnInit(): void {
    this.cargarUsuario();
    this.initForm();
  }
  
  cargarUsuario(): void {
    const userData = localStorage.getItem('usuario');
    if (userData) {
      this.usuario = JSON.parse(userData);
    } else {
      this.router.navigate(['/login']);
    }
  }
  
  initForm(): void {
    this.laboratorioForm = this.fb.group({
      nombre: ['', Validators.required],
      tipo: ['', Validators.required],
      direccion: ['', Validators.required],
      telefono: ['', [Validators.required, Validators.pattern(/^\+?56\d{9}$/)]],
      email: ['', [Validators.required, Validators.email]],
      especialidades: [''],
      horarioAtencion: ['', Validators.required],
      capacidadDiaria: [0, [Validators.required, Validators.min(1)]]
    });
  }
  
  cambiarVista(vista: string): void {
    this.vistaActual = vista;
  }
  
  contarUsuarios(): number {
    const usuarios = JSON.parse(localStorage.getItem('usuarios') || '[]');
    return usuarios.length;
  }
  
  contarPendientes(): number {
    return this.resultados.filter(r => r.estado === 'Pendiente').length;
  }
  
  obtenerTiposLaboratorios() {
    const tipos: any = {};
    this.laboratorios.forEach(lab => {
      tipos[lab.tipo] = (tipos[lab.tipo] || 0) + 1;
    });
    
    return Object.keys(tipos).map(nombre => ({
      nombre,
      cantidad: tipos[nombre]
    }));
  }
  
  abrirModalLaboratorio(laboratorio: Laboratorio | null): void {
    this.laboratorioSeleccionado = laboratorio;
    
    if (laboratorio) {
      this.laboratorioForm.patchValue(laboratorio);
    } else {
      this.laboratorioForm.reset();
    }
    
    const modal = new bootstrap.Modal(document.getElementById('modalLaboratorio'));
    modal.show();
  }
  
  guardarLaboratorio(): void {
    if (this.laboratorioForm.valid) {
      const formData = this.laboratorioForm.value;
      
      if (this.laboratorioSeleccionado) {
        const index = this.laboratorios.findIndex(l => l.id === this.laboratorioSeleccionado!.id);
        this.laboratorios[index] = {
          ...this.laboratorioSeleccionado,
          ...formData
        };
      } else {
        const nuevoLab: Laboratorio = {
          id: this.laboratorios.length + 1,
          ...formData,
          activo: true
        };
        this.laboratorios.push(nuevoLab);
      }
      
      const modal = bootstrap.Modal.getInstance(document.getElementById('modalLaboratorio'));
      modal.hide();
      
      alert('Laboratorio guardado exitosamente');
    }
  }
  
  eliminarLaboratorio(id: number): void {
    if (confirm('¿Está seguro de eliminar este laboratorio?')) {
      this.laboratorios = this.laboratorios.filter(l => l.id !== id);
      alert('Laboratorio eliminado');
    }
  }
  
  cerrarSesion(): void {
    localStorage.removeItem('usuario');
    this.router.navigate(['/login']);
  }
}
