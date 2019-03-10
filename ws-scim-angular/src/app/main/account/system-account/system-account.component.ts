import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';

import { ScimApiService } from './../../../service/scim-api.service';
import { System , Scheduler , SchedulerHistory} from '../../../model/model';

import { first } from 'rxjs/operators';

@Component({
  selector: 'app-system-account',
  templateUrl: './system-account.component.html',
  styleUrls: ['./system-account.component.css']
})
export class SystemAccountComponent implements OnInit {

  
  private systems:System[];
  private selectedSystem:System = new System();
  
  private users:any[] = [];
  private historis:any = [];

  private selectedUser:any = {};

  private schedulers:Scheduler[];
  private selectedScheduler:Scheduler;
  private schedulerHistorys:SchedulerHistory[];
  private displayDialog: boolean = false;

  constructor(
    private scimApiService:ScimApiService,
  ) { 
    this.displayDialog = false;
  }

  ngOnInit() {
    this.scimApiService.getSystems()
    .pipe(first())
    .subscribe( data =>{
      this.systems = data;
      this.selectedSystem = this.systems[0];
      console.log("systems : ", this.systems);
    },error =>{
        console.log("login-error : ", error);
    });

  }


  onSelect(event){
    this.selectedSystem = event.value;
    
    console.log("system account  : ", this.selectedSystem.systemId);

    this.scimApiService.getSystemAccount(this.selectedSystem.systemId)
    .pipe(first())
    .subscribe( data =>{
      console.log("account list : ", data);
      
      this.users = data;

    },error =>{
        console.log("login-error : ", error);
    });

  }

  displayContext(){
    console.log("item >> ",this.selectedSystem);
  }
  
  onRowSelect(user:any){
    console.log("user",user);
    this.scimApiService.getUserLifecycle(user.id)
    .pipe(first())
    .subscribe( data =>{
      console.log("account list : ", data);
      this.historis = data;
    },error =>{
        console.log("login-error : ", error);
    });
  }

  showDetail(event: Event, user:any) {
    console.log("item",user);

    this.scimApiService.getAccountHistory(user.id)
    .pipe(first())
    .subscribe( data =>{
      console.log("account history : ", data);
      this.historis = data;

    },error =>{
        console.log("login-error : ", error);
    });

  }
  runScheduler(event: Event, scheduler: Scheduler) {
    

  }

  onDialogHide(){
    this.displayDialog = false;
  }


}
