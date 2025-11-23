import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  loginForm!: FormGroup;
  showPassword = false;
  loading = false;
  errorMessage = '';
  
  usuarios = [
    { 
      email: 'admin@lab.com', 
      password: 'Admin123!', 
      rol: 'administrador',
      nombre: 'Admin Sistema'
    },
    { 
      email: 'medico@lab.com', 
      password: 'Medico123!', 
      rol: 'medico',
      nombre: 'Dr. Juan Pérez'
    },
    { 
      email: 'paciente@lab.com', 
      password: 'Paciente123!', 
      rol: 'paciente',
      nombre: 'María González'
    }
  ];
  
  constructor(
    private fb: FormBuilder,
    private router: Router
  ) { }
  
  ngOnInit(): void {
    this.initForm();
  }
  
  initForm(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      remember: [false]
    });
  }
  
  get email() {
    return this.loginForm.get('email');
  }
  
  get password() {
    return this.loginForm.get('password');
  }
  
  togglePassword(): void {
    this.showPassword = !this.showPassword;
  }
  
  onSubmit(): void {
    if (this.loginForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      
      const { email, password, remember } = this.loginForm.value;
      
      setTimeout(() => {
        const usuario = this.usuarios.find(
          u => u.email === email && u.password === password
        );
        
        if (usuario) {
          localStorage.setItem('usuario', JSON.stringify({
            email: usuario.email,
            rol: usuario.rol,
            nombre: usuario.nombre
          }));
          
          if (remember) {
            localStorage.setItem('rememberSession', 'true');
          }
          
          this.redirectByRole(usuario.rol);
        } else {
          this.errorMessage = 'Credenciales incorrectas. Por favor, intenta nuevamente.';
          this.loading = false;
        }
      }, 1500);
    } else {
      Object.keys(this.loginForm.controls).forEach(key => {
        this.loginForm.get(key)?.markAsTouched();
      });
    }
  }
  
  redirectByRole(rol: string): void {
    switch(rol) {
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
        this.router.navigate(['/dashboard']);
    }
  }
}
