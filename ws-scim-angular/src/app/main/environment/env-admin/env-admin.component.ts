import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { Admin} from '../../../model/model';

import { ScimApiService } from './../../../service/scim-api.service';
import { AlertService } from './../../../service/alert.service';
import {DialogType} from '../../../model/dialog-type.enum';

@Component({
  selector: 'app-env-admin',
  templateUrl: './env-admin.component.html',
  styleUrls: ['./env-admin.component.css']
})
export class EnvAdminComponent implements OnInit {

  private admins:Admin[];
  private selectedAdmin:Admin;

  private adminData:any;


  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService) { }

  ngOnInit() {
    this.scimApiService.getAdminList()
    .pipe(first())
    .subscribe( result =>{
      console.log("result >>>: ", result);
      if(result.state === "Success"){
        this.admins = result.data;
      }else{
        this.alertService.fail(result.message);
      }
    },error =>{
      console.log("login-error : ", error);
    });
  }

  onSelelectAdmin(admin:Admin){
    console.log("selected Admin ",admin);
    this.selectedAdmin = admin;
  } 

  onAdd(){
    this.adminData = {type:DialogType.ADD};
  }
  onRemove(){
    this.adminData = {admin:this.selectedAdmin, type:DialogType.DELETE};
  }
  onEdit(){
    this.adminData = {admin:this.selectedAdmin, type:DialogType.UPDATE};
  }

  onResult(event:any){
    console.log("event : ",event);
    if(event.result == "OK"){
      this.scimApiService.getAdminList()
      .subscribe( result =>{
        console.log("result >>>: ", result);
        if(result.state === "Success"){
          this.admins = result.data;
        }else{
          this.alertService.fail(result.message);
        }
      },error =>{
        console.log("error : ", error);
      });
    }
  }
}
