import { Component, OnInit ,Input,Output ,EventEmitter} from '@angular/core';

import {System,Scheduler} from '../../model/model';

import { ScimApiService } from '../../service/scim-api.service';


@Component({
  selector: 'app-scheduler-history-dlg',
  templateUrl: './scheduler-history-dlg.component.html',
  styleUrls: ['./scheduler-history-dlg.component.css']
})
export class SchedulerHistoryDlgComponent implements OnInit {
  private _displayDialog:boolean =false;
  private _scheduler:Scheduler;
  private _historys:any;

  private _displayDetailDialog:boolean = false;
  private _audits:any;

  constructor(private scimApiService:ScimApiService,) { }

  ngOnInit() {
  }

  @Output() showChange = new EventEmitter();
  @Input()  
  set scheduler(data:any){
    this._scheduler = data;
  }
  @Input()  
  set show(display:boolean){
    this._displayDialog = display;
    this.showChange.emit(this._displayDialog ); 
    if(display === true){
      this.scimApiService.getSchedulerHistory(this._scheduler.schedulerId)
      .subscribe( data =>{
        console.log("scheduler_history : ", data);
        this._historys = data;
      },error =>{
          console.log("login-error : ", error);
      });
    }
  }
  get show(){
    return this._displayDialog;
  }

  onDialogHide(){
    this._displayDetailDialog = false;
  }

  showDetailLog(history:any){
    this.scimApiService.getSchedulerWorkHistoryByWorkId(history.workId)
    .subscribe( data =>{
      console.log("result : ", data)
      this._displayDetailDialog = true;  
      this._audits = data;
    },error =>{
        console.log("login-error : ", error);
    });
  }
}
