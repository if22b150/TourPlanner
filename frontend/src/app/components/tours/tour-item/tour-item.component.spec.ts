import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TourItemComponent } from './tour-item.component';

describe('TourItemComponent', () => {
  let component: TourItemComponent;
  let fixture: ComponentFixture<TourItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TourItemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TourItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
