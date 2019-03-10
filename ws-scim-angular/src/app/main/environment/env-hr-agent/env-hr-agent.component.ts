import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { System , RepositoryType} from '../../../model/model';


import { ScimApiService } from './../../../service/scim-api.service';
import { AlertService } from './../../../service/alert.service';

@Component({
  selector: 'app-env-hr-agent',
  templateUrl: './env-hr-agent.component.html',
  styleUrls: ['./env-hr-agent.component.css']
})
export class EnvHrAgentComponent implements OnInit {

  private protocols:any[];
  private systems:System[];
  private selectedSystem:System = new System();

  private repositorys:RepositoryType[];// = ["Oracle","MsSql","MySql"];
  private selectcedRepository;//:String;

  private jdbcUrl:string;
  private repository:any = null;

  constructor( 
    private scimApiService:ScimApiService,
    private alertService:AlertService,) { }

  ngOnInit() {
    this.protocols = [
      {label:"Select Protocol"},
      {label:"http"},
      {label:"https"},];
    
    this.repositorys = [
      {label:'Select Repository', value:null},
      {label:'Oracle', value:'Oracle'},
      {label:'MsSql', value:'MsSql'},
      {label:'MySql', value:'MySql'},
    ] ,
   

    this.scimApiService.getHrSystems()
    .pipe(first())
    .subscribe( data =>{
      this.systems = data;
      console.log("systems >>>: ", this.systems);
    },error =>{
      console.log("login-error : ", error);
    });
  }

  onRowSelect(system:System){
    console.log("selected system >>>>:");
    console.log("selected system : ", event);
    console.log("selectedSystem : ", system);
    this.selectedSystem = system;
   
    this.scimApiService.getAgentRepositoryBySystemId(system.systemId)
    .pipe(first())
    .subscribe( data =>{
      console.log("repository >>>: ", data);
      this.repository = data;
      this.jdbcUrl = data.dbcp.jdbcUrl;

    },error =>{
      console.log("login-error : ", error);
    });
  }

  saveRepository(){
    console.log("save repository : ", this.jdbcUrl);
    this.repository.dbcp.jdbcUrl = this.jdbcUrl;
    console.log("save repository : ", this.repository);

    this.scimApiService.setAgentRepositoryBySystemId(
      this.selectedSystem.systemId, this.repository)
    .pipe(first())
    .subscribe( data =>{
      console.log("repository >>>: ", data);
     

    },error =>{
      console.log("login-error : ", error);
    });
  }

}
