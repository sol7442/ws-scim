import { Component, OnInit, Output, Input , EventEmitter } from '@angular/core';

import {DialogType} from '../../model/dialog-type.enum';
import { Admin} from '../../model/model';
import { ScimApiService } from '../../service/scim-api.service';

@Component({
  selector: 'app-admin-info-dlg',
  templateUrl: './admin-info-dlg.component.html',
  styleUrls: ['./admin-info-dlg.component.css']
})
export class AdminInfoDlgComponent implements OnInit {

  private _displayDialog:boolean =false;
  private _okLabel:string;

  private _adminData:any;
  private _admin:Admin;
  private _type:DialogType;
  private _controlFunc:Function;

  private _resultData:any;
  private _passwordConfim:string;

  constructor(    
    private scimApiService:ScimApiService,
    ) { }

  ngOnInit() {
    this._admin = new Admin();
  }

  @Input()  
  set adminData(data:any){
    console.log("adminData set : ",data)
    if(data !== undefined){
      this._displayDialog = true;
      this._type  = data.type;
      this._admin = data.admin; 

      if(this._type == DialogType.ADD){
        this._admin = new Admin();
        this._okLabel = "추가";
        this._controlFunc = this.scimApiService.createAdimn;
      }else if(this._type == DialogType.UPDATE){
        this._okLabel = "변경";
        this._controlFunc = this.scimApiService.updateAdimn;
      }else if(this._type == DialogType.DELETE){
        this._okLabel = "삭제";
        this._controlFunc = this.scimApiService.deleteAdimn;
      }
    }
  }

  @Output() result: EventEmitter<any> = new EventEmitter();

  okClose(){
    if(this._type == DialogType.ADD){
      this.scimApiService.createAdimn(this._admin)
      .subscribe(result=>{
          this._resultData = {
            result:'OK',
            type  : this._type,
            admin : this._admin
          };
          this.onDialogHide();
      });
    }else if(this._type == DialogType.UPDATE){
      this.scimApiService.updateAdimn(this._admin)
      .subscribe(result=>{
          this._resultData = {
            result:'OK',
            type  : this._type,
            admin : this._admin
          };
          this.onDialogHide();
      });
    }else if(this._type == DialogType.DELETE){
      this.scimApiService.deleteAdimn(this._admin)
      .subscribe(result=>{
          this._resultData = {
            result:'OK',
            type  : this._type,
            admin : this._admin
          };
          this.onDialogHide();
      });
    }

  }
  cancelClose(){
    this._resultData = {
      result:'CANCEL',
      type  : this._type,
      admin : this._admin
    };

    this.onDialogHide();
  }

  onDialogHide(){
    this._displayDialog = false;
    this.result.emit(this._resultData);
  }
}
