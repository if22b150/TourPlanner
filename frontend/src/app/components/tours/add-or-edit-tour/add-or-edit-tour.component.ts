import { Component } from '@angular/core';
import {NotificationService} from "../../../services/notification.service";
import {FormBuilder, FormControl, ReactiveFormsModule, Validators} from "@angular/forms";
import {DatePipe} from "@angular/common";
import {MdbModalRef} from "mdb-angular-ui-kit/modal";
import {ModalComponent} from "../../utils/modal/modal.component";
import {TourModel} from "../../../models/tour.model";
import {FormInputComponent} from "../../utils/form-input/form-input.component";

@Component({
  selector: 'app-add-or-edit-tour',
  standalone: true,
  imports: [
    ModalComponent,
    ReactiveFormsModule,
    FormInputComponent
  ],
  templateUrl: './add-or-edit-tour.component.html',
  styleUrl: './add-or-edit-tour.component.scss'
})
export class AddOrEditTourComponent {
  tour: TourModel | null = null
  loading: boolean = false

  formGroup= this.formBuilder.group({
    name: [null, Validators.required]
  })

  constructor(public modalRef: MdbModalRef<ModalComponent>,
              private datePipe: DatePipe,
              private notificationService: NotificationService,
              private formBuilder: FormBuilder) {}

  ngOnInit() {
    if (this.tour) {
      this.name?.setValue(this.tour.name);
    }
  }

  submit() {
    this.formGroup.markAllAsTouched();

    if(this.formGroup.invalid)
      return;

    let resource = this.formGroup.value

    this.loading = true
    // this.customerService.create(resource)
    //   .pipe(finalize(() => this.loading = false))
    //   .subscribe({
    //     next: (customer) => {
    //       this.modalRef.close(customer)
    //     },
    //     error: (e: HttpErrorResponse) => {
    //       console.log("Create customer error:")
    //       console.log(e)
    //
    //       if (e?.error?.errors?.contractNumber == "The contract number has already been taken.")
    //         this.customNotificationService.notify("The Contract Number already exists for another customer.", "warning", true, 4000)
    //       else if (e?.error?.errors?.name == "The name has already been taken.")
    //         this.customNotificationService.notify("The Name already exists for another customer.", "warning", true, 4000)
    //       else
    //         this.customNotificationService.notify("Customer could not be created.", "danger")
    //     }
    //   })
  }

  get name(): FormControl | null {
    return this.formGroup.get('name') as FormControl
  }
}
