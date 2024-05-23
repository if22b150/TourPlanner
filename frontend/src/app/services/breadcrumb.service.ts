import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable} from "rxjs";
import {TourModel} from "../models/tour.model";

@Injectable({
  providedIn: 'root'
})
export class BreadcrumbService {
  _tour: BehaviorSubject<TourModel | null>

  get tour$(): Observable<TourModel | null> {
    return this._tour.asObservable();
  }

  set tour(value: TourModel | null) {
    this._tour.next(value)
  }

  constructor() {
    this._tour = new BehaviorSubject<TourModel | null>(null)
  }
}
