
import { Component, OnInit } from '@angular/core';

import { ScimApiService } from './../../../service/scim-api.service';
import { System , Scheduler , SchedulerHistory, SystemColumn} from '../../../model/model';

import { first } from 'rxjs/operators';


@Component({
  selector: 'app-system-management',
  templateUrl: './system-management.component.html',
  styleUrls: ['./system-management.component.css']
})
export class SystemManagementComponent implements OnInit {

  private systems:System[];
  private selectedSystem:System = new System();
  private schedulers:Scheduler[];
  private selectedScheduler:Scheduler;
  private schedulerHistorys:SchedulerHistory[];
  private systemColumns:SystemColumn[] = [];

  private displayDialog: boolean = false;
  private displayDetailDialog:boolean = false;

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

      this.onSelect({value:this.selectedSystem});
    },error =>{
        console.log("login-error : ", error);
    });

  }

  onSelect(event){
    this.selectedSystem = event.value;   
    this.scimApiService.getSchedulerBySystemId(this.selectedSystem.systemId)
    .pipe(first())
    .subscribe( data =>{
      this.schedulers = data;
    },error =>{
        console.log("login-error : ", error);
    });

    this.scimApiService.getSystemComlumsBySystemId(this.selectedSystem.systemId)
    .pipe(first())
    .subscribe( data =>{
      console.log("system-columns", data);
      this.systemColumns = data;
    },error =>{
        console.log("login-error : ", error);
    });

    this.displayContext();
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
  showDetailLog(event: Event, history:SchedulerHistory){
    console.log("selected history : ", history);
    this.displayDetailDialog = true;
  }


  runSystemScheduler(event: Event, scheduler: Scheduler) {
    
    this.selectedScheduler = scheduler;
    this.scimApiService.runSystemScheduler(
      this.selectedScheduler.schedulerId)
    .pipe(first())
    .subscribe( data =>{
      console.log("result : ", data.status)
      if(data.status != "200")      {
        console.log("error : " , data.detail)
      }
      
    },error =>{
        console.log("login-error : ", error);
    });

  }


  onDialogHide(){
    this.displayDialog = false;
  }
}

