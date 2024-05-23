import {Component, Input, OnInit} from '@angular/core';
import {TourLogModel} from "../../../models/tour-log.model";
import {TourModel} from "../../../models/tour.model";
import {finalize} from "rxjs";
import {TourLogService} from "../../../services/tour-log.service";
import {NotificationService} from "../../../services/notification.service";
import {NgForOf, NgIf} from "@angular/common";
import {SpinnerComponent} from "../../utils/spinner/spinner.component";
import {TourLogRowComponent} from "./tour-log-row/tour-log-row.component";

@Component({
  selector: 'app-tour-logs',
  standalone: true,
  imports: [
    NgIf,
    SpinnerComponent,
    TourLogRowComponent,
    NgForOf
  ],
  templateUrl: './tour-logs.component.html',
  styleUrl: './tour-logs.component.scss'
})
export class TourLogsComponent implements OnInit {
  @Input({required: true}) tour!: TourModel
  

  tourLogs: TourLogModel[] | undefined
  loading: boolean = false

  constructor(public tourLogService: TourLogService,
              private notificationService: NotificationService) {
  }

  ngOnInit() {
    this.loading = true
    this.tourLogService.getAll(this.tour.id)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (tourLogs: TourLogModel[]) => {
          this.tourLogs = tourLogs;
        },
        error: (e) => {
          console.log(e);
          this.notificationService.notify("TourLogs could not be loaded.", "danger")
        }
      })
  }
  onDeleted(id: number) {
    //this.tourLogs = this.tourLogs?.filter(t => t.id != id)
    this.notificationService.notify("Die Tour wurde gelöscht.")
    this.tourLogService.getAll(this.tour.id)
  }

  onUpdated(tour: TourLogModel) {
    //this.tourLogs = this.tourLogs?.map(t => t.id != t.id ? tour : t)
    this.notificationService.notify("Die Tour wurde bearbeitet.")
    this.tourLogService.getAll(this.tour.id)
  }
}
