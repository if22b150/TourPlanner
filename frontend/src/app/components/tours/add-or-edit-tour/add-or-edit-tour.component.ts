import { Component, Input } from '@angular/core';
import {finalize} from "rxjs";
import {NotificationService} from "../../../services/notification.service";
import {FormBuilder, FormControl, ReactiveFormsModule, Validators} from "@angular/forms";
import {DatePipe} from "@angular/common";
import {MdbModalRef} from "mdb-angular-ui-kit/modal";
import {ModalComponent} from "../../utils/modal/modal.component";
import {TourModel} from "../../../models/tour.model";
import {FormInputComponent} from "../../utils/form-input/form-input.component";
import { TourService } from 'src/app/services/tour.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-add-or-edit-tour',
  standalone: true,
  //providers: [HeroService],
  imports: [
    ModalComponent,
    ReactiveFormsModule,
    FormInputComponent
  ],
  templateUrl: './add-or-edit-tour.component.html',
  styleUrl: './add-or-edit-tour.component.scss'
})
export class AddOrEditTourComponent {
  @Input({required: true}) tour!: TourModel

  loading: boolean = false

  formGroup= this.formBuilder.group({
    name: [null, Validators.required],
    from: [null, Validators.required],
    to: [null, Validators.required],
    transportType: [null, Validators.required],
    description: [null, Validators.required]
  })

  constructor(public modalRef: MdbModalRef<ModalComponent>,
              private datePipe: DatePipe,
              private notificationService: NotificationService,
              private formBuilder: FormBuilder,
              private tourService: TourService
            ) {}

  ngOnInit() {
    if (this.tour) {
      this.name?.setValue(this.tour.name);
      this.from?.setValue(this.tour.from);
      this.to?.setValue(this.tour.to);
      this.transportType?.setValue(this.tour.transportType);
      this.description?.setValue(this.tour.description)
    }
  }

  submit() {
    this.formGroup.markAllAsTouched();

    if(this.formGroup.invalid)
      return;

    let resource = this.formGroup.value
    console.log(resource)

    this.loading = true
    this.tourService.create(resource)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (tour) => {
          this.modalRef.close(tour)
        },
        error: (e: HttpErrorResponse) => {
          if (e?.error?.errors?.contractNumber == "The contract number has already been taken.")
            this.notificationService.notify("The Contract Number already exists for another customer.", "warning", true, 4000)
          else if (e?.error?.errors?.name == "The name has already been taken.")
            this.notificationService.notify("The Name already exists for another customer.", "warning", true, 4000)
          else
            this.notificationService.notify("Tour konnte nicht erstellt werden", "danger")
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
