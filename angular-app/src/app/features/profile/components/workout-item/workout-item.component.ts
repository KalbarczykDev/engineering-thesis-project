import {Component, Input} from '@angular/core';

interface Workout {
  date: string;
  type: string;
  duration: number;
}

@Component({
  selector: 'app-workout-item',
  standalone: true,
  imports: [],
  templateUrl: './workout-item.component.html',
  styleUrl: './workout-item.component.scss'
})
export class WorkoutItemComponent {
  @Input() workout!: Workout;
}
