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
import {DecimalPipe, NgForOf, NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {HighlightTextPipe} from "../../../pipes/highlight-text.pipe";
import {StarRatingComponent} from "../../utils/star-rating/star-rating.component";
import {MdbTooltipModule} from "mdb-angular-ui-kit/tooltip";

@Component({
  selector: 'app-tour-item',
  standalone: true,
  imports: [
    MdbButtonComponent,
    NgForOf,
    RouterLink,
    HighlightTextPipe,
    StarRatingComponent,
    NgIf,
    DecimalPipe,
    MdbTooltipModule
  ],
  templateUrl: './tour-item.component.html',
  styleUrl: './tour-item.component.scss'
})
export class TourItemComponent {
  @Input({required: true}) tour!: TourModel;
  @Input() searchText: string | null = null;

  @Output() onDeleted = new EventEmitter<number>()
  @Output() onUpdated = new EventEmitter<TourModel>()

  loading: boolean = false;
  exportLoading: boolean = false

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

  export() {
    this.exportLoading = true
    this.tourService.exportToCsv(this.tour.id)
      .pipe(finalize(() => this.exportLoading = false))
      .subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(new Blob([blob]));
          const link = document.createElement('a');
          link.href = url;

          // Set the filename for the downloaded file
          link.setAttribute('download', 'tour_' + this.tour.id + '.csv');

          // Append the anchor element to the body and trigger the click event
          document.body.appendChild(link);
          link.click();

          // Remove the anchor element
          document.body.removeChild(link);
        }
      })
  }

  protected readonly TourHelper = TourHelper;
}

export class TourHelper {
  static getTourTransportations(tour: TourModel): string[] {
    return tour.transportType.split(',').map(t => t.trim())
  }

  static getTourTitle(tour: TourModel): string {
    return `${tour.name}: (${tour.from} - ${tour.to})`
  }

  static containsSubstring(text: string, sub: string): boolean {
    return text.toLowerCase().includes(sub.toLowerCase())
  }
}
