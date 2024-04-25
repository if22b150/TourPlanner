import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MdbModalRef} from "mdb-angular-ui-kit/modal";
import {MdbButtonComponent} from "../mdb-button/mdb-button.component";

@Component({
  selector: 'app-modal',
  standalone: true,
  imports: [
    MdbButtonComponent
  ],
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.scss'
})
export class ModalComponent {
  @Input({required: true}) title!: string;
  @Input() submitText: string = "Submit";
  @Input() closeText: string = "Close";
  @Input() loading: boolean = false;
  @Output() submitClicked: EventEmitter<any> = new EventEmitter();

  constructor(public modalRef: MdbModalRef<ModalComponent>) {}
}
