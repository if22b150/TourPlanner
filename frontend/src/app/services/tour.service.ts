import { Injectable } from '@angular/core';
import {ResourceService} from "./resource.service";
import {TourModel} from "../models/tour.model";
import {HttpClient} from "@angular/common/http";
import {EnvironmentService} from "../app.module";

@Injectable({
  providedIn: 'root'
})
export class TourService extends ResourceService<TourModel> {
  constructor(http: HttpClient,
              environmentService: EnvironmentService) {
    super('tours', 'tours', http, environmentService);
  }
}
