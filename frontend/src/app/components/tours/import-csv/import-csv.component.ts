import { Component } from '@angular/core';
import {FormInputComponent} from "../../utils/form-input/form-input.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MdbFormsModule} from "mdb-angular-ui-kit/forms";
import {MdbOptionModule} from "mdb-angular-ui-kit/option";
import {MdbSelectModule} from "mdb-angular-ui-kit/select";
import {ModalComponent} from "../../utils/modal/modal.component";
import {NgForOf} from "@angular/common";
import {TextAreaInputComponent} from "../../utils/text-area-input/text-area-input.component";
import {finalize} from "rxjs";
import {TourService} from "../../../services/tour.service";
import {MdbModalRef} from "mdb-angular-ui-kit/modal";
import {HttpErrorResponse} from "@angular/common/http";
import {NotificationService} from "../../../services/notification.service";

@Component({
  selector: 'app-import-csv',
  standalone: true,
    imports: [
        FormInputComponent,
        FormsModule,
        MdbFormsModule,
        MdbOptionModule,
        MdbSelectModule,
        ModalComponent,
        NgForOf,
        ReactiveFormsModule,
        TextAreaInputComponent
    ],
  templateUrl: './import-csv.component.html',
  styleUrl: './import-csv.component.scss'
})
export class ImportCsvComponent {
  loading: boolean = false
  file: File | null = null

  constructor(private tourService: TourService,
              private notificationService: NotificationService,
              public modalRef: MdbModalRef<ModalComponent>) {
  }

  onFileSelected(event: any) {
    this.file = event.target.files[0];
  }

  submit() {
    if(!this.file)
      return;

    console.log(this.file)

    this.loading = true
    this.tourService.importCsv(this.file)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: () => {
          this.modalRef.close()
        },
        error: (e: HttpErrorResponse) => {
          this.notificationService.notify("Touren konnte nicht importiert werden", "danger")
        }
      })
  }
}
