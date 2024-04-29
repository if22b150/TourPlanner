import { Component } from '@angular/core';
import {TourService} from "./services/tour.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'frontend';

  constructor(private tourService: TourService) {
  }

  ngOnInit() {
    this.tourService.getAll();
  }
}
