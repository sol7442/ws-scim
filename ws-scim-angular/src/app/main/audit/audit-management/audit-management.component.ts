import { Component, OnInit } from '@angular/core';

import { ScimApiService } from './../../../service/scim-api.service';
import { System , Scheduler , SchedulerHistory} from '../../../model/model';

import { first } from 'rxjs/operators';

@Component({
  selector: 'app-audit-management',
  templateUrl: './audit-management.component.html',
  styleUrls: ['./audit-management.component.css']
})
export class AuditManagementComponent implements OnInit {

  
  private systems:System[];
  private selectedSystem:System = new System();
  
  private workHistory:any[] = [];


  constructor(
    private scimApiService:ScimApiService,
  ) { 

  }

  ngOnInit() {
    this.scimApiService.getSystems()
    .pipe(first())
    .subscribe( data =>{
      this.systems = data;
      this.selectedSystem = this.systems[0];
      console.log("systems : ", this.systems);
    },error =>{
        console.log("login-error : ", error);
    });
  }

  onSelect(event){
    this.selectedSystem = event.value;   

    this.selectedSystem.systemId;
    let sys_id = "sys-scim-hr";

    this.scimApiService.getWorkHistoryBySystemId(sys_id)
    .pipe(first())
    .subscribe( data =>{
      console.log("data-error : ", data);
      this.workHistory = data;
    },error =>{
        console.log("login-error : ", error);
    });

  }
}
