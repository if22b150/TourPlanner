import {Component, EventEmitter, Input, Output} from '@angular/core';
import {finalize} from "rxjs";
import {TourModel} from "../../../models/tour.model";
import {TourService} from 'src/app/services/tour.service';
import {NotificationService} from 'src/app/services/notification.service';
import {MdbModalService} from "mdb-angular-ui-kit/modal";
import {AddOrEditTourComponent} from '../add-or-edit-tour/add-or-edit-tour.component';
import {MdbButtonComponent} from "../../utils/mdb-button/mdb-button.component";
import {MdbPopconfirmService} from "mdb-angular-ui-kit/popconfirm";
import {PopconfirmComponent} from "../../utils/popconfirm/popconfirm.component";
import {NgForOf} from "@angular/common";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-tour-item',
  standalone: true,
  imports: [
    MdbButtonComponent,
    NgForOf,
    RouterLink
  ],
  templateUrl: './tour-item.component.html',
  styleUrl: './tour-item.component.scss'
})
export class TourItemComponent {
  @Input({required: true}) tour!: TourModel;

  @Output() onDeleted = new EventEmitter<number>()
  @Output() onUpdated = new EventEmitter<TourModel>()

  loading: boolean = false;

  constructor(private notificationService: NotificationService,
              public tourService: TourService,
              private modalService: MdbModalService,
              private popconfirmService: MdbPopconfirmService) {

  }

  delete(event: Event) {
    const target = event.target as HTMLElement;
    let popconfirmRef = this.popconfirmService.open(PopconfirmComponent, target, {
      data: {
        text: "Soll die Tour wirklich gelöscht werden?",
        submitText: "Ja, löschen",
        cancelText: "Abbrechen"
      }
    })
    popconfirmRef.onConfirm.subscribe({
      next: () => {
        this.loading = true
        this.tourService.delete(this.tour.id)
          .pipe(finalize(() => this.loading = false))
          .subscribe({
            next: () => {
              this.onDeleted.emit(this.tour.id)
            },
            error: (e) => {
              console.log("Delete tour error:")
              console.log(e)
              this.notificationService.notify("Tour could not be deleted.", "danger")
            }
          })
      }
    })
  }

  edit() {
    const modalRef = this.modalService.open(AddOrEditTourComponent, {data: {tour: this.tour}});
    modalRef.onClose.subscribe((value) => {
      if (!value?.id)
        return;

      this.onUpdated.emit(value)
    })
  }

  getTransportations() {
    return this.tour.transportType.split(',').map(t => t.trim())
  }
}