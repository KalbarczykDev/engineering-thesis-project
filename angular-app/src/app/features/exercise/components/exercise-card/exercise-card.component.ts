import {Component, input} from '@angular/core';
import { Exercise } from '../../models/exercise.model';

@Component({
  selector: 'app-exercise-card',
  standalone: true,
  imports: [],
  templateUrl: './exercise-card.component.html',
  styleUrl: './exercise-card.component.scss'
})
export class ExerciseCardComponent {
  exercise = input.required<Exercise>();
}
