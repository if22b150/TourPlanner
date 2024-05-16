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

  // update(customerId: number, id: number, resourceData: any): Observable<MachineModel> {
  //   return this.http.put<MachineModel>(this.apiUrl + `customers/${customerId}/machines/${id}`, resourceData)
  // }
  //
  // create(customerId: number, resourceData: any): Observable<MachineModel> {
  //   return this.http.post<MachineModel>(this.apiUrl + `customers/${customerId}/machines`, resourceData)
  // }
  //
  // delete(customerId: number, id: number): Observable<any> {
  //   return this.http.delete<any>(this.apiUrl + `customers/${customerId}/machines/${id}`)
  // }
}
