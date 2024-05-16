import {Component, EventEmitter, Input, Output} from '@angular/core';
import {TourLogModel} from "../../../../models/tour-log.model";
import {MdbModalService} from "mdb-angular-ui-kit/modal";
import {MdbPopconfirmService} from "mdb-angular-ui-kit/popconfirm";
import {NotificationService} from "../../../../services/notification.service";
import {TourLogService} from "../../../../services/tour-log.service";
import {MdbButtonComponent} from "../../../utils/mdb-button/mdb-button.component";
import {MdbTooltipModule} from "mdb-angular-ui-kit/tooltip";
import {DecimalPipe} from "@angular/common";
import {StarRatingComponent} from "../../../utils/star-rating/star-rating.component";

@Component({
  selector: '[app-tour-log-row]',
  standalone: true,
  imports: [
    MdbButtonComponent,
    MdbTooltipModule,
    DecimalPipe,
    StarRatingComponent
  ],
  templateUrl: './tour-log-row.component.html',
  styleUrl: './tour-log-row.component.scss'
})
export class TourLogRowComponent {
  @Input({required: true}) tourLog!: TourLogModel
  @Output() onDeleted = new EventEmitter<number>()
  @Output() onUpdated = new EventEmitter<TourLogModel>()
  loading: boolean = false

  constructor(private modalService: MdbModalService,
              private popconfirmService: MdbPopconfirmService,
              private notificationService: NotificationService,
              public tourLogService: TourLogService) {
  }

  edit() {

  }

  delete(event: Event) {

  }
}
