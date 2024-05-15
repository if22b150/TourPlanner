import {Component, Input, OnInit} from '@angular/core';
import {finalize} from "rxjs";
import {NotificationService} from "../../../services/notification.service";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {DatePipe} from "@angular/common";
import {MdbModalRef} from "mdb-angular-ui-kit/modal";
import {ModalComponent} from "../../utils/modal/modal.component";
import {TourModel} from "../../../models/tour.model";
import {FormInputComponent} from "../../utils/form-input/form-input.component";
import { TourService } from 'src/app/services/tour.service';
import { HttpErrorResponse } from '@angular/common/http';
import {TextAreaInputComponent} from "../../utils/text-area-input/text-area-input.component";

@Component({
  selector: 'app-add-or-edit-tour',
  standalone: true,
  //providers: [HeroService],
  imports: [
    ModalComponent,
    ReactiveFormsModule,
    FormInputComponent,
    TextAreaInputComponent
  ],
  templateUrl: './add-or-edit-tour.component.html',
  styleUrl: './add-or-edit-tour.component.scss'
})
export class AddOrEditTourComponent implements OnInit {
  @Input() tour?: TourModel

  loading: boolean = false

  formGroup!: FormGroup

  constructor(public modalRef: MdbModalRef<ModalComponent>,
              private datePipe: DatePipe,
              private notificationService: NotificationService,
              private formBuilder: FormBuilder,
              private tourService: TourService
            ) {}

  ngOnInit() {
    this.formGroup = this.formBuilder.group({
      name: [this.tour?.name, Validators.required],
      from: [this.tour?.from, Validators.required],
      to: [this.tour?.to, Validators.required],
      transportType: [this.tour?.transportType, Validators.required],
      description: [this.tour?.description, Validators.required]
    })
  }

  submit() {
    if (!this.tour)
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
    this.tourService.create(resource)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (tour) => {
          this.modalRef.close(tour)
        },
        error: (e: HttpErrorResponse) => {
          this.notificationService.notify("Tour konnte nicht erstellt werden", "danger")
        }
      })
  }

  update() {
    this.formGroup.markAllAsTouched();

    if(this.formGroup.invalid)
      return;

    let resource = this.formGroup.value

    this.loading = true
    this.tourService.update(this.tour!.id, resource)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (tour) => {
          this.modalRef.close(tour)
        },
        error: (e: HttpErrorResponse) => {
          this.notificationService.notify("Tour konnte nicht bearbeitet werden", "danger")
        }
      })
  }

  get name(): FormControl | null {
    return this.formGroup.get('name') as FormControl
  }

  get from(): FormControl | null {
    return this.formGroup.get('from') as FormControl
  }

  get to(): FormControl | null {
    return this.formGroup.get('to') as FormControl
  }

  get transportType(): FormControl | null {
    return this.formGroup.get('transportType') as FormControl
  }

  get description(): FormControl | null {
    return this.formGroup.get('description') as FormControl
  }
}
