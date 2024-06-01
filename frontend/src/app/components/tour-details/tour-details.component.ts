import {Component, EventEmitter, Input} from '@angular/core';
import {AsyncPipe, DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {HeadingComponent} from "../utils/heading/heading.component";
import {SpinnerComponent} from "../utils/spinner/spinner.component";
import {TourHelper, TourItemComponent} from "../tours/tour-item/tour-item.component";
import {TourModel} from "../../models/tour.model";
import {TourLogModel} from "../../models/tour-log.model";
import {Router} from "@angular/router";
import {MdbModalService} from "mdb-angular-ui-kit/modal";
import {NotificationService} from "../../services/notification.service";
import {TourService} from "../../services/tour.service";
import {filter, finalize} from "rxjs";
import {TourLogService} from "../../services/tour-log.service";
import {TourLogsComponent} from "./tour-logs/tour-logs.component";
import { AddOrEditTourLogComponent } from './tour-logs/add-or-edit-tour-log/add-or-edit-tour-log.component';
import {BreadcrumbService} from "../../services/breadcrumb.service";
import {StarRatingComponent} from "../utils/star-rating/star-rating.component";
import {MdbTooltipModule} from "mdb-angular-ui-kit/tooltip";

@Component({
  selector: 'app-tour-details',
  standalone: true,
  imports: [
    AsyncPipe,
    HeadingComponent,
    NgForOf,
    NgIf,
    SpinnerComponent,
    TourItemComponent,
    TourLogsComponent,
    StarRatingComponent,
    MdbTooltipModule,
    DecimalPipe
  ],
  templateUrl: './tour-details.component.html',
  styleUrl: './tour-details.component.scss'
})
export class TourDetailsComponent {
  tour: TourModel | undefined
  urlTourId: number | undefined;

  tourLogs: TourLogModel[] | undefined
  tourLogAdded = new EventEmitter<TourLogModel>()
  loading: boolean = false
  generateLoading: boolean = false

  @Input()
  set id(tourId: string) {
    this.urlTourId = +tourId
    if (isNaN(this.urlTourId))
      this.handleInvalidTour()
    this.initTourLogs()
  }

  constructor(private router: Router,
              private notificationService: NotificationService,
              private modalService: MdbModalService,
              public tourService: TourService,
              private breadcrumbService: BreadcrumbService,
  ) {
  }

  initTourLogs() {
    this.tourService.data$
      .pipe(filter(t => t != null))
      .subscribe({
        next: (data: TourModel[]) => {
          let t: TourModel | undefined
          if (!(t = data.find(t => t.id === this.urlTourId))) {
            this.handleInvalidTour()
            return
          }

          this.tour = t

          this.breadcrumbService.tour = t
        }
      })
  }

  add() {
    let modalRef = this.modalService.open(AddOrEditTourLogComponent,
      {
        data: { tour: this.tour },
      })
    modalRef.onClose.subscribe((value) => {
      if(!value?.id)
        return;

      this.tourLogAdded.emit(value)
      this.notificationService.notify("Die Tour wurde erfolgreich erstellt.")
    })
  }

  generateReport() {
    this.generateLoading = true
    this.tourService.downloadReport(this.tour!.id)
      .pipe(finalize(() => this.generateLoading = false))
      .subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `tour_${this.tour!.id}_report.pdf`;
          document.body.appendChild(a);
          a.click();
          window.URL.revokeObjectURL(url);
          document.body.removeChild(a);
        }
      })
  }

  handleInvalidTour() {
    this.router.navigate(['/tours']).then(() => this.notificationService.notify("Tour existiert nicht.", "danger"))
  }

  protected readonly TourHelper = TourHelper;
}
