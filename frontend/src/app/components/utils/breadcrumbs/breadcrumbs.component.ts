import { Component } from '@angular/core';
import {RouterLink} from "@angular/router";
import {BreadcrumbService} from "../../../services/breadcrumb.service";
import {AsyncPipe, NgIf} from "@angular/common";

@Component({
  selector: 'app-breadcrumbs',
  standalone: true,
  imports: [
    RouterLink,
    AsyncPipe,
    NgIf
  ],
  templateUrl: './breadcrumbs.component.html',
  styleUrl: './breadcrumbs.component.scss'
})
export class BreadcrumbsComponent {
  constructor(public breadcrumbService: BreadcrumbService) {
  }
}
