import {Component, Input} from '@angular/core';
import {ModalComponent} from "../../utils/modal/modal.component";
import {TourService} from "../../../services/tour.service";
import {NotificationService} from "../../../services/notification.service";
import {MdbModalRef} from "mdb-angular-ui-kit/modal";
import {finalize} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {FormInputComponent} from "../../utils/form-input/form-input.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MdbFormsModule} from "mdb-angular-ui-kit/forms";
import {MdbValidationModule} from "mdb-angular-ui-kit/validation";
import {NgIf} from "@angular/common";
import {TourModel} from "../../../models/tour.model";

@Component({
  selector: 'app-share-tour',
  standalone: true,
  imports: [
    ModalComponent,
    FormInputComponent,
    FormsModule,
    MdbFormsModule,
    MdbValidationModule,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './share-tour.component.html',
  styleUrl: './share-tour.component.scss'
})
export class ShareTourComponent {
  @Input({required: true}) tour!: TourModel

  loading: boolean = false
  email: string | null = null

  constructor(private tourService: TourService,
              private notificationService: NotificationService,
              public modalRef: MdbModalRef<ModalComponent>) {
  }

  submit() {
    if(!this.email)
      return;


    this.loading = true
    this.tourService.share(this.tour.id, this.email)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: () => {
          this.modalRef.close()
        },
        error: (e: HttpErrorResponse) => {
          this.notificationService.notify("Tour konnte nicht per Mail geteilt werden.", "danger")
        }
      })
  }
}
