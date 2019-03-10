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
  private message: any = {type:'',data:{}};
  private display:boolean = false;

  private error_display   :boolean = false;
  private warning_display :boolean = false;
  private alret_display   :boolean = false;

  constructor(private alertService: AlertService) { }

  ngOnInit() {
    this.subscription = this.alertService.getMessage().subscribe(message => {
      if(message == undefined){
        return;
      }
     
      console.log("message ", message);
      if(message.type === "success"){        
        this.display = true;
      }else if(message.type === "fail"){
        this.alret_display = true;
      }else if(message.type === "error"){
        this.error_display = true;
      }else if(message.type === "warning"){
        this.warning_display = true;
      }else {
        this.alret_display = true;
      }

      this.message = message;
    });
  }

  ngOnDestroy() {
  
  }

  confirm(){
    
  }
}
