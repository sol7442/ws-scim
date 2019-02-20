
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AuthenticationService } from './authentication.service';

import { AlertService } from './alert.service';

@Injectable()
export class HttpErrorInerceptor implements HttpInterceptor {

  constructor(
    private authenticationService: AuthenticationService,
    private alertService:AlertService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => {
      
      this.alertService.error(err.message);

      if (err.status === 401) {
          this.authenticationService.logout();
          location.reload(true);
        }
        const error = err.error.message || err.statusText;
        return throwError(error);
    }));
  }
}
