import { Injectable } from '@angular/core';
import {BehaviorSubject, finalize, Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {EnvironmentService} from "../app.module";

@Injectable({
  providedIn: 'root'
})
export abstract class ResourceService <M> {
  protected readonly apiUrl: string;

  private _data: BehaviorSubject<M[]>
  private _loading: BehaviorSubject<boolean>

  public get data$(): Observable<M[]> {
    return this._data.asObservable()
  }
  public get loading$(): Observable<boolean> {
    return this._loading.asObservable()
  }

  public set data(value: M[]) {
    this._data.next(value)
    if(this.sessionKey)
      sessionStorage.setItem(this.sessionKey, JSON.stringify(value))
  }
  public set loading(value: boolean) {
    this._loading.next(value)
  }

  protected constructor(protected resourceUrl: string,
                        protected sessionKey: string | null,
                        protected http: HttpClient,
                        protected environmentService: EnvironmentService) {
    this._data = new BehaviorSubject<M[]>(sessionKey ? JSON.parse(sessionStorage.getItem(sessionKey)!) : [])
    this._loading = new BehaviorSubject<boolean>(false)
    this.apiUrl = environmentService.getValue('apiUrl');
  }

  getAll(): void {
    this.loading = true;
    this.http.get<M[]>(this.apiUrl + this.resourceUrl)
      .pipe(
        finalize(() => this.loading = false)
      )
      .subscribe({
        next: (data) => {
          this.data = data
        },
        error: (err) => {
          console.log(err)
        },
      })
  }

  create(resourceData: any): Observable<M> {
    return this.http.post<M>(this.apiUrl + this.resourceUrl, resourceData)
  }

  delete(id: number): Observable<any> {
    return this.http.delete<any>(this.apiUrl + this.resourceUrl + `/${id}`)
  }
}
