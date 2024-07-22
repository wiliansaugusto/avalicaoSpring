import { Component, Inject, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Usuario } from '../../../model/Usuario';
import { Funcionario } from '../../../model/Funcionario';


@Component({
  selector: 'app-modal-funcionario',
  templateUrl: './modal-funcionario.component.html',
  styleUrl: './modal-funcionario.component.css'
})
export class ModalFuncionarioComponent {


  formEditar!: FormGroup
  constructor(private formConstructor: FormBuilder, public dialogRef: MatDialogRef<ModalFuncionarioComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {

    this.formEditar = formConstructor.group({
      nome: [data.funcionario.nome, Validators.required],
      login: [data.funcionario.usuario.login, Validators.required],
      senha: [data.funcionario.usuario.senha, Validators.required],

    })

  }
  onSubmit() {
    if (this.formEditar.valid) {
      this.dialogRef.close(this.formEditar.value);
    }else{
      this.formEditar.markAllAsTouched();

    }

  }
}
