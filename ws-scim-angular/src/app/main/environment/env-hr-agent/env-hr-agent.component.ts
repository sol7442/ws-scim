import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { System , RepositoryType} from '../../../model/model';


import { ScimApiService } from './../../../service/scim-api.service';
import { AlertService } from './../../../service/alert.service';
import { AuthenticationService } from './../../../service/authentication.service';

@Component({
  selector: 'app-env-hr-agent',
  templateUrl: './env-hr-agent.component.html',
  styleUrls: ['./env-hr-agent.component.css']
})
export class EnvHrAgentComponent implements OnInit {

  private protocols:any[];
  private systems:System[];
  private selectedSystem:System = new System();

  private configFiles:any[];
  private configFile:any;
  private selectedConfig:any;

  private repositoryTables:any[];
  private selectedTable:any;
  private repositoryColumns:any[];

  private schemaData:string;

  private displayDialog:boolean = false;

  private librayUploadUrl:string;
  private configUploadUrl:string;

  constructor( 
    private scimApiService:ScimApiService,
    private alertService:AlertService,
    private authService: AuthenticationService) { }

  ngOnInit() {
    this.scimApiService.getHrSystems()
    .pipe(first())
    .subscribe( data =>{
      this.systems = data;
      console.log("systems >>>: ", this.systems);
      //this.selectedSystem = this.systems[0];

    },error =>{
      console.log("login-error : ", error);
    });
  }

  onSelelectSystem(system:System){
    console.log("selectedSystem : ", system);
    this.selectedSystem = system;
   
    this.librayUploadUrl = "/agent/library/" + this.selectedSystem.systemId;
    this.configUploadUrl = "/agent/config/" + this.selectedSystem.systemId;
    console.log("this.librayUploadUrl : ", this.librayUploadUrl);
    console.log("this.configUploadUrl : ", this.configUploadUrl);
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

  onBeforeLibraryUpload(event){
    console.log("onBeforeLibraryUpload : ", this.librayUploadUrl);

    let token =  this.authService.getToken();
    event.xhr.open('POST', this.librayUploadUrl);
    event.xhr.setRequestHeader('Authorization', token);
  }

  uploadLibray(event:any){
    console.log("selected system : ", this.selectedSystem.systemId);
    console.log("uploadLibray result : ", event);
    //this.librayUploadUrl = "/agent/library/" + this.selectedSystem.systemId;
    //this.selectedSystem.systemName
  }

  onBeforeConfigUpload(event){
    console.log("onBeforeConfigUpload : ", this.configUploadUrl);
    console.log("onBeforeConfigUpload event : ", event);

    console.log("session user >>", sessionStorage.getItem('currentUser'));
    let user = JSON.parse(sessionStorage.getItem('currentUser'));
    console.log("session user >>", user);
    let token ;
    if(user == null){
      token =  this.authService.getToken();
    }else{
      token = user.token;
    }

    //let token =  this.authService.getToken();
    console.log("onBeforeConfigUpload token : ", token);
    event.xhr.open('POST', this.configUploadUrl);
    event.xhr.setRequestHeader('Authorization', '' + token);
  }
  uploadConfig(event:any){
    console.log("selected system : ", this.selectedSystem.systemId);
    console.log("uploadConfig result : ", event);
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
    this.scimApiService.getTableColumnList(this.selectedSystem.systemId,this.selectedTable.id, "isUse" )
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
