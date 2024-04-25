import { Injectable } from '@angular/core';
import {MdbNotificationService} from "mdb-angular-ui-kit/notification";
import {ToastComponent} from "../components/utils/toast/toast.component";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  notify(message: string, type: string = 'success', autohide: boolean = true, delay: number = 2500): void {
    this.notificationService.open(ToastComponent, { data: { text: message, type: type }, autohide: autohide, delay: delay })
  }

  constructor(private notificationService: MdbNotificationService) { }
}
