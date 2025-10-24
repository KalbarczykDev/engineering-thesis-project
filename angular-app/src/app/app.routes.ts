import { Routes } from '@angular/router';
import {HomeComponent} from "./features/home/home.component";
import {ExerciseListComponent} from "./features/exercise/components/exercise-list/exercise-list.component";

export const routes: Routes = [
  {path : '', component: HomeComponent},
  {path : 'exercises',component: ExerciseListComponent},
  {path : '**' , redirectTo: ''}
];
