import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private apiUrl = 'http://localhost:8080/api/'; // substitua pela URL da sua API
  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });
  constructor(private http: HttpClient) { }

  login(data: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}usuario`, data, { headers: this.headers, observe: 'response' });
  }
  usarioNovo(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}usuario-novo`, data, { headers: this.headers, observe: 'response' });
  }
  alterarUsuario(data: any): Observable<any> {
    return this.http.put(`${this.apiUrl}alterar-usuario`, data, { headers: this.headers, observe: 'response' });
  }
  deletarUsuario(id: any): Observable<any> {
    return this.http.delete(`${this.apiUrl}deletar-usuario/${id}`, { headers: this.headers, observe: 'response' });
  }
}
