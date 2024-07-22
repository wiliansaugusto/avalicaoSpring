import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './view/login/login.component';
import { FuncionarioComponent } from './view/funcionario/funcionario.component';
import { ExameComponent } from './view/exame/exame.component';
import { DashboardComponent } from './view/dashboard/dashboard.component';

const routes: Routes = [
  {path:"", component:LoginComponent},
  {path:"funcionario", component:FuncionarioComponent},
  {path:"exame", component:ExameComponent},
  {path:"dashboard", component:DashboardComponent}  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
