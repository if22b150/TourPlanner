import {Component, Input} from '@angular/core';
import {finalize} from "rxjs";
import {TourModel} from "../../../models/tour.model";
import { TourService } from 'src/app/services/tour.service';
import { NotificationService } from 'src/app/services/notification.service';
import {MdbModalService} from "mdb-angular-ui-kit/modal";
import { AddOrEditTourComponent } from '../add-or-edit-tour/add-or-edit-tour.component';

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

  constructor(private notificationService: NotificationService,
    public tourService: TourService,
    private modalService: MdbModalService) {

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

  edit() {
    const modalRef = this.modalService.open(AddOrEditTourComponent, { data: { tour: this.tour } });
    modalRef.onClose.subscribe((value) => {
      if(!value?.id)
        return;

      this.tourService.getAll();
      this.notificationService.notify("Die Tour wurde erfolgreich editiert.")
    })
  }
}