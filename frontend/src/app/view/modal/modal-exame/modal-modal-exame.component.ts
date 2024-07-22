import { Component, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-modal-exame',
  templateUrl: './modal-modal-exame.component.html',
  styleUrl: './modal-modal-exame.component.css'
})
export class ModalModalExameComponent {
  formEditar!: FormGroup
  constructor(private formConstructor: FormBuilder, public dialogRef: MatDialogRef<ModalModalExameComponent>,
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
    }else{
      this.formEditar.markAllAsTouched();

    }
    
  }
}
