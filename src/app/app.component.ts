import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  show = false;
  constructor(private router: Router) { }
  sair() {
    sessionStorage.clear();
    this.router.navigate(['/']);
  }


}
