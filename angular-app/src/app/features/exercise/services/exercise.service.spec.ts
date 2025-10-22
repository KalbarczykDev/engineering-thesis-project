import { TestBed } from '@angular/core/testing';

import { ExerciseService } from './exercise.service';

describe('ExerciseService', () => {
  let service: ExerciseService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ExerciseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return an array of exercises', (done: DoneFn) => {
    service.getExercises().subscribe(exercises => {
      expect(exercises).toBeTruthy();
      expect(exercises.length).toBeGreaterThan(0);
      done();
    });
  });
});
