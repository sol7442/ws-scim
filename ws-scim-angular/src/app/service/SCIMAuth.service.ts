import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SCIMAuthService {

  constructor(
    private http: HttpClient
  ) {
  }
  
  login(id:string, pw:string){  
    let headers:HttpHeaders = new HttpHeaders(); 
    headers.append("Content-Type", 'application/json;charset=UTF-8');
    return this.http.post<any>("/login" ,JSON.stringify({"id":id,"pw":pw}),{headers:headers} );
  }
}
