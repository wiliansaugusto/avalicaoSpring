import { Component, ElementRef, inject, signal, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ExameService } from '../../service/exame.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Exame } from '../../model/Exame';
import { MatDialog } from '@angular/material/dialog';
import { ModalFuncionarioComponent } from '../modal/modal-funcionario/modal-funcionario.component';

@Component({
  selector: 'app-exame',
  templateUrl: './exame.component.html',
  styleUrl: './exame.component.css'
})
export class ExameComponent {

  formListarExameCodigo!: FormGroup;
  formListarExameAtivos!: FormGroup;
  readonly panelOpenState = signal(false);
  formNovoExame!: FormGroup;
  formListarExame!: FormGroup;
  mostrarTabela = false
  elementeData: Exame[] = [{
    cd_exame: 0,
    nmExame: '',
    ds_detalhe_exame: '',
    ds_detalhe_exame1: '',
    ic_ativo: false
  }]

  displayedColumns: string[] = ["cd_exame", "nmExame", "ds_detalhe_exame", "ds_detalhe_exame1", "ic_ativo", "action"];
  dataSource = new MatTableDataSource<Exame>();

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild('inputFilter') inputFilter!: ElementRef<HTMLInputElement>;
  openPanelId: string = '';
  validarNovoExame = false;
  constructor(private router: Router, private snackBar: MatSnackBar,
    private formConstructor: FormBuilder, private exameService: ExameService, public dialog: MatDialog

  ) {
    this.dataSource.data = this.elementeData;
    this.dataSource.paginator = this.paginator;

    if (sessionStorage.getItem('login')) {
      console.log("session" + sessionStorage.getItem('login'));

      this.openSnackBar("Usuário precisa estar logado", "Fechar");
      router.navigate(["/"]);
    }
    this.formNovoExame = this.formConstructor.group({
      exame: ['', Validators.required],
      descricao: [''],
      descricao1: [''],
      ic_ativo: ['true', Validators.required]
    });

    this.formListarExame = this.formConstructor.group({
      nomeExame: ['', Validators.required],

    });
    this.formListarExameAtivos = this.formConstructor.group({
      ic_ativo: ['true'],

    });
    this.formListarExameCodigo = this.formConstructor.group({
      cod_exame: ['', Validators.required],

    });
    this.dataSource.filterPredicate = (data: Exame, filter: string) => {
      return data.nmExame.toLowerCase().includes(filter);
    };

  }

  onSubmitExame() {

    if (!this.validarNovoExame) {
      this.mostrarTabela=true
      this.dataSource.data = this.elementeData
      this.dataSource.paginator = this.paginator;
      this.deletarExameTabela(0)

      this.validarNovoExame = true;
    }
    if (this.formNovoExame.valid) {
      const novoExame = {
        nmExame: this.formNovoExame.get('exame')?.value,
        ds_detalhe_exame: this.formNovoExame.get('descricao')?.value,
        ds_detalhe_exame1: this.formNovoExame.get('descricao1')?.value,
        ic_ativo: this.formNovoExame.get('ic_ativo')?.value
      }
      this.exameService.novoExame(novoExame).subscribe((resp: HttpResponse<any>) => {
        this.formNovoExame.reset();
        this.openPanelId = "";
        const body = resp.body;
        this.openSnackBar("Exame salvo: " + body.nmExame + " com cod_exame: " + body.cd_exame + " com sucesso", "Fechar")
        this.dataSource.data.push(body)
        this.dataSource.paginator = this.paginator;

      }, (error: HttpErrorResponse) => {
        console.warn(error);
        this.openSnackBar(error.error, "Fechar")
        this.formNovoExame?.reset();
      })

    }
  }
  onSubmitListarExame() {

    this.mostrarTabela = true;
    this.validarNovoExame = false
    if (this.formListarExame.valid) {
      const listarExameNome = {
        nmExame: this.formListarExame.get('nomeExame')?.value,
        

      }
      this.exameService.listarExamesNome(listarExameNome).subscribe((resp: HttpResponse<any>) => {
        const body = resp.body;
        const status = resp.status
        console.warn(body);
        this.dataSource.data = body
        this.dataSource.paginator = this.paginator;

      }, (error: HttpErrorResponse) => {
        console.warn(error);
        this.openSnackBar(error.error, "Fechar")
        this.formNovoExame.get("nomeExame")?.reset()
        this.mostrarTabela = false;

      })
      this.limparFiltro();
    }
  }
  onSubmitListarExameAtivos() {
    this.validarNovoExame = false;
    if (this.formListarExameAtivos.valid) {
      this.mostrarTabela = true;
      const ic_ativo = this.formListarExameAtivos.get('ic_ativo')?.value
      this.exameService.geExamesAtivos(ic_ativo).subscribe((resp: HttpResponse<any>) => {
        const body = resp.body;
        const status = resp.status
        console.warn(body);
        this.dataSource.data = body
        this.dataSource.paginator = this.paginator;
      }, (error: HttpErrorResponse) => {
        console.warn(error);
        this.openSnackBar(error.error, "Fechar")
        this.formNovoExame.get("nomeExame")?.reset();
        this.mostrarTabela = false;

      })

    }
    this.limparFiltro();

  }
  onSubmitListarExameCodigo() {
    this.validarNovoExame = false;

    if (this.formListarExameCodigo.valid && this.validadorInteger() === true) {
      this.mostrarTabela = true;

      const cod_exame = this.formListarExameCodigo.get("cod_exame")?.value

      this.exameService.geExamesCodigo(cod_exame).subscribe((resp: HttpResponse<any>) => {
        const body: Exame[] = new Array(Object.assign(resp.body));
        const status = resp.status
        console.warn(body);
        this.dataSource.data = body
        this.dataSource.paginator = this.paginator;

      }, (error: HttpErrorResponse) => {
        console.warn(error);
        this.openSnackBar(error.error, "Fechar")
        this.formListarExameCodigo.get("cod_exame")?.reset()
        this.mostrarTabela = false;
      })

    } else {
      this.openSnackBar("O numero deve ser um inteiro", "Fechar");
      this.formListarExameCodigo.reset()
    }
    this.limparFiltro();

  }
  listarTodosExame() {
    this.mostrarTabela = true;
    this.openPanelId = " ";
    this.validarNovoExame = false;

    this.exameService.getTodosExames().subscribe((resp: HttpResponse<any>) => {
      const body = resp.body;
      const status = resp.status
      console.warn(body);
      this.dataSource.data = body
      this.dataSource.paginator = this.paginator;


    }, (error: HttpErrorResponse) => {
      console.warn(error);
      this.openSnackBar(error.error, "Fechar")
      this.formListarExameCodigo.get("cod_exame")?.reset()
      this.mostrarTabela = false;

    })
    this.limparFiltro();

  }
  editarExame(exameeditar: Exame): Promise<void> {
    return new Promise((resolve, reject) => {

      this.exameService.editarExame(exameeditar).subscribe((resp: any) => {
        const body = resp.body;
        const status = resp.status
        console.warn(body);
        this.dataSource.data = body
        this.dataSource.paginator = this.paginator;
        resolve();

      }, (error: HttpErrorResponse) => {
        console.warn(error);
        this.openSnackBar(error.error, "Fechar")
        this.formListarExameCodigo.get("cod_exame")?.reset()
        this.mostrarTabela = false;
        reject();
      })
      this.limparFiltro();
    })
  }
  deletarExame(id: any) {

    this.exameService.deletar(id).subscribe((resp: HttpResponse<any>) => {
      const body = resp.body;
      const status = resp.status
      console.warn(body);
      this.deletarExameTabela(id);
      this.openSnackBar("Deletado com sucesso - ".concat(body.nmExame), "Fechar")

    }, (error: HttpErrorResponse) => {
      console.warn(error);
      this.openSnackBar(error.error, "Fechar")
      this.formListarExameCodigo.get("cod_exame")?.reset()
    })
  }
  openSnackBar(message: string, action: string = 'Fechar'): void {
    this.snackBar.open(message, action, {
      duration: 9000,
      horizontalPosition: 'center',
      verticalPosition: 'bottom',
      panelClass: ['custom-snackbar']
    });
  }
  validadorInteger(): Boolean {
    const number = Number.isInteger(this.formListarExameCodigo.get("cod_exame")?.value) == true ? true : false;
    return number
  }
  deletarExameTabela(id: any) {
    const index = this.dataSource.data.findIndex((element) => element.cd_exame === id);
    console.log("entrou no deletar tabela com id: " + id);
    console.log("retorno do index: " + index);

    if (index != null) {
      console.warn("Entrou no if da tabela");

      this.dataSource.data.splice(index, 1);
      this.dataSource._updateChangeSubscription()

    }
  }
  applyFilter() {
    const filterValue = this.inputFilter.nativeElement.value.trim().toLowerCase();
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
  isPanelOpen(panelId: string) {
    return this.openPanelId === panelId;
  }
  openPanel(panelId: string) {
    this.openPanelId = panelId;
  }
  limparFiltro() {
    this.inputFilter.nativeElement.value = '';
    this.applyFilter()
  }

  openDialog(exame: Exame): void {
    const dialogRef = this.dialog.open(ModalFuncionarioComponent, {
      data: { exame },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.editarExame(result).then(() => {
          this.atualizarTabela();

        }).catch((err) => {
          alert(err)
        });
      }
    });
  }
  atualizarTabela() {
    switch (this.openPanelId) {
      case "painelExameCD":
        this.onSubmitListarExameAtivos();
        break;
      case "painelExameNome":
        this.onSubmitListarExame();
        break;
      case "painelExameCodigo":
        this.onSubmitListarExameCodigo();
        break;
      default:
        this.listarTodosExame();
        break;
    }
  }
}



