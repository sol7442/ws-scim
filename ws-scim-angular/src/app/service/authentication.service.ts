import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { User } from '../model/model';

@Injectable()
export class AuthenticationService {
  constructor(private http: HttpClient) { }
  login(userId:string, password:string){
     let user = new User;

    return this.http.post<any>('/login',JSON.stringify({"id":userId,"pw":password}))
      .pipe(map( result =>{
        if(result.code == "0001"){
          user.id    = userId;
          user.name  = result.data.user.userName;
          user.type  = "";
          user.token = result.data.token;

          localStorage.setItem('currentUser', JSON.stringify(user));
        }
        return user;
      }));
  }
  logout(){
    localStorage.removeItem('currentUser');
  }
}
