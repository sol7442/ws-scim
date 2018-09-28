import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders , HttpParams  } from '@angular/common/http';
import { Admin } from '../model/admin';
import 'rxjs/add/operator/map'


@Injectable()
export class SCIMService {
    constructor(private http: HttpClient) { }
    
    login(admin:Admin){
        console.log("request : " , admin);

        let headers = new HttpHeaders();
        headers = headers.append("Content-Type", 'application/x-www-form-urlencoded;charset=UTF-8');

        const params = new HttpParams()
            .set('puserid', admin.puserid)
            .set('ppasswd', admin.ppasswd);
        

        this.http.post<any>('/admin/login',params, {headers: headers})
            .subscribe(data => {
                console.log("POST Request is successful ", data);
            },
            error => {
                console.log("Error", error);
            }
        );
    }
    logout(){

    }
}
