import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {DatePipe} from "@angular/common";
import {MdbPopconfirmService} from "mdb-angular-ui-kit/popconfirm";
import {MdbModalService} from "mdb-angular-ui-kit/modal";
import {MdbNotificationService} from "mdb-angular-ui-kit/notification";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule
  ],
  providers: [
    MdbNotificationService,
    MdbModalService,
    MdbPopconfirmService,
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
