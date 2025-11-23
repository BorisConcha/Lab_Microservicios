import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';


@Component({
  selector: 'app-recuperar-password',
  templateUrl: './recuperar-password.component.html',
  styleUrls: ['./recuperar-password.component.css']
})
export class RecuperarPasswordComponent implements OnInit{

  emailForm!: FormGroup;
  codigoForm!: FormGroup;
  passwordForm!: FormGroup;
  
  emailEnviado = false;
  codigoVerificado = false;
  passwordCambiada = false;
  showPassword = false;
  loading = false;
  errorMessage = '';
  
  emailUsuario = '';
  codigoGenerado = '';
  
  constructor(private fb: FormBuilder) { }
  
  ngOnInit(): void {
    this.initForms();
  }
  
  initForms(): void {
    this.emailForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
    
    this.codigoForm = this.fb.group({
      codigo: ['', [Validators.required, Validators.pattern(/^\d{6}$/)]]
    });
    
    this.passwordForm = this.fb.group({
      newPassword: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(20),
        Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]/)
      ]],
      confirmNewPassword: ['', [Validators.required]]
    }, {
      validators: this.passwordMatchValidator
    });
  }
  
  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('newPassword');
    const confirmPassword = control.get('confirmNewPassword');
    
    if (!password || !confirmPassword) {
      return null;
    }
    
    return password.value === confirmPassword.value ? null : { passwordMismatch: true };
  }
  
  get emailControl() { return this.emailForm.get('email'); }
  get codigoControl() { return this.codigoForm.get('codigo'); }
  get newPassword() { return this.passwordForm.get('newPassword'); }
  get confirmNewPassword() { return this.passwordForm.get('confirmNewPassword'); }
  
  enviarCodigo(): void {
    if (this.emailForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      
      setTimeout(() => {
        this.emailUsuario = this.emailForm.value.email;
        this.codigoGenerado = Math.floor(100000 + Math.random() * 900000).toString();
        this.emailEnviado = true;
        this.loading = false;
        
        console.log('Código generado:', this.codigoGenerado);
      }, 1500);
    }
  }
  
  reenviarCodigo(): void {
    this.codigoGenerado = Math.floor(100000 + Math.random() * 900000).toString();
    console.log('Nuevo código generado:', this.codigoGenerado);
    alert('Código reenviado a ' + this.emailUsuario);
  }
  
  verificarCodigo(): void {
    if (this.codigoForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      
      setTimeout(() => {
        const codigoIngresado = this.codigoForm.value.codigo;
        
        if (codigoIngresado === this.codigoGenerado) {
          this.codigoVerificado = true;
          this.loading = false;
        } else {
          this.errorMessage = 'Código incorrecto. Por favor, verifica e intenta nuevamente.';
          this.loading = false;
        }
      }, 1000);
    }
  }
  
  cambiarPassword(): void {
    if (this.passwordForm.valid) {
      this.loading = true;
      
      setTimeout(() => {
        console.log('Nueva contraseña:', this.passwordForm.value.newPassword);
        
        this.passwordCambiada = true;
        this.loading = false;
      }, 1500);
    }
  }
}
