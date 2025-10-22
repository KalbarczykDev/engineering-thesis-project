import { Component } from '@angular/core';
import {HttpClientModule} from "@angular/common/http";
import {ExerciseListComponent} from "./features/exercise/components/exercise-list/exercise-list.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HttpClientModule, ExerciseListComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

}
