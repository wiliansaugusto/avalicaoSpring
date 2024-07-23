import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Funcionario } from '../../model/Funcionario';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { FuncionarioService } from '../../service/funcionario.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import { ModalFuncionarioComponent } from '../modal/modal-funcionario/modal-funcionario.component';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-funcionario',
  templateUrl: './funcionario.component.html',
  styleUrl: './funcionario.component.css'
})
export class FuncionarioComponent {

  openPanelId: string = '';
  mostrarTabela = false
  elementeData: Funcionario[] = [{
    nome: "", codigo: 0, usuario: null
  }]

  displayedColumns: string[] = ["codigo", "nome", "login", "action"];
  dataSource = new MatTableDataSource<Funcionario>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild('inputFilter') inputFilter!: ElementRef<HTMLInputElement>;
  validarNovoFuncionario = false;

  formNovoFuncionario!: FormGroup;
  formListarFuncionarios!: FormGroup;

  constructor(private router: Router, private snackBar: MatSnackBar, private spiner: NgxSpinnerService,
    private formConstructor: FormBuilder, private funcionarioService: FuncionarioService, public dialog: MatDialog) {

    this.dataSource.data = this.elementeData;
    this.dataSource.paginator = this.paginator;
    this.formNovoFuncionario = this.formConstructor.group({
      nome: ['', Validators.required],
      login: ['', Validators.required],
      senha: ['', Validators.required]
    });

    this.formListarFuncionarios = formConstructor.group({
      nome: [''],
      codigo: ['']
    })
    if (!sessionStorage.getItem('login')) {
      this.openSnackBar("Usuário precisa estar logado", "Fechar");
      router.navigate(["/"]);

    }


  }
  onSubmitFuncionarioNovo() {

    if (this.formNovoFuncionario.valid) {
      this.spiner.show()

      if (!this.validarNovoFuncionario) {
        this.mostrarTabela = true
        this.dataSource.data = this.elementeData
        this.dataSource.paginator = this.paginator;
        this.deletarFuncionarioTabela(0)

        this.validarNovoFuncionario = true;
      }
      const usuarioNovo = {
        login: this.formNovoFuncionario.get('login')?.value,
        senha: this.formNovoFuncionario.get('senha')?.value
      }

      const funcionarioNovo = {

        nome: this.formNovoFuncionario.get('nome')?.value,
        usuario: usuarioNovo
      }
      this.funcionarioService.salvarFuncionarioNovo(funcionarioNovo).subscribe((resp: HttpResponse<any>) => {
        const body = resp.body;
        this.openSnackBar("Cadastrado com sucesso - ".concat(body.nome), "Fechar")
        this.dataSource.data.push(body)
        this.dataSource.paginator = this.paginator;
        this.formNovoFuncionario.reset();
        this.spiner.hide()

      }, (error: HttpErrorResponse) => {
        this.spiner.hide()
        this.openSnackBar(error.error, "Fechar")
        // this.formListarExameCodigo.get("cod_exame")?.reset()
      })

    } else {
      this.formNovoFuncionario.markAllAsTouched();

    }
  }
  deletarFuncionario(id: any) {
    this.spiner.show()

    this.funcionarioService.deleteFuncionario(id).subscribe((resp: HttpResponse<any>) => {
      const body = resp.body;
      this.deletarFuncionarioTabela(id);
      this.openSnackBar("Deletado com sucesso - ".concat(body.nome), "Fechar")
      this.spiner.hide()

    }, (error: HttpErrorResponse) => {
      this.spiner.hide()

      this.openSnackBar(error.error, "Fechar")
      // this.formListarExameCodigo.get("cod_exame")?.reset()
    })

  }

  listarTodosFuncionario() {
    this.spiner.show()

    this.mostrarTabela = true;
    this.openPanelId = " ";
    this.validarNovoFuncionario = false;

    this.funcionarioService.getTodosFuncionarios().subscribe((resp: HttpResponse<any>) => {
      const body = resp.body;
      this.dataSource.data = body
      this.dataSource.paginator = this.paginator;
      this.spiner.hide()

    }, (error: HttpErrorResponse) => {
      this.spiner.hide()
      this.openSnackBar(error.error, "Fechar")
      this.mostrarTabela = false;

    })
    this.limparFiltro();


  }
  openSnackBar(message: string, action: string = 'Fechar'): void {
    this.snackBar.open(message, action, {
      duration: 9000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: ['custom-snackbar']
    });
  }
  isPanelOpen(panelId: string) {
    return this.openPanelId === panelId;
  }
  openPanel(panelId: string) {
    this.openPanelId = panelId;
  }
  applyFilter() {
    const filterValue = this.inputFilter.nativeElement.value.trim().toLowerCase();
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  openDialog(funcionario: Funcionario): void {
    const dialogRef = this.dialog.open(ModalFuncionarioComponent, {
      data: { funcionario },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        funcionario.nome = result.nome;
        funcionario.usuario!.login = result.login;
        funcionario.usuario!.senha = result.senha;
        this.editarFuncionario(funcionario).then(() => {
          this.atualizarTabela();

        }).catch((err) => {
          this.openSnackBar(err, "Fechar")
        });
      }
    });
  }
  atualizarTabela() {
    switch (this.openPanelId) {
      case "painelNovFuncionario":
        this.onSubmitFuncionarioNovo();
        break;
      case "painelListarFuncionario":
         this.onSubmitListarFuncionarios();
        break;
      default:
        this.listarTodosFuncionario();
        break;
    }
  }
  limparFiltro() {
    this.inputFilter.nativeElement.value = '';
    this.applyFilter()
  }
  deletarFuncionarioTabela(id: any) {
    const index = this.dataSource.data.findIndex((element) => element.codigo === id);
    
    if (index != null) {

      this.dataSource.data.splice(index, 1);
      this.dataSource._updateChangeSubscription()

    }
  }
  onSubmitListarFuncionarios() {

    this.mostrarTabela = true
    this.validarNovoFuncionario = true;

    if (this.formListarFuncionarios.get('codigo')?.value && this.formListarFuncionarios.get('nome')?.value) {
      this.openSnackBar("Os dois campos estão preenchidos, por favor decida por um", "Fechar")

    } else if (this.formListarFuncionarios.get('codigo')!.value) {
      this.funcionarioService.getFuncionarioID(this.formListarFuncionarios.get('codigo')!.value).subscribe((resp: HttpResponse<any>) => {
        const body: Funcionario[] = [];
        body.push(resp.body);
 
        this.dataSource.data = body
        this.dataSource.paginator = this.paginator;

      }, (error: HttpErrorResponse) => {
        this.openSnackBar(error.error, "Fechar")
        this.mostrarTabela = false;

      })

    } else if (this.formListarFuncionarios.get('nome')!.value) {
      const funcionario: Funcionario = {
        nome: this.formListarFuncionarios.get('nome')!.value
      }
      this.funcionarioService.listarFuncionarioNome(funcionario).subscribe((resp: HttpResponse<any>) => {
        const body: any[] = resp.body;
        this.dataSource.data = body
        this.dataSource.paginator = this.paginator;
        if (body.length === 0) {
          this.openSnackBar("Funcionario não encontrado", "Fechar")
        }
      }, (error: HttpErrorResponse) => {
        this.openSnackBar(error.error, "Fechar")
        this.mostrarTabela = false;

      })
    } else {
      this.openSnackBar("Você deve informar pelo ao menos um dos dois campos", "Fechar")
    }


  }
  editarFuncionario(funcionarioEditar: Funcionario): Promise<void> {
    return new Promise((resolve, reject) => {

      this.funcionarioService.editarFuncionario(funcionarioEditar).subscribe((resp: any) => {
        const body = resp.body;
        this.dataSource.data = body
        this.dataSource.paginator = this.paginator;
        resolve();

      }, (error: HttpErrorResponse) => {
        this.openSnackBar(error.error, "Fechar")
        this.formListarFuncionarios.reset()
        this.mostrarTabela = false;
        reject();
      })
      this.limparFiltro();
    })
  }

}
