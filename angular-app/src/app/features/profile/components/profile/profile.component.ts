import {Component} from '@angular/core';
import {WorkoutListComponent} from "../workout-list/workout-list.component";


interface Workout {
  date: string;
  type: string;
  duration: number; // in minutes
}


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    WorkoutListComponent
  ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
  user = {
    username: 'gymbro',
    email: 'gymbro@example.com'
  };

  workouts: Workout[] = [
    {date: '2025-10-01', type: 'Chest', duration: 60},
    {date: '2025-10-03', type: 'Legs', duration: 45},
    {date: '2025-10-05', type: 'Back', duration: 50},
  ];

}
