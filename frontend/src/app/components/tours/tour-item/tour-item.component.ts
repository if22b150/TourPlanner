import {Component, Input} from '@angular/core';
import {TourModel} from "../../../models/tour.model";

@Component({
  selector: 'app-tour-item',
  standalone: true,
  imports: [],
  templateUrl: './tour-item.component.html',
  styleUrl: './tour-item.component.scss'
})
export class TourItemComponent {
  @Input({required: true}) tour!: TourModel



}
