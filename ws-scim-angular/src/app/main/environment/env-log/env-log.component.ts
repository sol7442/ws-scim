import { Component, OnInit } from '@angular/core';

import { System} from '../../../model/model';
import { ScimApiService } from './../../../service/scim-api.service';
import { AlertService } from './../../../service/alert.service';

import { first } from 'rxjs/operators';

@Component({
  selector: 'app-env-log',
  templateUrl: './env-log.component.html',
  styleUrls: ['./env-log.component.css']
})
export class EnvLogComponent implements OnInit {
  private systems:System[];
  private selectedSystem:System = new System();
  private logFiles:any[];
  private logFile:any;//string = "testaassssssaa";

  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService,
  ) { }

  ngOnInit() {
    this.scimApiService.getAllSystems()
    .pipe(first())
    .subscribe( data =>{
      this.systems = data;
      this.selectedSystem = this.systems[0];
      console.log("systems >>>: ", this.systems);
    },error =>{
      console.log("login-error : ", error);
    });


  }
  
  onSelectSystem(event:any){
    console.log("selected systemd : ", event);

    this.selectedSystem = event.value;
    this.logFiles = [];

    this.scimApiService.getLogFileList(this.selectedSystem.systemId)
    .pipe(first())
    .subscribe( data =>{
      console.log("files >>>: ", data);
      this.logFiles = data;

    },error =>{
      console.log("login-error : ", error);
    });
  }

  onSelectFile(event:any){
    console.log("selected File : ", event);
    let file = event.value;
    console.log("selected File : ", file.path);

    
    this.scimApiService.getLogFile(this.selectedSystem.systemId, file.path)
    .subscribe( data =>{
      //console.log("files >>>: ", data);
      this.logFile = data;
    },error =>{
      console.log("login-error : ", error);
    });
  }
}
