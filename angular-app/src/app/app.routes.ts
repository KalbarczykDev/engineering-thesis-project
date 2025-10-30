import {Routes} from '@angular/router';
import {HomeComponent} from "./features/home/home.component";
import {ExerciseListComponent} from "./features/exercise/components/exercise-list/exercise-list.component";
import {WorkoutListComponent} from "./features/workout/components/workout-list/workout-list.component";
import {ProfileComponent} from "./features/profile/components/profile/profile.component";

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'exercises', component: ExerciseListComponent},
  {path: 'workouts', component: WorkoutListComponent},
  {path: 'profile', component: ProfileComponent},
  {path: '**', redirectTo: ''}
];
