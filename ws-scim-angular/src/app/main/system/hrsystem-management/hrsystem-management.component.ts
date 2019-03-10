import { SystemColumn } from './../../../model/model';
import { Component, OnInit } from '@angular/core';

import { ScimApiService } from './../../../service/scim-api.service';
import { AlertService } from './../../../service/alert.service';
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
  private displayDetailDialog :boolean = false;
  private detailAuditLogs:any[] = [];

  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService,
  ) { 
    
    this.displayDialog = false;  
  }

  ngOnInit() {
    this.scimApiService.getHrSystems()
    .pipe(first())
    .subscribe( data =>{
      this.systems = data;
      this.selectedSystem = this.systems[0];
      console.log("systems >>>: ", this.systems);
      
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

  showSchedulerLog(scheduler: Scheduler) {
    console.log("item",scheduler);
    this.selectedScheduler = scheduler;
    this.schedulerHistorys = [];

    this.scimApiService.getSchedulerHistory(scheduler.schedulerId)
    .pipe(first())
    .subscribe( data =>{
      this.schedulerHistorys = data;
      console.log("scheduler_history : ", data);
    },error =>{
        console.log("login-error : ", error);
    });
    
    this.displayDialog = true;

  }
  runSystemScheduler(scheduler: Scheduler) {
    
    this.selectedScheduler = scheduler;
    this.scimApiService.runSystemScheduler(scheduler.schedulerId)
    .pipe(first())
    .subscribe( data =>{
      console.log("result >>: ", data)
      if(data == "InternalServerError"){
        this.alertService.fail("스케줄러 실행 오류");
      }else{
        let message = "성공("+data.successCount+")/실패("+data.failCount+")"
        this.alertService.success(message);
      }

    },error =>{
        console.log("login-error>>: ", error);
    });
  }

  showDetailLog(history:any){
    console.log("selected history : ", history);
    

    this.scimApiService.getSchedulerWorkHistoryByWorkId(history.workId)
    .pipe(first())
    .subscribe( data =>{
      console.log("result : ", data)
      this.displayDetailDialog = true;  

      this.detailAuditLogs = data;

    },error =>{
        console.log("login-error : ", error);
    });
  }

  onDialogHide(){
    this.displayDialog = false;
  }


}
