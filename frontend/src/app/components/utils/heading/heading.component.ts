import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MdbRippleModule} from "mdb-angular-ui-kit/ripple";
import {NgIf} from "@angular/common";
import {MdbButtonComponent} from "../mdb-button/mdb-button.component";

@Component({
  selector: 'app-heading',
  standalone: true,
  imports: [
    MdbRippleModule,
    NgIf,
    MdbButtonComponent
  ],
  templateUrl: './heading.component.html',
  styleUrl: './heading.component.scss'
})
export class HeadingComponent {
  @Input() title!: string;
  @Input() subtitle: string | undefined;
  @Input() actionText: string | undefined;
  @Input() actionIcon: string | undefined;
  @Output() actionClicked: EventEmitter<any> = new EventEmitter<any>();
}
