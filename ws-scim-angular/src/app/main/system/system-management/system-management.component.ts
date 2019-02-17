
import { Component, OnInit } from '@angular/core';

import { ScimApiService } from './../../../service/scim-api.service';
import { System } from '../../../model/model';

import { first } from 'rxjs/operators';


@Component({
  selector: 'app-system-management',
  templateUrl: './system-management.component.html',
  styleUrls: ['./system-management.component.css']
})
export class SystemManagementComponent implements OnInit {

  private systems:System[];
  private selectedSystem:System;

  constructor(
    private scimApiService:ScimApiService,
  ) { 
        
  }

  ngOnInit() {
    var system_list = this.scimApiService.getSystemAll()
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
    this.displayContext();
  }

  displayContext(){
    console.log("item",this.selectedSystem);
  }
}

