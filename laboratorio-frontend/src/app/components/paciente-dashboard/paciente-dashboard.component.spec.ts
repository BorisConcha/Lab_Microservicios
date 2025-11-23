import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PacienteDashboardComponent } from './paciente-dashboard.component';

describe('PacienteDashboardComponent', () => {
  let component: PacienteDashboardComponent;
  let fixture: ComponentFixture<PacienteDashboardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PacienteDashboardComponent]
    });
    fixture = TestBed.createComponent(PacienteDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
