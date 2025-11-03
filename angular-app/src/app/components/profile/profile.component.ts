import {Component} from '@angular/core';
import {WorkoutListComponent} from "../workout-list/workout-list.component";
import {Workout} from "../../models/workout";
import {ProfileService} from "../../services/profile/profile.service";
import {NgOptimizedImage} from "@angular/common";


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [
    WorkoutListComponent,
  ],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {

  user: { username: string; email: string } | null = null;
  workouts: Workout[] = [];
  profilePic: string | ArrayBuffer | null = null;
  defaultPic = 'https://via.placeholder.com/120';


  constructor(private profileService: ProfileService) {
  }

  ngOnInit() {
    this.profileService.getUser().subscribe(u => this.user = u);
    this.profileService.getWorkouts().subscribe(w => this.workouts = w);
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    const file = input.files[0];
    const reader = new FileReader();
    reader.onload = () => {
      this.profilePic = reader.result;
    };
    reader.readAsDataURL(file);
  }


}
