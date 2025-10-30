import { Component, Input } from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {WorkoutItemComponent} from "../workout-item/workout-item.component";

interface Workout {
  date: string;
  type: string;
  duration: number;
}

@Component({
  selector: 'app-workout-list',
  standalone: true,
  imports: [
    NgIf,
    WorkoutItemComponent,
    NgForOf
  ],
  templateUrl: './workout-list.component.html',
  styleUrl: './workout-list.component.scss'
})
export class WorkoutListComponent {
   @Input() workouts: Workout[] = [];
}
