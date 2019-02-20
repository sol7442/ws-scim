import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

import { AlertService } from '../service/alert.service';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css'],
})
export class AlertComponent implements OnInit {
  private subscription: Subscription;
  private message: any = {};
  private display:boolean = false;

  constructor(private alertService: AlertService) { }

  ngOnInit() {
    this.subscription = this.alertService.getMessage().subscribe(message => {
      if(message != undefined) {
        console.log("message",message);
        this.display = true;
        this.message = message;
      }
    });
  }

  ngOnDestroy() {
  
  }

  confirm(){
    
  }
}
