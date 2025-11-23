import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent implements OnInit{
  registroForm!: FormGroup;
  showPassword = false;
  loading = false;
  successMessage = '';
  
  constructor(
    private fb: FormBuilder,
    private router: Router
  ) { }
  
  ngOnInit(): void {
    this.initForm();
  }
  
  initForm(): void {
    this.registroForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellido: ['', [Validators.required, Validators.minLength(2)]],
      rut: ['', [Validators.required, Validators.pattern(/^\d{1,2}\.\d{3}\.\d{3}-[\dkK]$/)]],
      telefono: ['', [Validators.required, Validators.pattern(/^\+?56\d{9}$/)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(20),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]/)
      ]],
      confirmPassword: ['', [Validators.required]],
      acceptTerms: [false, [Validators.requiredTrue]]
    }, {
      validators: this.passwordMatchValidator
    });
  }
  
  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');
    
    if (!password || !confirmPassword) {
      return null;
    }
    
    return password.value === confirmPassword.value ? null : { passwordMismatch: true };
  }
  
  get nombre() { return this.registroForm.get('nombre'); }
  get apellido() { return this.registroForm.get('apellido'); }
  get rut() { return this.registroForm.get('rut'); }
  get telefono() { return this.registroForm.get('telefono'); }
  get email() { return this.registroForm.get('email'); }
  get password() { return this.registroForm.get('password'); }
  get confirmPassword() { return this.registroForm.get('confirmPassword'); }
  get acceptTerms() { return this.registroForm.get('acceptTerms'); }
  
  getPasswordStrength(): number {
    const password = this.password?.value || '';
    let strength = 0;
    
    if (password.length >= 8) strength += 25;
    if (password.length >= 12) strength += 25;
    if (/[a-z]/.test(password) && /[A-Z]/.test(password)) strength += 25;
    if (/\d/.test(password)) strength += 12.5;
    if (/[@$!%*?&]/.test(password)) strength += 12.5;
    
    return strength;
  }

  getPasswordStrengthClass(): string {
    const strength = this.getPasswordStrength();
    if (strength < 40) return 'bg-danger';
    if (strength < 70) return 'bg-warning';
    return 'bg-success';
  }
  
  onSubmit(): void {
    if (this.registroForm.valid) {
      this.loading = true;
      this.successMessage = '';
      
      setTimeout(() => {
        const userData = {
          ...this.registroForm.value,
          id: Date.now(),
          fechaRegistro: new Date(),
          rol: 'paciente'
        };
        
        delete userData.confirmPassword;
        delete userData.acceptTerms;
        
        const usuarios = JSON.parse(localStorage.getItem('usuarios') || '[]');
        usuarios.push(userData);
        localStorage.setItem('usuarios', JSON.stringify(usuarios));
        
        this.loading = false;
        this.successMessage = '¡Registro exitoso! Redirigiendo al inicio de sesión...';
        
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
        
      }, 1500);
    } else {
      Object.keys(this.registroForm.controls).forEach(key => {
        this.registroForm.get(key)?.markAsTouched();
      });
    }
  }
}
