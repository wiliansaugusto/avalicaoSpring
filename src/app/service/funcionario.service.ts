import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Funcionario } from '../model/Funcionario';

@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {

  private apiUrl = 'http://localhost:8080/api/'; // substitua pela URL da sua API
  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });
  constructor(private http: HttpClient) { }

  getTodosFuncionarios(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}listar-todos-funcionario`, { headers: this.headers, observe: 'response' });
  }
  editarFuncionario(funcionario: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}editar-funcionario`, funcionario, { headers: this.headers, observe: 'response' });
  }
  listarFuncionarioNome(funcionario: Funcionario): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}listar-funcionario-nome`, funcionario, { headers: this.headers, observe: 'response' });
  }
  salvarFuncionarioNovo(funcionario: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}funcionario-novo`, funcionario, { headers: this.headers, observe: 'response' });
  }
  getFuncionarioID(id: any): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}listar-funcionario-codigo/${id}`, { headers: this.headers, observe: 'response' });
  }
  
  deleteFuncionario(id: any): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}deletar-funcionario/${id}`, { headers: this.headers, observe: 'response' });
  }
}