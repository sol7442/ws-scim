import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { Admin} from '../../../model/model';

import { ScimApiService } from './../../../service/scim-api.service';
import { AlertService } from './../../../service/alert.service';

@Component({
  selector: 'app-env-admin',
  templateUrl: './env-admin.component.html',
  styleUrls: ['./env-admin.component.css']
})
export class EnvAdminComponent implements OnInit {

  private admins:Admin[];
  private displayDialog:Boolean = false;
  private newAdmin:Admin = new Admin();

  private newPassword:string;
  private newPasswordC:string;

  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService) { }

  ngOnInit() {
    this.scimApiService.getAdminList()
    .pipe(first())
    .subscribe( data =>{
      console.log("admins >>>: ", data);
      this.admins = data;
    },error =>{
      console.log("login-error : ", error);
    });
  }

  newPasswordCheck(){
    if(this.newAdmin.password == null){
      this.alertService.fail("패스워드가 입력되지 않았습니다.");
      return false;
    }

    if(this.newAdmin.password != this.newPasswordC){
      this.alertService.fail("패스워드 검증 실패");
      return false;
    }

    return true;
  }
  showFindUserDialog(){
    this.newAdmin = new Admin();
    this.displayDialog = true;  
  }
  
  selectClose(){
    if(this.newPasswordCheck()){
      
      this.scimApiService.addAdmin(this.newAdmin)
      .pipe(first())
      .subscribe( data =>{
        console.log("addAdmin-data >>>: ", data);
       
        //alert ///

        this.displayDialog = false; 
      },error =>{
        console.log("addAdmin-error : ", error);
      });

      
    }
  }
  cancelClose(){
    console.log("canceled....")
    this.displayDialog = false;  
    this.newAdmin = new Admin();
  }

  searchUserById(userId:string){
    console.log("searchUserId : ", userId);

    this.scimApiService.searchUserById(userId)
    .pipe(first())
    .subscribe( data =>{
      console.log("searchUser >>>: ", data);

      if(data == null){
        this.alertService.fail("사용자를 찾지 못했습니다.");
      }else{
        this.newAdmin.adminId   = data.id;
        this.newAdmin.adminName = data.userName;
      }
    },error =>{
      console.log("login-error : ", error);
    });
  }

}
