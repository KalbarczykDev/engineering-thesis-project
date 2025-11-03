import {Component, Input} from '@angular/core';
import {Workout} from '../../models/workout';


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
