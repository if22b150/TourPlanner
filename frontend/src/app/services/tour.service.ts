import { Injectable } from '@angular/core';
import {ResourceService} from "./resource.service";
import {TourModel} from "../models/tour.model";
import {HttpClient} from "@angular/common/http";
import {EnvironmentService} from "../app.module";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class TourService extends ResourceService<TourModel> {
  constructor(http: HttpClient,
              environmentService: EnvironmentService) {
    super('tours', 'tours', http, environmentService);
  }

  downloadReport(id: number): Observable<Blob> {
    return this.http.get<Blob>(this.apiUrl + this.resourceUrl + `/${id}/report`, {responseType: 'blob' as 'json'})
  }

  downloadSummaryReport(): Observable<Blob> {
    return this.http.get<Blob>(this.apiUrl + this.resourceUrl + `/report`, {responseType: 'blob' as 'json'})
  }
}
