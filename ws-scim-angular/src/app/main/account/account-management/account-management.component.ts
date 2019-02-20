import { Component, OnInit } from '@angular/core';

import { ScimApiService } from './../../../service/scim-api.service';
import { System , Scheduler , SchedulerHistory} from '../../../model/model';

import { first } from 'rxjs/operators';

@Component({
  selector: 'app-account-management',
  templateUrl: './account-management.component.html',
  styleUrls: ['./account-management.component.css']
})
export class AccountManagementComponent implements OnInit {

  private systems:System[];
  private selectedSystem:System = new System();
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

    // this.scimApiService.getSystemScheduler(this.selectedSystem.systemId)
    // .pipe(first())
    // .subscribe( data =>{
    //   console.log("schedulers : ", data);
    //   this.schedulers = data;
    // },error =>{
    //     console.log("login-error : ", error);
    // });


    //this.schedulers = this.systems;
    //this.displayContext();
  }

  displayContext(){
    console.log("item >> ",this.selectedSystem);
  }

  showSchedulerLog(event: Event, scheduler: Scheduler) {
    console.log("item",scheduler);
    this.selectedScheduler = scheduler;
    
    this.schedulerHistorys = [];

    this.scimApiService.getSchedulerHistory(this.selectedScheduler.schedulerId)
    .pipe(first())
    .subscribe( data =>{
      this.schedulerHistorys = data;
      console.log("scheduler_history : ", data);
      
    },error =>{
        console.log("login-error : ", error);
    });
    
    this.displayDialog = true;

  }
  runScheduler(event: Event, scheduler: Scheduler) {
    
    // this.selectedScheduler = scheduler;
    // this.scimApiService.runScheduler(
    //   this.selectedScheduler.sourceSystemId,
    //   this.selectedScheduler.schedulerId)
    // .pipe(first())
    // .subscribe( data =>{
    //   console.log("runScheduler : ", data);
      
    // },error =>{
    //     console.log("login-error : ", error);
    // });

  }

  onDialogHide(){
    this.displayDialog = false;
  }

}
