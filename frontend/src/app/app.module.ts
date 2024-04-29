import {Inject, Injectable, InjectionToken, NgModule, Optional} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {DatePipe} from "@angular/common";
import {MdbPopconfirmService} from "mdb-angular-ui-kit/popconfirm";
import {MdbModalService} from "mdb-angular-ui-kit/modal";
import {MdbNotificationService} from "mdb-angular-ui-kit/notification";
import {environment} from "../environments/environment";
import {HttpClientModule} from "@angular/common/http";

export const ENVIRONMENT = new InjectionToken<{ [key: string]: any }>('environment')

@Injectable({
  providedIn: 'root',
})
export class EnvironmentService {
  private readonly environment: any

  // We need @Optional to be able start app without providing environment file
  constructor(@Optional() @Inject(ENVIRONMENT) environment: any) {
    this.environment = environment !== null ? environment : {}
  }

  getValue(key: string, defaultValue?: any): any {
    return this.environment[key] || defaultValue
  }
}

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule
  ],
  providers: [
    { provide: ENVIRONMENT, useValue: environment },
    MdbNotificationService,
    MdbModalService,
    MdbPopconfirmService,
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
