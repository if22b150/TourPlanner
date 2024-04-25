import {Component, Input} from '@angular/core';
import {MdbPopconfirmRef} from "mdb-angular-ui-kit/popconfirm";
import {MdbButtonComponent} from "../mdb-button/mdb-button.component";

@Component({
  selector: 'app-popconfirm',
  standalone: true,
  imports: [
    MdbButtonComponent
  ],
  templateUrl: './popconfirm.component.html',
  styleUrl: './popconfirm.component.scss'
})
export class PopconfirmComponent {
  @Input({required: true}) text!: string;
  @Input() submitText: string = "Submit";
  @Input() cancelText: string = "Cancel";
  constructor(public popconfirmRef: MdbPopconfirmRef<PopconfirmComponent>) {}
}
