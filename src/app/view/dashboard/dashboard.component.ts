import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {

  constructor(private router: Router, private snackBar: MatSnackBar) {
    if (!sessionStorage.getItem('login')) {
      console.log("session"+sessionStorage.getItem('login'));
      
      this.openSnackBar("Usuário precisa estar logado", "Fechar");
      router.navigate(["/"]);
    }
  }

  openSnackBar(message: string, action: string = 'Fechar'): void {
    this.snackBar.open(message, action, {
      duration: 9000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: ['custom-snackbar']
    });
  }
}