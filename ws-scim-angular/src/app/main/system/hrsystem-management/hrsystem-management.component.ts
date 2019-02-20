import { SystemColumn } from './../../../model/model';
import { Component, OnInit } from '@angular/core';

import { ScimApiService } from './../../../service/scim-api.service';
import { System , Scheduler , SchedulerHistory} from '../../../model/model';

import { first } from 'rxjs/operators';

@Component({
  selector: 'app-hrsystem-management',
  templateUrl: './hrsystem-management.component.html',
  styleUrls: ['./hrsystem-management.component.css']
})

export class HrsystemManagementComponent implements OnInit {

  
  private systems:System[];
  private selectedSystem:System = new System();
  private schedulers:Scheduler[];
  private selectedScheduler:Scheduler;
  private schedulerHistorys:SchedulerHistory[];

  private systemColumns:SystemColumn[] = [];

  private displayDialog: boolean = false;

  constructor(
    private scimApiService:ScimApiService,
  ) { 
    
    this.displayDialog = false;

    let sys_col1= new SystemColumn();
        sys_col1.columnName    = "ID";
        sys_col1.displayName = "사용자 ID";
        sys_col1.mappingColumn = "userId"
        
    let sys_col2= new SystemColumn();
        sys_col2.columnName = "Name";
        sys_col2.displayName = "사용자 이름";
        sys_col2.mappingColumn = "userName"

    let sys_col3= new SystemColumn();
        sys_col3.columnName = "EmpNo";
        sys_col3.displayName = "사용자 사번";
        sys_col3.mappingColumn = "employeeNumber"
        
    this.systemColumns.push(sys_col1);    
    this.systemColumns.push(sys_col2);    
    this.systemColumns.push(sys_col3);    
  }

  ngOnInit() {
    this.scimApiService.getHrSystems()
    .pipe(first())
    .subscribe( data =>{
      this.systems = data;
      this.selectedSystem = this.systems[0];
      console.log("systems >>>: ", this.systems);
    },error =>{
      //HttpErrorResponse 
      
      console.log("error tyep : " , typeof(error)) ;
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
  runSystemScheduler(event: Event, scheduler: Scheduler) {
    
    this.selectedScheduler = scheduler;
    this.scimApiService.runSystemScheduler(
      this.selectedScheduler.schedulerId)
    .pipe(first())
    .subscribe( data =>{
      console.log("runScheduler : ", data);
      
    },error =>{
        console.log("login-error : ", error);
    });

  }

  onDialogHide(){
    this.displayDialog = false;
  }


}
