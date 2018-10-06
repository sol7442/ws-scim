import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders , HttpParams  } from '@angular/common/http';
import { Admin } from '../model/admin';
import { Router } from '@angular/router';

import 'rxjs/add/operator/map'


@Injectable()
export class SCIMService {
    constructor(
        private http: HttpClient,
        private router: Router,

    ) { }
    
    login(admin:Admin){
        console.log("request : " , admin);

        let headers = new HttpHeaders();
        headers = headers.append("Content-Type", 'application/x-www-form-urlencoded;charset=UTF-8');
        headers.append("content", "application/json");

        const params = new HttpParams()
            .set ('puserid', admin.puserid)
            .set('ppasswd', admin.ppasswd);

        this.http.post<any>('/admin/login',params)
            .subscribe(data => {
                this.router.navigate(["home"]);
            },
            error => {
                console.log("Error", error);
            }
        );
    }
    logout(){

    }
}
