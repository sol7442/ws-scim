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

  onAdd(){
    this.displayDialog =true;
  }
  onRemove(){

  }
  onEdit(){

  }

}
