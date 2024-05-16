import {Component, Input} from '@angular/core';
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";
import {HeadingComponent} from "../utils/heading/heading.component";
import {SpinnerComponent} from "../utils/spinner/spinner.component";
import {TourItemComponent} from "../tours/tour-item/tour-item.component";
import {TourModel} from "../../models/tour.model";
import {TourLogModel} from "../../models/tour-log.model";
import {Router} from "@angular/router";
import {MdbModalService} from "mdb-angular-ui-kit/modal";
import {NotificationService} from "../../services/notification.service";
import {TourService} from "../../services/tour.service";
import {filter, finalize} from "rxjs";
import {TourLogService} from "../../services/tour-log.service";
import {TourLogsComponent} from "./tour-logs/tour-logs.component";

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
    TourLogsComponent
  ],
  templateUrl: './tour-details.component.html',
  styleUrl: './tour-details.component.scss'
})
export class TourDetailsComponent {
  tour: TourModel | undefined
  urlTourId: number | undefined

  tourLogs: TourLogModel[] | undefined
  loading: boolean = false

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
              private tourLogService: TourLogService
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

          // this.breadcrumbService.customer = c
        }
      })
  }

  handleInvalidTour() {
    this.router.navigate(['/tours']).then(() => this.notificationService.notify("Tour existiert nicht.", "danger"))
  }
}