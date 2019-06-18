import { Component, OnInit, Output, Input , EventEmitter } from '@angular/core';

import {DialogType} from '../../model/dialog-type.enum';
import {System} from '../../model/model';
import { ScimApiService } from '../../service/scim-api.service';

@Component({
  selector: 'app-system-info-dlg',
  templateUrl: './system-info-dlg.component.html',
  styleUrls: ['./system-info-dlg.component.css']
})
export class SystemInfoDlgComponent implements OnInit {

  private _displayDialog:boolean =false;
  private _okLabel:string;

  private _systemData:any = {};
  private _system:System = new System();
  private _type:DialogType;

  private _resultData:any;

  constructor(private scimApiService:ScimApiService,) { }

  ngOnInit() {
  }

  @Input()  
  set systemData(data:any){
    console.log("systemData : ",data)
    if(data !== undefined){      
      this._type  = data.type;
      this._displayDialog = true;

      if(this._type == DialogType.ADD){
        this._system = new System();
        this._okLabel = "추가";
      }else if(this._type == DialogType.UPDATE){
        this._system = data.system; 
        this._okLabel = "변경";
      }else if(this._type == DialogType.DELETE){
        this._system = data.system; 
        this._okLabel = "삭제";
      }
    }
  }

  @Output() result: EventEmitter<any> = new EventEmitter();


  okClose(){
    if(this._type == DialogType.ADD){
      this.scimApiService.createSystem(this._system)
      .subscribe(result=>{
          this._resultData = {
            result:'OK',
            type  : this._type,
            system : this._system
          };
          this.onDialogHide();
      });
    }else if(this._type == DialogType.UPDATE){
      this.scimApiService.updateSystem(this._system)
      .subscribe(result=>{
          this._resultData = {
            result:'OK',
            type  : this._type,
            system : this._system
          };
          this.onDialogHide();
      });
    }else if(this._type == DialogType.DELETE){
      this.scimApiService.deleteSystem(this._system)
      .subscribe(result=>{
          this._resultData = {
            result:'OK',
            type  : this._type,
            system : this._system
          };
          this.onDialogHide();
      });
    }

  }
  cancelClose(){
    this._resultData = {
      result:'CANCEL',
      type  : this._type,
      system : this._system
    };

    this.onDialogHide();
  }
  onDialogHide(){
    this._displayDialog = false;
    this.result.emit(this._resultData);
  }
}
