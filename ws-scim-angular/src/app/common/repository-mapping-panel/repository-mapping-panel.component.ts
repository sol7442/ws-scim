import { Component,Input, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { ScimApiService } from '../../service/scim-api.service';
import { AlertService } from '../../service/alert.service';

@Component({
  selector: 'app-repository-mapping-panel',
  templateUrl: './repository-mapping-panel.component.html',
  styleUrls: ['./repository-mapping-panel.component.css']
})
export class RepositoryMappingPanelComponent implements OnInit {

  private _system;
  private _mappers:any;
  private _selectedMapper:any;
  private _tables:any[];
  private _selectedTable:any;
  private _columns:any[];

  private _tableName:string;
  private _schemaName:string
  private _schemaAttributes:any[];

  private _selectedColumn:any;
  private _showMappingDlg:boolean = false;

  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService,
  ) { 
  }

  ngOnInit() {
  
  }

  @Input()
  set system(system:any) {
    console.log('prev value: ', this._system);
    console.log('got name: ', system);
    this._system = system;

    console.log("call : getSchemaInputMapper ");      
    this.scimApiService.getSchemaInputMapper(this._system.systemId)
    .pipe(first())
    .subscribe( result =>{
      console.log("result : ", result);      
      if(result.state === "Success"){
        this._mappers = result.data;
        console.log("attribute : ", result.data.attributes); 
      }else{
        this._schemaAttributes = null;
      }
    },error =>{
        console.log("error : ", error);
    });
  }

  onSelectMapper(mapper:any){
    this._tables = mapper.tables;
  }
  onSelectTable(table:any){
    this._selectedTable = table;
    console.log("table,", table);

    this._columns = table.columns;
  }

  editMapping(column:any){
    console.log("column : ", column);
    this._selectedColumn = column;
    this._showMappingDlg = true;
  }

  onEditMappingResult(event:any){
    
  }
}
