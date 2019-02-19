import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { User } from '../model/model';

@Injectable()
export class AuthenticationService {
  
  private toekn:string;

  constructor(private http: HttpClient) {
    this.toekn = "";
  }
  
  login(userId:string, password:string){
    let user = new User;
    
    return this.http.post<any>('/login',JSON.stringify({"id":userId,"pw":password}))
      .pipe(map( result =>{
        if(result.code == "0001"){
          user.id    = userId;
          user.name  = result.data.user.userName;
          user.type  = "";
          user.token = result.data.token;

          this.toekn = result.data.token;
          
          sessionStorage.setItem('currentUser', JSON.stringify(user));
        }
        return user;
      }));
  }
  logout(){
    localStorage.removeItem('currentUser');
  }
  getToken(){
    return this.toekn;
  }
}
