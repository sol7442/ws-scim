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
  private logData:any;
  private selectedlogFile:any;//string = "testaassssssaa";

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
    .subscribe( result =>{
      console.log("result >>>: ", result);
      if(result.state === "Success"){
        var i=0;
        var len = result.data.length;
        for (i=0; i<len; ++i) {
          this.logFiles.push({label:result.data[i], value:result.data[i]});
        }
      }else{
        this.alertService.fail(result.message);
      }
    },error =>{
      console.log("login-error : ", error);
    });
  }

  onSelectLogFile(){  
    let file_name;
    if(this.selectedlogFile.path === undefined){
      file_name = this.selectedlogFile;
    }else{
      file_name = this.selectedlogFile.path;
    }
    console.log("selected File : ", file_name);
    this.scimApiService.getLogFile(this.selectedSystem.systemId, file_name)
    .subscribe( data =>{
      this.logData = data;
    },error =>{
      console.log("login-error : ", error);
    });
  }
}
