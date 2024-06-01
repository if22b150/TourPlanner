import { Component } from '@angular/core';
import {TourService} from "./services/tour.service";
import {filter, finalize} from "rxjs";
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'frontend';
  generateLoading: boolean = false
  currentRoute: string | undefined

  constructor(private tourService: TourService,
              private router: Router) {
  }

  generateReport() {
    this.generateLoading = true
    this.tourService.downloadSummaryReport()
      .pipe(finalize(() => this.generateLoading = false))
      .subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `tours_summary_report.pdf`;
          document.body.appendChild(a);
          a.click();
          window.URL.revokeObjectURL(url);
          document.body.removeChild(a);
        }
      })
  }


  ngOnInit() {
    this.tourService.getAll();
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe({
      next: (event ) => {
        this.currentRoute = (event as NavigationEnd).urlAfterRedirects;
      }
    });
  }
}
