import {Component, Input} from '@angular/core';
import {finalize} from "rxjs";
import {TourModel} from "../../../models/tour.model";
import { TourService } from 'src/app/services/tour.service';

@Component({
  selector: 'app-tour-item',
  standalone: true,
  imports: [],
  templateUrl: './tour-item.component.html',
  styleUrl: './tour-item.component.scss'
})
export class TourItemComponent {
  @Input({required: true}) tour!: TourModel;
  loading: boolean = false;

  constructor(private tourService: TourService) {

  }

  delete() {
    this.loading = true;
    this.tourService.delete(this.tour.id)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: () => {
          this.tourService.getAll();
        }
      })
  }
}
