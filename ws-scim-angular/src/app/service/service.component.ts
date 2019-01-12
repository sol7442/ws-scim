import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders , HttpParams  } from '@angular/common/http';
import { Admin } from '../model/admin';
import 'rxjs/add/operator/map'


@Injectable()
export class SCIMService {
    constructor(
        private http: HttpClient,

    ) { }
    
    post(url:string, admin:Admin){
        console.log("request : " , admin);

        let headers = new HttpHeaders();
        headers = headers.append("Content-Type", 'application/json;charset=UTF-8');
        let login_request = {"id":admin.puserid,"pw":admin.ppasswd};
        
        /*
        const params = new HttpParams()
            .set ('puserid', admin.puserid)
            .set('ppasswd', admin.ppasswd);
        */

        return this.http.post<any>(url ,JSON.stringify(login_request) );
    }
    get(url:string){
        return this.http.get<any>(url);
    }
}
