import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MdbButtonComponent } from './mdb-button.component';

describe('IconButtonComponent', () => {
  let component: MdbButtonComponent;
  let fixture: ComponentFixture<MdbButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MdbButtonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MdbButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
