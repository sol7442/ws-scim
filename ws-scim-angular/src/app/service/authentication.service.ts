import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { AlertService } from './alert.service';
import { User ,ErrorData} from '../model/model';

@Injectable()
export class AuthenticationService {
  
  private toekn:string;

  constructor(
    private http: HttpClient,
    private alertService:AlertService ) {
    this.toekn = "";
  }
  
  login(userId:string, password:string){
    return this.http.post<any>('/login',JSON.stringify({"id":userId,"pw":password}))
      .pipe(map( result =>{
        console.log("login result : ", result);
       
        let user:User = new User();

        user.id    = userId;
        user.name  = result.user.userName;
        user.type  = result.user.type;
        user.token = result.token;

        this.toekn = user.token;
        sessionStorage.setItem('currentUser', JSON.stringify(user));

        return user;

        // }else{
        //   let error_data :ErrorData = new ErrorData(); 
        //   error_data.type = "HTTP ERROR";
        //   error_data.code = result.code;
        //   error_data.message = result.message;
        //   error_data.detail = JSON.stringify(result.data,undefined, 2);
        
        //   this.alertService.error(error_data);

        //   return null;
        // }
        
        //return result;
      }));
  }
  logout(){
    sessionStorage.removeItem('currentUser');
  }
  getToken(){
    return this.toekn;
  }
}
