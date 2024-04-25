import {Component, Input} from '@angular/core';
import {AbstractControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MdbFormsModule} from "mdb-angular-ui-kit/forms";
import {MdbValidationModule} from "mdb-angular-ui-kit/validation";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-form-input',
  standalone: true,
  imports: [
    FormsModule,
    MdbFormsModule,
    MdbValidationModule,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './form-input.component.html',
  styleUrl: './form-input.component.scss'
})
export class FormInputComponent {
  @Input({required: true}) id!: string;
  @Input({required: true}) label!: string;
  @Input({required: true}) controlName!: string;
  @Input({required: true}) formGroup!: FormGroup;
  @Input() isRequired: boolean = false;
  @Input() type: string = 'text';

  get control(): AbstractControl | null {
    return this.formGroup!.get(this.controlName!)
  }
}
