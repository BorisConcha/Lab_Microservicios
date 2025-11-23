import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicoDashboardComponent } from './medico-dashboard.component';

describe('MedicoDashboardComponent', () => {
  let component: MedicoDashboardComponent;
  let fixture: ComponentFixture<MedicoDashboardComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MedicoDashboardComponent]
    });
    fixture = TestBed.createComponent(MedicoDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
