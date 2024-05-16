import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourLogRowComponent } from './tour-log-row.component';

describe('TourLogRowComponent', () => {
  let component: TourLogRowComponent;
  let fixture: ComponentFixture<TourLogRowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourLogRowComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TourLogRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
