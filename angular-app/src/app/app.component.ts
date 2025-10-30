import {Component} from '@angular/core';
import {HttpClientModule} from "@angular/common/http";
import {RouterOutlet} from "@angular/router";
import {HeaderComponent} from "./core/layout/header/header.component";
import {FooterComponent} from "./core/layout/footer/footer.component";
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HttpClientModule, RouterOutlet, HeaderComponent, FooterComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  constructor(private router: Router) {
  }

  isHome(): boolean {
    return this.router.url === '/';
  }
}
