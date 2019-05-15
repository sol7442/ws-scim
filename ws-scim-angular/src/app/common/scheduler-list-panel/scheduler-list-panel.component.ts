import { Component, Input, OnInit } from '@angular/core';

import { ScimApiService } from '../../service/scim-api.service';
import { AlertService } from '../../service/alert.service';

import {DialogType} from '../../model/dialog-type.enum';

@Component({
  selector: 'app-scheduler-list-panel',
  templateUrl: './scheduler-list-panel.component.html',
  styleUrls: ['./scheduler-list-panel.component.css']
})
export class SchedulerListPanelComponent implements OnInit {
  private _system;
  private _schedulers:any;
  private _selectedScheduler:any;
  private _schedulerData:any;

  private _showHistory:boolean = false;

  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService,) { }

  ngOnInit() {
  }

  @Input()
  set system(system:any) {
    console.log('prev value: ', this._system);
    console.log('got name: ', system);
    this._system = system;
    this._selectedScheduler = undefined;

    this.scimApiService.getSchedulerBySystemId(this._system.systemId)
    .subscribe( data =>{
      console.log("_schedulers", data)
      this._schedulers = data;
    },error =>{
        console.log("login-error : ", error);
    });
    
  }

  onSelelectSchduler(scheduler){
    this._selectedScheduler = scheduler;
  }

  addScheduler(){
    this._schedulerData = {type:DialogType.ADD};

  }
  editScheduler(){
    this._schedulerData = {scheduler:this._selectedScheduler,type:DialogType.UPDATE};

  }
  removeScheduler(){
    this._schedulerData = {scheduler:this._selectedScheduler,type:DialogType.DELETE};

  }
  onSchedulerEditResult(event){    
    this.scimApiService.getSchedulerBySystemId(this._system.systemId)
    .subscribe( data =>{
      console.log("_schedulers", data)
      this._schedulers = data;
    },error =>{
        console.log("login-error : ", error);
    });
  }

  
  showSchedulerLog() {
    this._showHistory = true;
  }
  runSystemScheduler() {  
    this.scimApiService.runSystemScheduler(this._selectedScheduler.schedulerId)
    .subscribe( result =>{
      console.log("result >>: ", result)
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
    

    // this.scimApiService.getSchedulerWorkHistoryByWorkId(history.workId)
    // .pipe(first())
    // .subscribe( data =>{
    //   console.log("result : ", data)
    //   this.displayDetailDialog = true;  

    //   this.detailAuditLogs = data;

    // },error =>{
    //     console.log("login-error : ", error);
    // });
  }
}
