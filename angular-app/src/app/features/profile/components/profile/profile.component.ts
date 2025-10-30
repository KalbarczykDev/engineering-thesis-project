import {Component} from '@angular/core';
import {WorkoutListComponent} from "../workout-list/workout-list.component";
import {Workout} from "../../models/workout";
import {ProfileService} from "../../services/profile.service";


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

  user: { username: string; email: string } | null = null;
  workouts: Workout[] = [];

  constructor(private profileService: ProfileService) {
  }

  ngOnInit() {
    this.profileService.getUser().subscribe(u => this.user = u);
    this.profileService.getWorkouts().subscribe(w => this.workouts = w);
  }


}
