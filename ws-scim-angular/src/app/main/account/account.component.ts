import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { first } from 'rxjs/operators';

import { ScimApiService } from '../../service/scim-api.service';
import { AlertService } from   '../../service/alert.service';

import { System , Scheduler , SchedulerHistory, SystemColumn} from '../../model/model';


@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  private systems:System[];
  private selectedSystem:System;// = null;//new System();

  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService,
    private router: Router) { }

  ngOnInit() {
    this.scimApiService.getSystems()
    .pipe(first())
    .subscribe( data =>{
      this.systems = data;
      //this.selectedSystem = this.systems[0];
      console.log("systems : ", this.systems);

    },error =>{
        console.log("login-error : ", error);
    });
  }

  onSelectImSystem(){
    console.log("selected system : im..");
    this.selectedSystem = null;
    this.router.navigate(["/main/account/im_account"]);

  }
  onSelectSystem(event:any){
    console.log("selected system : ", event);
    this.selectedSystem = event.value;
    this.router.navigate(["/main/account/sys_account", this.selectedSystem.systemId]);
  }
}
