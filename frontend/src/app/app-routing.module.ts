import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ToursComponent} from "./components/tours/tours.component";

const routes: Routes = [
  {
    path: "tours",
    component: ToursComponent
  },
  {
    path: "**",
    redirectTo: 'tours',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
