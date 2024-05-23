import {Component, Input, OnInit} from '@angular/core';
import {finalize} from "rxjs";
import {NotificationService} from "../../../../services/notification.service";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {DatePipe, NgIf} from "@angular/common";
import {MdbModalRef} from "mdb-angular-ui-kit/modal";
import {ModalComponent} from "../../../utils/modal/modal.component";
import {TourModel} from "../../../../models/tour.model";
import { TourLogModel } from 'src/app/models/tour-log.model';
import {FormInputComponent} from "../../../utils/form-input/form-input.component";
import { TourLogService } from 'src/app/services/tour-log.service';
import { HttpErrorResponse } from '@angular/common/http';
import {TextAreaInputComponent} from "../../../utils/text-area-input/text-area-input.component";
import {MdbFormsModule} from "mdb-angular-ui-kit/forms";
import {MdbDatepickerModule} from "mdb-angular-ui-kit/datepicker";
import {MdbValidationModule} from "mdb-angular-ui-kit/validation";

@Component({
  selector: 'app-add-or-edit-tour-log',
  standalone: true,
  imports: [
    ModalComponent,
    ReactiveFormsModule,
    FormInputComponent,
    TextAreaInputComponent,
    MdbFormsModule,
    MdbDatepickerModule,
    MdbValidationModule,
    NgIf
  ],
  templateUrl: './add-or-edit-tour-log.component.html',
  styleUrl: './add-or-edit-tour-log.component.scss'
})
export class AddOrEditTourLogComponent implements OnInit {
  @Input({required: true}) tour!: TourModel
  @Input() tourLog?: TourLogModel;

  loading: boolean = false

  formGroup!: FormGroup

  constructor(public modalRef: MdbModalRef<ModalComponent>,
              private datePipe: DatePipe,
              private notificationService: NotificationService,
              private formBuilder: FormBuilder,
              private tourLogService: TourLogService
            ) {}

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      date: [this.tourLog?.date ? new Date(this.tourLog!.date) : null, Validators.required],
      totalTime: [this.tourLog?.totalTime, Validators.required],
      comment: [this.tourLog?.comment, Validators.required],
      difficulty: [this.tourLog?.difficulty, Validators.required],
      totalDistance: [this.tourLog?.totalDistance, Validators.required],
      rating: [this.tourLog?.rating, Validators.required],
    })
  }

  submit() {
    if (!this.tourLog)
      this.create();
    else
      this.update();
  }

  create() {
    this.formGroup.markAllAsTouched();

    if(this.formGroup.invalid)
      return;

    let resource = this.formGroup.value

    this.loading = true
    this.tourLogService.create(this.tour.id, resource)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (tourLog) => {
          this.modalRef.close(tourLog)
        },
        error: (e: HttpErrorResponse) => {
          this.notificationService.notify("Tour Log konnte nicht erstellt werden", "danger")
        }
      })
  }

  update() {
    this.formGroup.markAllAsTouched();

    if(this.formGroup.invalid)
      return;

    let resource = this.formGroup.value

    this.loading = true
    this.tourLogService.update(this.tour.id, this.tourLog!.id, resource)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (tourLog) => {
          this.modalRef.close(tourLog)
        },
        error: (e: HttpErrorResponse) => {
          this.notificationService.notify("Tour Log konnte nicht bearbeitet werden", "danger")
        }
      })
  }

  get totalDistance(): FormControl | null {
    return this.formGroup.get('totalDistance') as FormControl
  }

  get totalTime(): FormControl | null {
    return this.formGroup.get('totalTime') as FormControl
  }

  get date(): FormControl | null {
    return this.formGroup.get('date') as FormControl
  }

  get difficulty(): FormControl | null {
    return this.formGroup.get('difficulty') as FormControl
  }

  get rating(): FormControl | null {
    return this.formGroup.get('rating') as FormControl
  }

  get comment(): FormControl | null {
    return this.formGroup.get('comment') as FormControl
  }
}
