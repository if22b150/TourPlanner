import { Component } from '@angular/core';
import {HeadingComponent} from "../utils/heading/heading.component";
import {NotificationService} from "../../services/notification.service";
import {MdbModalService} from "mdb-angular-ui-kit/modal";
import {AddOrEditTourComponent} from "./add-or-edit-tour/add-or-edit-tour.component";
import {TourModel} from "../../models/tour.model";
import {TourService} from "../../services/tour.service";
import {TourItemComponent} from "./tour-item/tour-item.component";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {SpinnerComponent} from "../utils/spinner/spinner.component";

@Component({
  selector: 'app-tours',
  standalone: true,
  imports: [
    HeadingComponent,
    TourItemComponent,
    NgForOf,
    NgIf,
    AsyncPipe,
    SpinnerComponent
  ],
  templateUrl: './tours.component.html',
  styleUrl: './tours.component.scss'
})
export class ToursComponent {
  tours: TourModel[] | undefined;

  constructor(private notificationService: NotificationService,
              public tourService: TourService,
              private modalService: MdbModalService) {
  }

  ngOnInit() {
    this.tourService.data$
      .subscribe({
        next: (data: TourModel[]) => {
          this.tours = data;
        }
      })
  }

  add() {
    let modalRef = this.modalService.open(AddOrEditTourComponent)
    modalRef.onClose.subscribe((value) => {
      if(!value?.id)
        return;

      this.tours?.push(value)
      this.tourService.getAll();
      this.notificationService.notify("Die Tour wurde erfolgreich erstellt.")
    })
  }

  onDeleted(id: number) {
    this.tours = this.tours?.filter(t => t.id != id)
    this.notificationService.notify("Die Tour wurde gelöscht.")
    this.tourService.getAll()
  }

  onUpdated(tour: TourModel) {
    this.tours = this.tours?.map(t => t.id != t.id ? tour : t)
    this.notificationService.notify("Die Tour wurde bearbeitet.")
    this.tourService.getAll()
  }
}
