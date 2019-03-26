
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AuthenticationService } from './authentication.service';

import { AlertService } from './alert.service';

import { ErrorData } from '../model/model';

@Injectable()
export class HttpErrorInerceptor implements HttpInterceptor {

  constructor(
    private authenticationService: AuthenticationService,
    private alertService:AlertService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => {
      //ErrorData      
      console.log("catch error : ", err);

      let error_data :ErrorData = new ErrorData(); 
      error_data.type = "HTTP ERROR";
      error_data.code = err.status;
      error_data.message = err.message;
      error_data.detail = JSON.stringify(err,undefined, 2);

      console.log("http 1 error : ", err);
      
      this.alertService.error(error_data);
      
      const error = err.error.message || err.statusText;
      return throwError(error);

    }));
  }
}
