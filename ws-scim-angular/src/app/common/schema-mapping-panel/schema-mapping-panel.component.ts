import { Component, Input, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { ScimApiService } from '../../service/scim-api.service';
import { AlertService } from '../../service/alert.service';

@Component({
  selector: 'app-schema-mapping-panel',
  templateUrl: './schema-mapping-panel.component.html',
  styleUrls: ['./schema-mapping-panel.component.css'],
  
})
export class SchemaMappingPanelComponent implements OnInit {

  private _system;
  private _mappers:any;
  private _selectedMapper:any;
  private _tableName:string;//  = "SSO_ORG_PERSON" ;


  private _schemaName:string = "user_schema.json";
  private _mapperName:string;
  private _schemaAttributes:any[];
  private _tableColumns:any[];

  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService,
  ) { 
  }

  @Input()
  set system(system:any) {
    console.log('prev value: ', this._system);
    console.log('got name: ', system);
    this._system = system;

    console.log("call : getSchemaOutputMapper ");      
    this.scimApiService.getSchemaOutputMapper(this._system.systemId)
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

  ngOnInit() {
    
  }

  onSelectMapper(mapper:any){
    this._schemaAttributes = [];
    for(var key in mapper.attributes){
      this._schemaAttributes.push(mapper.attributes[key]);
    }

    this._selectedMapper = mapper;
    this._tableName = mapper.table.name;
    console.log("selected mapper : ", mapper);
  }

  loadTable(){
    this.scimApiService.getTableColumnList(this._system.systemId,this._tableName)
    .pipe(first())
    .subscribe( result =>{
      console.log("result >>>: ", result);
      if(result.state === "Success"){
        this._tableColumns = result.data;
      }
    },error =>{
      console.log("error : ", error);
    });
  }
  loadSchemaMapper(){
   
    
  }
}
