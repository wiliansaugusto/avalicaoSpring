import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalModalExameComponent } from './modal-modal-exame.component';

describe('ModalModalExameComponent', () => {
  let component: ModalModalExameComponent;
  let fixture: ComponentFixture<ModalModalExameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModalModalExameComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalModalExameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
