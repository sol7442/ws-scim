import { Component, OnInit, Output, Input , EventEmitter } from '@angular/core';

import {DialogType} from '../../model/dialog-type.enum';
import {System,Scheduler} from '../../model/model';
import { ScimApiService } from '../../service/scim-api.service';
import { AlertService } from '../../service/alert.service';

@Component({
  selector: 'app-scheduler-info-dlg',
  templateUrl: './scheduler-info-dlg.component.html',
  styleUrls: ['./scheduler-info-dlg.component.css']
})
export class SchedulerInfoDlgComponent implements OnInit {
  private _displayDialog:boolean =false;
  private _okLabel:string;

  private _schedulerData:any = {};
  private _scheduler:Scheduler = new Scheduler();
  private _type:DialogType;

  private _resultData:any;

  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService,) { }

  ngOnInit() {
  }

  @Input()  
  set schedulerData(data:any){
    console.log("schedulerData : ",data)
    if(data !== undefined){      
      this._type  = data.type;
      this._displayDialog = true;

      if(this._type == DialogType.ADD){
        this._scheduler = new Scheduler();
        this._okLabel = "추가";
      }else if(this._type == DialogType.UPDATE){
        this._scheduler = data.scheduler; 
        this._okLabel = "변경";
      }else if(this._type == DialogType.DELETE){
        this._scheduler = data.scheduler; 
        this._okLabel = "삭제";
      }
    }
  }
  @Output() result: EventEmitter<any> = new EventEmitter();

  okClose(){
    if(this._type == DialogType.ADD){
      this.scimApiService.createScheduler(this._scheduler)
      .subscribe(result=>{
          this._resultData = {
            result:'OK',
            type  : this._type,
            system : this._scheduler
          };
          this.onDialogHide();
      });
    }else if(this._type == DialogType.UPDATE){
      this.scimApiService.updateScheduler(this._scheduler)
      .subscribe(result=>{
          this._resultData = {
            result:'OK',
            type  : this._type,
            system : this._scheduler
          };
          this.onDialogHide();
      });
    }else if(this._type == DialogType.DELETE){
      this.scimApiService.deleteScheduler(this._scheduler)
      .subscribe(result=>{
          this._resultData = {
            result:'OK',
            type  : this._type,
            system : this._scheduler
          };
          this.onDialogHide();
      });
    }

  }
  cancelClose(){
    this._resultData = {
      result:'CANCEL',
      type  : this._type,
      admin : this._scheduler
    };

    this.onDialogHide();
  }
  onDialogHide(){
    this._displayDialog = false;
    this.result.emit(this._resultData);
  }
}
