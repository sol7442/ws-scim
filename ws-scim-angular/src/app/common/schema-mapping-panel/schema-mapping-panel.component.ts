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

  private _selectedAttribute:any;
  private _showMappingDlg:boolean = false;

  private _tables:any[];
  private _keyColumn:string;

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
    
    if(this._system.systemId != undefined){
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
  
      this.scimApiService.getTableList(this._system.systemId)
        .subscribe( result =>{
          console.log("table list >>>: ", result);
          if(result.state === "Success"){
            this._tables = result.data;
          }else{
            console.error(result.message);
            this.alertService.fail(result.message);
          }
        },error =>{
          console.log("login-error : ", error);
        });
    }
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
    this._keyColumn = mapper.table.keyColumn;
    console.log("selected mapper : ", mapper);
  }

  editMapping(attribute:any){
    console.log("attribute : ", attribute);
    this._selectedAttribute = attribute;
    console.log("this._showMappingDlg : ", this._showMappingDlg);
    this._showMappingDlg = true;
  }

  onEditMappingResult(event:any){
    console.log("result : ", event);
    if(event.result ==="OK"){      
      this._selectedMapper.attributes[event.attribute.name] = event.attribute;
      this.onSelectMapper(this._selectedMapper);
      console.log(this._mappers);
       
      this.scimApiService.updateSchemaOutputMapper(this._system.systemId,this._selectedMapper)
      .subscribe(result =>{
        console.log("result : ", result);
      });
    }
  }
  onSelectTable(event:any){
    console.log("select table : ", event);
    this._tableName = event.name;
    this._keyColumn = ""; 
  }
  changeTable(){
    console.log("table : ", this._tableName);
    console.log("table : ", this._keyColumn);

    this.scimApiService.getTableColumnList(this._system.systemId,this._tableName,this._keyColumn)
    .subscribe( result =>{
      console.log("column list >>>: ", result);
      this._tableColumns = result.data;  
    },error =>{
      console.log("login-error : ", error);
    });


    // this._selectedMapper = mapper;
    // this._tableName = mapper.table.name;
    // this._keyColumn = mapper.table.keyColumn;
    // console.log("selected mapper : ", mapper);

    // this._schemaAttributes.resourceColumn = [];
  }
  saveMapper(){

  }
}
