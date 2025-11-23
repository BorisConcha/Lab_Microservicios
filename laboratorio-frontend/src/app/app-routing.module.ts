import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './components/login/login.component';
import { RegistroComponent } from './components/registro/registro.component';
import { RecuperarPasswordComponent } from './components/recuperar-password/recuperar-password.component';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { MedicoDashboardComponent } from './components/medico-dashboard/medico-dashboard.component';
import { PacienteDashboardComponent } from './components/paciente-dashboard/paciente-dashboard.component';
import { PerfilComponent } from './components/perfil/perfil.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: 'recuperar-password', component: RecuperarPasswordComponent },
  { path: 'admin/dashboard', component: AdminDashboardComponent },
  { path: 'medico/dashboard', component: MedicoDashboardComponent },
  { path: 'paciente/dashboard', component: PacienteDashboardComponent },
  { path: 'perfil', component: PerfilComponent },
  { path: '**', redirectTo: '/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
