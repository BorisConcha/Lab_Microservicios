import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.css']
})
export class PerfilComponent implements OnInit{

  perfilForm!: FormGroup;
  passwordForm!: FormGroup;
  usuario: any = {};
  
  showCurrentPassword = false;
  showNewPassword = false;
  guardando = false;
  cambiandoPassword = false;
  mensajeExito = '';
  mensajePasswordExito = '';
  errorPassword = '';
  
  constructor(
    private fb: FormBuilder,
    private router: Router
  ) { }
  
  ngOnInit(): void {
    this.cargarUsuario();
    this.initForms();
  }
  
  cargarUsuario(): void {
    const userData = localStorage.getItem('usuario');
    if (userData) {
      this.usuario = JSON.parse(userData);
    } else {
      this.router.navigate(['/login']);
    }
  }
  
  initForms(): void {
    this.perfilForm = this.fb.group({
      nombre: [this.usuario.nombre || '', Validators.required],
      apellido: [this.usuario.apellido || '', Validators.required],
      email: [this.usuario.email || '', [Validators.required, Validators.email]],
      telefono: [this.usuario.telefono || ''],
      rut: [this.usuario.rut || ''],
      direccion: [this.usuario.direccion || '']
    });
    
    this.passwordForm = this.fb.group({
      currentPassword: ['', Validators.required],
      newPassword: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(20),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]/)
      ]],
      confirmPassword: ['', Validators.required]
    }, {
      validators: this.passwordMatchValidator
    });
  }
  
  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('newPassword');
    const confirmPassword = control.get('confirmPassword');
    
    if (!password || !confirmPassword) {
      return null;
    }
    
    return password.value === confirmPassword.value ? null : { passwordMismatch: true };
  }
  
  get nombre() { return this.perfilForm.get('nombre'); }
  get apellido() { return this.perfilForm.get('apellido'); }
  get email() { return this.perfilForm.get('email'); }
  
  get currentPassword() { return this.passwordForm.get('currentPassword'); }
  get newPassword() { return this.passwordForm.get('newPassword'); }
  get confirmPassword() { return this.passwordForm.get('confirmPassword'); }
  
  guardarPerfil(): void {
    if (this.perfilForm.valid) {
      this.guardando = true;
      this.mensajeExito = '';
      
      setTimeout(() => {
        this.usuario = {
          ...this.usuario,
          ...this.perfilForm.value
        };
        
        localStorage.setItem('usuario', JSON.stringify(this.usuario));
        
        this.guardando = false;
        this.mensajeExito = 'Perfil actualizado exitosamente';
        
        setTimeout(() => {
          this.mensajeExito = '';
        }, 3000);
      }, 1000);
    }
  }
  
  cambiarPassword(): void {
    if (this.passwordForm.valid) {
      this.cambiandoPassword = true;
      this.mensajePasswordExito = '';
      this.errorPassword = '';
      
      setTimeout(() => {
        const passwordActualCorrecta = true;
        
        if (passwordActualCorrecta) {
          this.mensajePasswordExito = 'Contraseña cambiada exitosamente';
          this.passwordForm.reset();
          
          setTimeout(() => {
            this.mensajePasswordExito = '';
          }, 3000);
        } else {
          this.errorPassword = 'La contraseña actual es incorrecta';
        }
        
        this.cambiandoPassword = false;
      }, 1000);
    }
  }
  
  cancelar(): void {
    this.perfilForm.reset();
    this.initForms();
    this.mensajeExito = '';
  }
  
  cancelarPassword(): void {
    this.passwordForm.reset();
    this.mensajePasswordExito = '';
    this.errorPassword = '';
  }
  
  volver(): void {
    switch(this.usuario.rol) {
      case 'administrador':
        this.router.navigate(['/admin/dashboard']);
        break;
      case 'medico':
        this.router.navigate(['/medico/dashboard']);
        break;
      case 'paciente':
        this.router.navigate(['/paciente/dashboard']);
        break;
      default:
        this.router.navigate(['/login']);
    }
  }
}
