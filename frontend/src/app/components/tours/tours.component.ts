import { Component } from '@angular/core';
import {HeadingComponent} from "../utils/heading/heading.component";
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'app-tours',
  standalone: true,
  imports: [
    HeadingComponent
  ],
  templateUrl: './tours.component.html',
  styleUrl: './tours.component.scss'
})
export class ToursComponent {
  constructor(private notificationService: NotificationService) {
  }

  add() {
    this.notificationService.notify("Add clicked!")
  }
}
