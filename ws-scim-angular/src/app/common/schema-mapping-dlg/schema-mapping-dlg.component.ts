import { Component, OnInit, Output, Input , EventEmitter } from '@angular/core';

import {DialogType} from '../../model/dialog-type.enum';

import { ScimApiService } from '../../service/scim-api.service';
import { AlertService } from '../../service/alert.service';

@Component({
  selector: 'app-schema-mapping-dlg',
  templateUrl: './schema-mapping-dlg.component.html',
  styleUrls: ['./schema-mapping-dlg.component.css']
})
export class SchemaMappingDlgComponent implements OnInit {

  private _displayDialog:boolean = false;
  //private _system:any;
  private _attribute:any = {};  
  private _tables:any={};
  private _selectedTable:any={};
  private _columns:any={};
  private _selectedColumn:any={};
  private _dataMapper:any = {};

  private _resultData:any = {};

  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService,) { }

  ngOnInit() {
  }

  @Input()
  set display(display:boolean){
    console.log("set display : ", display)
    this._displayDialog = display;
    this.displayChange.emit(this._displayDialog);

    // if(display == true){
    //   this.scimApiService.getTableList(this._system.systemId)
    //   .subscribe( result =>{
    //     console.log("table list >>>: ", result);
    //     if(result.state === "Success"){
    //       this._tables = result.data;
    //     }else{
    //       console.error(result.message);
    //       this.alertService.fail(result.message);
    //     }
    //   },error =>{
    //     console.log("login-error : ", error);
    //   });
    // }else{
    //   this._dataMapper = {};
    //   this._selectedTable = {};
    //   this._selectedColumn = {};
    // }
  }
  get display(){
    return this._displayDialog;
  }
   
  @Input()
  set columns(columns:any[]){
    this._columns = columns;
  }
  @Input()  
  set attribute(attribute:any){
    if(attribute != undefined){
      this._attribute = JSON.parse(JSON.stringify(attribute))
      this._dataMapper = this._attribute.dataMapper
      if(this._dataMapper === undefined){
        this._dataMapper = {};
      }

      //console.log("system", this._system);
      console.log("dataMapper", this._dataMapper);
    }
  }

  @Output() result: EventEmitter<any> = new EventEmitter();
  @Output() displayChange = new EventEmitter();

  okClose(){
    if(this._dataMapper.className != undefined){
      console.log("datamapper 1 ", this._dataMapper.className);
      this._attribute.dataMapper = this._dataMapper;
    }else{
      console.log("datamapper 2",this._dataMapper.className);
      this._attribute.dataMapper = undefined;
    }
    if(this._selectedColumn != "" ){
      this._attribute.resourceColumn = this._selectedColumn;
    }
    console.log("this._attribute : ", this._attribute);
    this._resultData = {
      result:'OK',
      attribute : this._attribute
    };

    this.onDialogHide();
  }

  cancelClose(){
    this._resultData = {
      result:'CANCEL',
      attribute : undefined
    };

    this.onDialogHide();
  }

  onDialogHide(){    
    this.display = false;
    this.result.emit(this._resultData);    
  }


  onSelectTable(event:any){
    console.log("event",event);
  }

  onSelectColumn(event:any){
    console.log("event",event);
    if(this._selectedTable != undefined){
      this._selectedColumn = event.value;
    }
  }
}
