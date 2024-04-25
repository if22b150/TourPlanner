import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MdbRippleModule} from "mdb-angular-ui-kit/ripple";
import {NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-mdb-button',
  standalone: true,
  imports: [
    MdbRippleModule,
    NgIf,
    RouterLink
  ],
  templateUrl: './mdb-button.component.html',
  styleUrl: './mdb-button.component.scss'
})
export class MdbButtonComponent {
  @Input() text: string | undefined
  @Input() iconClass: string | undefined
  @Input() iconPosRight: boolean = false
  @Input() color: string = "primary"
  @Input() size: string | undefined
  @Input() rippleColor: string = "light"
  @Input() outlined: boolean = false
  @Input() floating: boolean = false
  @Input() customClasses: string = ""
  @Input() routerLink: string | undefined
  @Input() loading: boolean = false

  @Output() onClick = new EventEmitter()
}
