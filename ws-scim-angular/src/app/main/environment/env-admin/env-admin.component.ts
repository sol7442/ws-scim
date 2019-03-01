import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';

import { Admin} from '../../../model/model';

import { ScimApiService } from './../../../service/scim-api.service';

@Component({
  selector: 'app-env-admin',
  templateUrl: './env-admin.component.html',
  styleUrls: ['./env-admin.component.css']
})
export class EnvAdminComponent implements OnInit {

  private admins:Admin[];


  constructor(private scimApiService:ScimApiService) { }

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
}
