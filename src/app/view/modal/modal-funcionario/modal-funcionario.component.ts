import { Component, Inject, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Exame } from '../../../model/Exame';
import { timeout } from 'rxjs';

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
      cd_exame: [data.exame.cd_exame],
      nmExame: [data.exame.nmExame, Validators.required],
      ds_detalhe_exame: [data.exame.ds_detalhe_exame],
      ds_detalhe_exame1: [data.exame.ds_detalhe_exame1],
      ic_ativo: [data.exame.ic_ativo, Validators.required]
    })

  }
  onSubmitExame() {
    if (this.formEditar.valid) {
      this.dialogRef.close(this.formEditar.value);
    }
    
  }
}
