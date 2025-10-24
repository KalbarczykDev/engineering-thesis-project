import { Component, OnInit } from '@angular/core';
import { Exercise } from "../../models/exercise.model";
import { ExerciseService } from "../../services/exercise.service";
import { ExerciseCardComponent } from '../exercise-card/exercise-card.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-exercise-list',
  standalone: true,
  imports: [ExerciseCardComponent, CommonModule],
  templateUrl: './exercise-list.component.html',
  styleUrl: './exercise-list.component.scss'
})
export class ExerciseListComponent implements OnInit{
  exercises: Exercise[] = [];
  loading = true;

  constructor(private service: ExerciseService) {
  }

  ngOnInit(): void {
    this.loadExercises();
  }

  loadExercises(): void {
    this.service.getExercises().subscribe(
      {
        next: (data) => {
          this.exercises = data;
          this.loading = false;
        },
        error: (err) => {
          console.error('Error fetching exercises:', err);
          this.loading = false;
        }
      }
    )
  }
}
