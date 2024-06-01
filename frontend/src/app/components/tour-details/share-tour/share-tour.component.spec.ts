import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShareTourComponent } from './share-tour.component';

describe('ShareTourComponent', () => {
  let component: ShareTourComponent;
  let fixture: ComponentFixture<ShareTourComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShareTourComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ShareTourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
