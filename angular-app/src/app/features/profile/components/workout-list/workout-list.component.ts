import { Component, Input } from '@angular/core';
import { Workout } from '../../models/workout';
import {WorkoutItemComponent} from "../workout-item/workout-item.component";
import {NgForOf, NgIf} from "@angular/common";


@Component({
  selector: 'app-workout-list',
  standalone: true,
  imports: [
    WorkoutItemComponent,
    NgForOf,
    NgIf
  ],
  templateUrl: './workout-list.component.html',
  styleUrl: './workout-list.component.scss'
})
export class WorkoutListComponent {
   @Input() workouts: Workout[] = [];
}
