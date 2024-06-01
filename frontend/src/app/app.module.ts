import {Inject, Injectable, InjectionToken, LOCALE_ID, NgModule, Optional} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {DatePipe, registerLocaleData} from "@angular/common";
import {MdbPopconfirmService} from "mdb-angular-ui-kit/popconfirm";
import {MdbModalService} from "mdb-angular-ui-kit/modal";
import {MdbNotificationService} from "mdb-angular-ui-kit/notification";
import {environment} from "../environments/environment";
import {HttpClientModule} from "@angular/common/http";
import localeDe from '@angular/common/locales/de';
import {BreadcrumbsComponent} from "./components/utils/breadcrumbs/breadcrumbs.component";
import {MdbButtonComponent} from "./components/utils/mdb-button/mdb-button.component";
import {HeadingComponent} from "./components/utils/heading/heading.component";

export const ENVIRONMENT = new InjectionToken<{ [key: string]: any }>('environment')
registerLocaleData(localeDe);

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
    HttpClientModule,
    BreadcrumbsComponent,
    MdbButtonComponent,
    HeadingComponent
  ],
  providers: [
    { provide: ENVIRONMENT, useValue: environment },
    { provide: LOCALE_ID, useValue: 'de' },
    MdbNotificationService,
    MdbModalService,
    MdbPopconfirmService,
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
