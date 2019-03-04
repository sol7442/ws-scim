import { Injectable } from '@angular/core';
import { Router, NavigationStart } from '@angular/router';
import { Observable, Subject } from 'rxjs';

@Injectable()
export class AlertService {

  private subject = new Subject<any>();
  private keepAfterNavigationChange = false;

  constructor(private router: Router) {
    router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
          if (this.keepAfterNavigationChange) {
            this.keepAfterNavigationChange = false;
          } else {
            this.subject.next();
          }
      }
    });
  }

  success(message: string, keepAfterNavigationChange = false) {

    console.log("success", message);

    this.keepAfterNavigationChange = keepAfterNavigationChange;
    this.subject.next({ type: 'success', data: message });
  }
  fail(message: any, keepAfterNavigationChange = false) {
    console.log("fail", message);
    this.keepAfterNavigationChange = keepAfterNavigationChange;
    this.subject.next({ type: 'fail', data: message });
  }

  error(error: any, keepAfterNavigationChange = false) {
    console.log("error", error);
    this.keepAfterNavigationChange = keepAfterNavigationChange;
    this.subject.next({ type: 'error', data: error });
  }

  

  getMessage(): Observable<any> {
    return this.subject.asObservable();
  }
}
