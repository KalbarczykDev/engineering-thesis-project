import { Component, Input } from '@angular/core';
import { Workout } from '../../models/workout';


@Component({
  selector: 'app-workout-list',
  standalone: true,
  imports: [],
  templateUrl: './workout-list.component.html',
  styleUrl: './workout-list.component.scss'
})
export class WorkoutListComponent {
   @Input() workouts: Workout[] = [];
}
