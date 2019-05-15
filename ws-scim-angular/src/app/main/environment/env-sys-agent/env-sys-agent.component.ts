import { Component, OnInit } from '@angular/core';

import { first } from 'rxjs/operators';

import { System , RepositoryType} from '../../../model/model';
import { ScimApiService } from './../../../service/scim-api.service';
import { AlertService } from './../../../service/alert.service';
import { AuthenticationService } from './../../../service/authentication.service';

@Component({
  selector: 'app-env-sys-agent',
  templateUrl: './env-sys-agent.component.html',
  styleUrls: ['./env-sys-agent.component.css']
})
export class EnvSysAgentComponent implements OnInit {

  private systems:System[];
  private selectedSystem:System = new System();
  private displayDialog:boolean = false;

  private configFiles:any[];
  private configFile:any;
  private selectedConfig:any;

  private repositoryTables:any[];
  private selectedTable:any;
  private repositoryColumns:any[];

  private schemaData:string;


  constructor(private scimApiService:ScimApiService,
    private alertService:AlertService,
    private authService: AuthenticationService) { }

  ngOnInit() {
    this.scimApiService.getSystems()
    .pipe(first())
    .subscribe( data =>{
      console.log("systems >>>: ", data);
      this.systems = data;
    },error =>{
      console.log("login-error : ", error);
    });
  }

  onSelelectSystem(system:System){
    console.log("selectedSystem : ", system);
    this.selectedSystem = system;
  }

  getConfigList(){
    this.configFiles = [];
    this.scimApiService.getConfigFileList(this.selectedSystem.systemId)
    .pipe(first())
    .subscribe( result =>{
      console.log("result >>>: ", result);
      if(result.state === "Success"){
        var i=0;
        var len = result.data.length;
        for (i=0; i<len; ++i) {
          this.configFiles.push({label:result.data[i], value:result.data[i]});
        }
      }else{
        this.alertService.fail(result.message);
      }
    },error =>{
      console.log("login-error : ", error);
    });
  }

  onSelectConfigFile(){
    let file_name;
    if(this.selectedConfig.path === undefined){
      file_name = this.selectedConfig;
    }else{
      file_name = this.selectedConfig.path;
    }
    console.log("selectedConfig :", file_name);
    
    this.scimApiService.getConfigFile(this.selectedSystem.systemId, file_name)
    .subscribe( data =>{
      this.configFile = data;
    },error =>{
      console.log("login-error : ", error);
    });
  }

  getTableList(){
    this.scimApiService.getTableList(this.selectedSystem.systemId)
    .pipe(first())
    .subscribe( result =>{
      console.log("table list >>>: ", result);
      if(result.state === "Success"){
        this.repositoryTables = result.data;
        this.schemaData = JSON.stringify(result.data,null, 2);
      }else{
        console.error(result.message);
        this.alertService.fail(result.message);
      }
    },error =>{
      console.log("login-error : ", error);
    });
  }
  
  getTableColumnList(){
    
    console.log("selected table>>>: ", this.selectedTable);
    this.scimApiService.getTableColumnList(this.selectedSystem.systemId,this.selectedTable.id )
    .pipe(first())
    .subscribe( data =>{
      console.log("table list >>>: ", data);
      this.schemaData = JSON.stringify(data.data.data,null, 2);
    },error =>{
      console.log("login-error : ", error);
    });
  }
  
  onAdd(){
    this.displayDialog =true;
  }
  onRemove(){

  }
  onEdit(){

  }

}
