import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExameService {

  private apiUrl = 'http://localhost:8080/api/'; 
  private headers = new HttpHeaders({
    'Content-Type': 'application/json'
  });
  constructor(private http: HttpClient) { }

  getTodosExames(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}listar-todos-exames`, { headers: this.headers, observe: 'response' });
  }

  geExamesAtivos(ic_ativo: any):Observable<any>{
    return this.http.get<any>(`${this.apiUrl}listar-todos-exames/${ic_ativo}`, { headers: this.headers, observe: 'response' });
  }
  geExamesCodigo(codigo: any):Observable<any>{
    return this.http.get<any>(`${this.apiUrl}listar-exames-cd/${codigo}`, { headers: this.headers, observe: 'response' });
  }
  listarExamesNome(nomeExame:any):Observable<any>{
    return this.http.post(`${this.apiUrl}listar-exames-nome`,nomeExame, { headers: this.headers, observe: 'response' })
  }
  novoExame(exame:any):Observable<any>{
    return this.http.post(`${this.apiUrl}novo-exame`,exame, { headers: this.headers, observe: 'response' })
  }
  
  editarExame(novoExame:any):Observable<any>{
    return this.http.put(`${this.apiUrl}editar-exame`,novoExame, { headers: this.headers, observe: 'response' })
  }
  
  deletar(codigoExame:any):Observable<any>{
    return this.http.delete(`${this.apiUrl}deletar-exame/${codigoExame}`, { headers: this.headers, observe: 'response' })
  }
}