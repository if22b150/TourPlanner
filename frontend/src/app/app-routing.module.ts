import { NgModule } from '@angular/core';
import {provideRouter, RouterModule, Routes, withComponentInputBinding} from '@angular/router';
import {ToursComponent} from "./components/tours/tours.component";
import {TourDetailsComponent} from "./components/tour-details/tour-details.component";

const routes: Routes = [
  {
    path: "tours",
    component: ToursComponent
  },
  {
    path: "tours/:id",
    component: TourDetailsComponent
  },
  {
    path: "**",
    redirectTo: 'tours',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [provideRouter(routes, withComponentInputBinding())]
})
export class AppRoutingModule { }
