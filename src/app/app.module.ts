import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { MatSidenavModule } from '@angular/material/sidenav';
import { LoginComponent } from './view/login/login.component';
import { ExameComponent } from './view/exame/exame.component';
import { DashboardComponent } from './view/dashboard/dashboard.component';
import { FuncionarioComponent } from './view/funcionario/funcionario.component';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms'; // Importe ReactiveFormsModule
import { MatSnackBarModule } from '@angular/material/snack-bar'; // Importar o módulo MatSnackBar
import {MatExpansionModule} from '@angular/material/expansion';
import {MatRadioModule} from '@angular/material/radio';
import {MatTableModule} from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { ModalFuncionarioComponent } from './view/modal/modal-funcionario/modal-funcionario.component';
import {MatDialogModule} from '@angular/material/dialog';
import { ModalModalExameComponent } from './view/modal/modal-exame/modal-modal-exame.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ExameComponent,
    DashboardComponent,
    FuncionarioComponent,
    ModalFuncionarioComponent,
    ModalModalExameComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    MatSidenavModule,
    MatIconModule,
    MatCardModule,
    MatInputModule,
    MatSnackBarModule,
    MatExpansionModule,
    MatRadioModule,
    MatTableModule,
    MatPaginatorModule,
    MatDialogModule

  ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
