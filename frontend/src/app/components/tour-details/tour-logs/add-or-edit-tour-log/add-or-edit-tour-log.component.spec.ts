import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddOrEditTourLogComponent } from './add-or-edit-tour-log.component';

describe('AddOrEditTourLogComponent', () => {
  let component: AddOrEditTourLogComponent;
  let fixture: ComponentFixture<AddOrEditTourLogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddOrEditTourLogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddOrEditTourLogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
