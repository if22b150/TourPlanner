import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {EnvironmentService} from "../app.module";
import {Observable} from "rxjs";
import {TourLogModel} from "../models/tour-log.model";

@Injectable({
  providedIn: 'root'
})
export class TourLogService {
  private readonly apiUrl: string;

  constructor(private http: HttpClient,
              private environmentService: EnvironmentService) {
    this.apiUrl = environmentService.getValue('apiUrl');
  }

  getAll(tourId: number): Observable<TourLogModel[]> {
    return this.http.get<TourLogModel[]>(this.apiUrl + `tours/${tourId}/tour-logs`)
  }

  update(tourId: number, tourLogId: number, resourceData: any): Observable<TourLogModel> {
    return this.http.put<TourLogModel>(this.apiUrl + `tours/${tourId}/tour-logs/${tourLogId}`, resourceData)
  }

  create(tourId: number, resourceData: any): Observable<TourLogModel> {
    return this.http.post<TourLogModel>(this.apiUrl + `tours/${tourId}/tour-logs`, resourceData)
  }
  
  delete(tourId: number, tourLogId: number): Observable<any> {
    return this.http.delete<any>(this.apiUrl + `tours/${tourId}/tour-logs/${tourLogId}`)
  }
}
