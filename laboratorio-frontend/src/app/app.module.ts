import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegistroComponent } from './components/registro/registro.component';
import { RecuperarPasswordComponent } from './components/recuperar-password/recuperar-password.component';
import { PerfilComponent } from './components/perfil/perfil.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { MedicoDashboardComponent } from './components/medico-dashboard/medico-dashboard.component';
import { PacienteDashboardComponent } from './components/paciente-dashboard/paciente-dashboard.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistroComponent,
    RecuperarPasswordComponent,
    PerfilComponent,
    AdminDashboardComponent,
    MedicoDashboardComponent,
    PacienteDashboardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
