import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import{AuthenticationService} from './authentication.service';
import { User } from '../model/model';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
    constructor(private authService: AuthenticationService) { }
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
      
      console.log("session user >>", sessionStorage.getItem('currentUser'));
      let user = JSON.parse(sessionStorage.getItem('currentUser'));///, JSON.stringify(user));
      console.log("session user >>", user);
      let token ;
      if(user == null){
        token =  this.authService.getToken();
      }else{
        token = user.token;
      }
        req = req.clone({
          setHeaders: {
            'Content-Type' : 'application/json; charset=UTF-8',
            'Accept'       : 'application/json',
            'Authorization': token
          },
        });
        console.log("add Authorization", token);
        return next.handle(req);
      }
}
