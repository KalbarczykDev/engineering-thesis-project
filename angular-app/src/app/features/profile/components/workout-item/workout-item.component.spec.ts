import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkoutItemComponent } from './workout-item.component';

describe('WorkoutItemComponent', () => {
  let component: WorkoutItemComponent;
  let fixture: ComponentFixture<WorkoutItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkoutItemComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(WorkoutItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
