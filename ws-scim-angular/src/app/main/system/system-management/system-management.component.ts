
import { Component, OnInit } from '@angular/core';

import { ScimApiService } from './../../../service/scim-api.service';
import { AlertService } from './../../../service/alert.service';


import { System , Scheduler , SchedulerHistory, SystemColumn} from '../../../model/model';
import {DialogType} from '../../../model/dialog-type.enum';

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
  private _systemData:any;
  
  private displayDialog: boolean = false;
  private displayDetailDialog:boolean = false;
  private detailAuditLogs:any[] = [];

  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService,
  ) { 
    this.displayDialog = false;

  }

  ngOnInit() {
    this.scimApiService.getSystems()
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
    .subscribe( data =>{
      this.schedulers = data;
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

  runSystemScheduler(scheduler: Scheduler) {
    this.selectedScheduler = scheduler;
    console.log("run scheduler :",scheduler );
    
    this.scimApiService.runSystemScheduler(scheduler.schedulerId)
    .pipe(first())
    .subscribe( result =>{
      if(result.state === "Success") {
        let message = "성공("+result.data.successCount+")/실패("+result.data.failCount+")"
        this.alertService.success(message);
      }else{
        this.alertService.fail("스케줄러 실행 오류");
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

  addSystem(){
    this._systemData = {type:DialogType.ADD};
  }

  editSystem(){
    this._systemData = {system:this.selectedSystem,type:DialogType.UPDATE};
  }

  removeSystem(){
    this._systemData = {system:this.selectedSystem,type:DialogType.DELETE};
  }
  onSystemEditResult(event:any){
    console.log("onSystemEditResult ",event)

    this.scimApiService.getSystems()
    .subscribe( data =>{
      console.log("getSystems : ", data);

      this.systems = data;
      this.selectedSystem = this.systems[0];
      console.log("systems : ", this.systems);

      this.onSelect({value:this.selectedSystem});
    },error =>{
        console.log("login-error : ", error);
    });
    
    // this.scimApiService.getHrSystems()
    // .subscribe( data =>{
    //   this.systems = data;
    //   if(event.result === "OK" && event.type === DialogType.DELETE){
    //     this.selectedSystem = data[0];
    //   }else{
    //     this.selectedSystem = event.system;
    //   }
    //   this.onSelect({value:this.selectedSystem});
    // },error =>{
    //   console.log("login-error : ", error);
    // });
  }




  onDialogHide(){
    this.displayDialog = false;
  }
}

