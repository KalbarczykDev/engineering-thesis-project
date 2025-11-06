import {Routes} from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {ExerciseListComponent} from "./components/exercise-list/exercise-list.component";
import {WorkoutListComponent} from "./components/workout-list/workout-list.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {RegisterComponent} from "./components/register/register.component";
import {LoginComponent} from "./components/login/login.component";

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'exercises', component: ExerciseListComponent},
  {path: 'workouts', component: WorkoutListComponent},
  {path: 'profile', component: ProfileComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: '**', redirectTo: ''}
];
