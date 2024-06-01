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

  exportToCsv(id: number | null = null): Observable<Blob> {
    if(id)
      return this.http.get<Blob>(this.apiUrl + this.resourceUrl + `/${id}/export/csv`, {responseType: 'blob' as 'json'})

    return this.http.get<Blob>(this.apiUrl + this.resourceUrl + `/export/csv`, {responseType: 'blob' as 'json'})
  }

  importCsv(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<any>(this.apiUrl + this.resourceUrl + `/import/csv`, formData)
  }

  share(id: number, email: string): Observable<any> {
    return this.http.post<any>(this.apiUrl + this.resourceUrl + `/${id}/send/${email}`, {})
  }
}
