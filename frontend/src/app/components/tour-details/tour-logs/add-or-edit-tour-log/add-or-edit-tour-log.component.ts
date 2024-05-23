import {Component, Input, OnInit} from '@angular/core';
import {finalize} from "rxjs";
import {NotificationService} from "../../../../services/notification.service";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {DatePipe} from "@angular/common";
import {MdbModalRef} from "mdb-angular-ui-kit/modal";
import {ModalComponent} from "../../../utils/modal/modal.component";
import {TourModel} from "../../../../models/tour.model";
import { TourLogModel } from 'src/app/models/tour-log.model';
import {FormInputComponent} from "../../../utils/form-input/form-input.component";
import { TourService } from 'src/app/services/tour.service';
import { TourLogService } from 'src/app/services/tour-log.service';
import { HttpErrorResponse } from '@angular/common/http';
import {TextAreaInputComponent} from "../../../utils/text-area-input/text-area-input.component";

@Component({
  selector: 'app-add-or-edit-tour-log',
  standalone: true,
  imports: [
    ModalComponent,
    ReactiveFormsModule,
    FormInputComponent,
    TextAreaInputComponent
  ],
  templateUrl: './add-or-edit-tour-log.component.html',
  styleUrl: './add-or-edit-tour-log.component.scss'
})
export class AddOrEditTourLogComponent implements OnInit {
  @Input() tour?: TourModel
  @Input()
  tourLog!: TourLogModel;

  @Input()
  urlTourId!: number;


  loading: boolean = false

  formGroup!: FormGroup

  constructor(public modalRef: MdbModalRef<ModalComponent>,
              private datePipe: DatePipe,
              private notificationService: NotificationService,
              private formBuilder: FormBuilder,
              private tourService: TourService,
              private tourLogService: TourLogService
            ) {}

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      date: [this.tourLog?.date, Validators.required],
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
    this.tourLogService.create(this.urlTourId, resource)
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
    this.tourLogService.update(this.urlTourId, this.tourLog.id, resource)
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
    return this.formGroup.get('name') as FormControl
  }

  get totalTime(): FormControl | null {
    return this.formGroup.get('from') as FormControl
  }

  get date(): FormControl | null {
    return this.formGroup.get('to') as FormControl
  }

  get difficulty(): FormControl | null {
    return this.formGroup.get('transportType') as FormControl
  }

  get rating(): FormControl | null {
    return this.formGroup.get('description') as FormControl
  }

  get comment(): FormControl | null {
    return this.formGroup.get('description') as FormControl
  }
}
