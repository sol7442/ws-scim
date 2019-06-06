import { Component, OnInit, Output, Input , EventEmitter } from '@angular/core';

import {DialogType} from '../../model/dialog-type.enum';

import { ScimApiService } from '../../service/scim-api.service';
import { AlertService } from '../../service/alert.service';

@Component({
  selector: 'app-repository-mapping-dlg',
  templateUrl: './repository-mapping-dlg.component.html',
  styleUrls: ['./repository-mapping-dlg.component.css']
})
export class RepositoryMappingDlgComponent implements OnInit {

  private _displayDialog:boolean = false;
  private _system:any;
  private _column:any = {'attributes':{}};

  private _schemas:any = [];
  private _attributes:any;
  private _selectedAttribute:any = {};

  private _dataMapper:any = {};

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
    
    if(display === true){
      this.scimApiService.getScimSchemas()
      .subscribe( result =>{
        console.log("schema >>>: ", result);
        this._schemas = [];
        for(var key in result.data) {
          console.log("schema 1 >>>: ", key);
          console.log("schema 2 >>>: ", result.data[key]);
          this._schemas.push({label:key, value:result.data[key]});
        }

        console.log("schema 3 >>>: ", this._schemas);
      },error =>{
        console.log("login-error : ", error);
      });
    }
  }
  get display(){
    return this._displayDialog;
  }
   
  @Input()
  set system(system:any){
    this._system = system;
  }
  @Input()  
  set column(column:any){  
    if(column != undefined){
      this._column = JSON.parse(JSON.stringify(column))

      this._dataMapper = this._column.dataMapper
      if(this._dataMapper === undefined){
        this._dataMapper = {};
      }

      this._selectedAttribute = this._column.attributeSchema;
      if(this._selectedAttribute === undefined){
        this._selectedAttribute = {};
      }

      if(this._column.defaultValue === undefined){
        this._column.defaultValue = "";
      }
    }  
  }

  @Output() result: EventEmitter<any> = new EventEmitter();
  @Output() displayChange = new EventEmitter();


  onSelectSchema(event:any){
    console.log("selected schema : ", event);
    this._attributes = [];    
    for(var key in event.value.value.attributes) {      
      this._attributes.push({label:key, value:event.value.value.attributes[key]});
    }
  }
  
  onSelectAttribute(event:any){
    this._selectedAttribute = event.value.value;
  }
  okClose(){
    this.onDialogHide();
  }

  cancelClose(){
    
    this.onDialogHide();
  }

  onDialogHide(){    
    this.display = false;
    //this.result.emit(this._resultData);    
  }
}
