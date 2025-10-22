import { Component } from '@angular/core';
import {HttpClientModule} from "@angular/common/http";
import {ExerciseListComponent} from "./features/exercise/components/exercise-list/exercise-list.component";
import {RouterOutlet} from "@angular/router";
import {HeaderComponent} from "./core/layout/header/header.component";
import {FooterComponent} from "./core/layout/footer/footer.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HttpClientModule, ExerciseListComponent, RouterOutlet, HeaderComponent, FooterComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

}
