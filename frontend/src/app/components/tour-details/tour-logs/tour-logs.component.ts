import {Component, EventEmitter, Input, OnInit} from '@angular/core';
import {TourLogModel} from "../../../models/tour-log.model";
import {TourModel} from "../../../models/tour.model";
import {finalize} from "rxjs";
import {TourLogService} from "../../../services/tour-log.service";
import {NotificationService} from "../../../services/notification.service";
import {NgForOf, NgIf} from "@angular/common";
import {SpinnerComponent} from "../../utils/spinner/spinner.component";
import {TourLogRowComponent} from "./tour-log-row/tour-log-row.component";
import {TourHelper} from "../../tours/tour-item/tour-item.component";
import {TextSearchComponent} from "../../utils/text-search/text-search.component";

@Component({
  selector: 'app-tour-logs',
  standalone: true,
  imports: [
    NgIf,
    SpinnerComponent,
    TourLogRowComponent,
    NgForOf,
    TextSearchComponent
  ],
  templateUrl: './tour-logs.component.html',
  styleUrl: './tour-logs.component.scss'
})
export class TourLogsComponent implements OnInit {
  @Input({required: true}) tour!: TourModel
  @Input() tourLogAdded = new EventEmitter<TourLogModel>()

  tourLogs: TourLogModel[] | undefined
  filteredTourLogs: TourLogModel[] | undefined
  searchText: string | null = null
  loading: boolean = false

  constructor(public tourLogService: TourLogService,
              private notificationService: NotificationService) {
  }

  ngOnInit() {
    this.fetchTourLogs()
    this.tourLogAdded.subscribe({
      next: (tl: TourLogModel) => {
        this.tourLogs!.push(tl);
      }
    })
  }

  fetchTourLogs() {
    this.loading = true
    this.tourLogService.getAll(this.tour.id)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (tourLogs: TourLogModel[]) => {
          this.tourLogs = tourLogs;
          this.filteredTourLogs = tourLogs
        },
        error: (e) => {
          console.log(e);
          this.notificationService.notify("TourLogs could not be loaded.", "danger")
        }
      })
  }

  onDeleted(id: number) {
    this.tourLogs = this.tourLogs?.filter(t => t.id != id)
    this.notificationService.notify("Der Log wurde gelöscht.")
  }

  onUpdated(tourLog: TourLogModel) {
    this.tourLogs = this.tourLogs?.map(t => t.id != tourLog.id ? t : tourLog)
    this.notificationService.notify("Der Log wurde bearbeitet.")
  }

  filterTourLogs(searchText: string | null) {
    if(!searchText || searchText == "") {
      this.filteredTourLogs = this.tourLogs
    }

    this.searchText = searchText

    this.filteredTourLogs = this.tourLogs?.filter(t => TourHelper.containsSubstring(t.comment, searchText!))
  }
}
