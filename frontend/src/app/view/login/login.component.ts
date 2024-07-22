import { Component } from '@angular/core';
import { LoginService } from '../../service/login.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { MatSnackBar, MatSnackBarAction } from '@angular/material/snack-bar';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  userForm!: FormGroup;

  constructor(private loginService: LoginService, private formConstructor: FormBuilder,
    private snackBar: MatSnackBar, private route: Router) {
    this.userForm = this.formConstructor.group({
      login: ['', Validators.required],
      senha: ['', [Validators.required]]
    });

  }


  onSubmit() {
    if (this.userForm.valid) {
      const login = {
        login: this.userForm.get('login')?.value,
        senha: this.userForm.get('senha')?.value
      }
      this.loginService.login(login).subscribe((resp: HttpResponse<any>) => {
      sessionStorage.setItem("login", "true")
      this.route.navigate(["dashboard"])

      }, (erro: HttpErrorResponse) => {
        console.log(erro.error);
        sessionStorage.clear();
        this.userForm.reset()
        this.openSnackBar(erro.error, "fechar")
      })
    }
      console.warn(sessionStorage.getItem('login'));
      
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
