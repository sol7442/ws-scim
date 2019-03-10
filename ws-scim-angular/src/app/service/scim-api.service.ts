import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders,HttpParams } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { Admin } from '../model/model';


@Injectable()
export class ScimApiService {
  constructor(private http: HttpClient) { }
  
  /**************************************************
   * 
   **************************************************/  

  // Managered - System 
  getSystems(){
    let api_url = '/system/';
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }


  getSystem(id:string){
    let api_url = '/system/' + id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }

  getSchedulerBySystemId(systemId:string){
    let api_url = '/scheduler/system/' + systemId;
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));
  }

  getSystemComlumsBySystemId(systemId:string){
    let api_url = '/system/columns/' + systemId;
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));
  }
  

  getSchedulerHistory(id:string){
    let api_url = '/scheduler/history/' + id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }

  runSystemScheduler(schedulerId:string){
    let api_url = '/scheduler/run';    
    let  httpParams = new HttpParams();
    httpParams.append("schedulerId",schedulerId);    
    return this.http.post<any>(api_url,{'schedulerId': schedulerId})
    .pipe(map( result =>{
      return result;
    }));
  }

  getHrSystems(){
    let api_url = '/hrsystem/';
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));
  }

  getHrSystem(id:string){
    let api_url = '/hrsystem/'+id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }

  getHrSystemScheduler(id:string){
    let api_url = '/hrsystem/' + id + "/scheduler";
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }

  getSystemAccount(id:string){
    let api_url = '/account/system/' + id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }

  getAccountHistory(id:string){
    let api_url = '/account/history/' + id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }

  getWorkHistoryBySystemId(id:string){    
    let api_url = '/scheduler/history/system/' + id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }

  getAdminList(){    
    let api_url = '/env/admins/';
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));
  }

  addAdmin(admin:Admin){    
    let api_url = '/env/admins/';
    return this.http.put<any>(api_url, JSON.stringify(admin))
    .pipe(map( result =>{
      return result;
    }));
  }

  searchUserById(userId:string){
    let api_url = '/scim/v2.0/Users/' + userId;
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));
  }

  getSchedulerWorkHistoryByWorkId(id:string){
    let api_url = '/scheduler/history/work/' + id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }

  getAgentRepositoryBySystemId(id:string){
    let api_url = '/agent/repository/' + id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }

  setAgentRepositoryBySystemId(id:string, repository:any){
    let api_url = '/agent/repository/' + id;
    return this.http.post<any>(api_url,JSON.stringify(repository),
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }

  getUserLifecycle(id:string){
    let api_url = '/audit/user/' + id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }
}
