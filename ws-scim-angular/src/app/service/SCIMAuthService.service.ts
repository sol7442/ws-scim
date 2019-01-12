import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SCIMAuthServiceService {

constructor(
  private http: HttpClient,
  private headers:HttpHeaders
) {
    this.headers.append("Content-Type", 'application/json;charset=UTF-8');
}

login(id:string, pw:string){  
  return this.http.post<any>("/auth/login" ,JSON.stringify({"id":id,"pw":pw}) );
}

}


