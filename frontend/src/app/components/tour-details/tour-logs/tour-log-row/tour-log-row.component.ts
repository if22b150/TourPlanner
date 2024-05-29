import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TourLogModel} from "../../../../models/tour-log.model";
import {MdbModalService} from "mdb-angular-ui-kit/modal";
import {MdbPopconfirmService} from "mdb-angular-ui-kit/popconfirm";
import {NotificationService} from "../../../../services/notification.service";
import {TourLogService} from "../../../../services/tour-log.service";
import {MdbButtonComponent} from "../../../utils/mdb-button/mdb-button.component";
import {MdbTooltipModule} from "mdb-angular-ui-kit/tooltip";
import {DatePipe, DecimalPipe} from "@angular/common";
import {StarRatingComponent} from "../../../utils/star-rating/star-rating.component";
import {filter, finalize} from "rxjs";
import {PopconfirmComponent} from "../../../utils/popconfirm/popconfirm.component";
import {AddOrEditTourLogComponent} from '../add-or-edit-tour-log/add-or-edit-tour-log.component';
import {TourModel} from 'src/app/models/tour.model';
import {HighlightTextPipe} from "../../../../pipes/highlight-text.pipe";

@Component({
  selector: '[app-tour-log-row]',
  standalone: true,
  imports: [
    MdbButtonComponent,
    MdbTooltipModule,
    DecimalPipe,
    StarRatingComponent,
    DatePipe,
    HighlightTextPipe
  ],
  templateUrl: './tour-log-row.component.html',
  styleUrl: './tour-log-row.component.scss'
})
export class TourLogRowComponent {
  @Input() tour!: TourModel

  @Input({required: true}) tourLog!: TourLogModel
  @Input() searchText: string | null = null
  @Output() onDeleted = new EventEmitter<number>()
  @Output() onUpdated = new EventEmitter<TourLogModel>()
  loading: boolean = false

  constructor(private modalService: MdbModalService,
              private popconfirmService: MdbPopconfirmService,
              private notificationService: NotificationService,
              public tourLogService: TourLogService) {
  }

  edit() {
    let modalRef = this.modalService.open(AddOrEditTourLogComponent,
      { data: {tourLog: this.tourLog, tour: this.tour}})
    modalRef.onClose.subscribe((value) => {
      if (!value?.id)
        return;

      this.onUpdated.emit(value)
    })
  }

  delete(event: Event) {
    const target = event.target as HTMLElement;
    let popconfirmRef = this.popconfirmService.open(PopconfirmComponent, target, {
      data: {
        text: "Soll der Log wirklich gelöscht werden?",
        submitText: "Ja, löschen",
        cancelText: "Abbrechen"
      }
    })
    popconfirmRef.onConfirm.subscribe({
      next: () => {
        this.loading = true
        this.tourLogService.delete(this.tour.id, this.tourLog.id)
          .pipe(finalize(() => this.loading = false))
          .subscribe({
            next: () => {
              this.onDeleted.emit(this.tourLog.id)
            },
            error: (e) => {
              console.log("Delete tour error:")
              console.log(e)
              this.notificationService.notify("TourLog could not be deleted.", "danger")
            }
          })
      }
    })
  }
}
