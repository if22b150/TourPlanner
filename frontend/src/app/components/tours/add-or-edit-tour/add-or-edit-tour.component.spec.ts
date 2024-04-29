import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddOrEditTourComponent } from './add-or-edit-tour.component';

describe('AddOrEditTourComponent', () => {
  let component: AddOrEditTourComponent;
  let fixture: ComponentFixture<AddOrEditTourComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddOrEditTourComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddOrEditTourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
