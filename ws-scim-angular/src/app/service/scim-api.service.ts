import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders,HttpParams } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable } from "rxjs/Observable";

import { Admin } from '../model/model';

import { SCIMFindRequest } from '../model/scim.model';


@Injectable()
export class ScimApiService {
  constructor(private http: HttpClient) { }
  
  /**************************************************
   * 
   **************************************************/  
  

  getAccountState(){
    let api_url = '/account/im/state';
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));  
  }

  getSysAccountState(systemId:string){
    let api_url = '/account/sys/state/' + systemId;
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));  
  }

  getAccountStatus(){
    let api_url = '/account/im/status';
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));  
  }

  getSysAccountStatus(systemId:string){
    let api_url = '/account/sys/status/'+ systemId;
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));  
  }

  findAccounts(where:string, order:string, startIndex:number, count:number){
    let find_request:SCIMFindRequest = new SCIMFindRequest();
    find_request.where = where;
    find_request.order = order;
    find_request.startIndex = startIndex;
    find_request.count = count;

    let api_url = '/account/im/find';
    return this.http.post<any>(api_url,find_request)
    .pipe(map( result =>{
      return result;
    }));  
  }

  findSystemAccount(systemId:string, where:string, order:string, startIndex:number, count:number){
    let find_request:SCIMFindRequest = new SCIMFindRequest();
    find_request.where = where;
    find_request.order = order;
    find_request.startIndex = startIndex;
    find_request.count = count;

    let api_url = '/account/sys/find/' + systemId;
    return this.http.post<any>(api_url,find_request)
    .pipe(map( result =>{
      return result;
    }));  
  }
  getSysAccountByUserId(userId:string){
    let api_url = '/account/im/usersys/' + userId;
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));  
  }


  // Managered - System 
  getSystems(){
    let api_url = '/system/';
    return this.http.get<any>(api_url)
    .pipe(map( result =>{

      

      return result;
    }));
  }

  getAllSystems(){
    let api_url = '/system/all/';
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

  getAgentLibraryList(id:string){
    let api_url = '/agent/library/' + id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    }));
  }
  patchLibrary(id:string){
    let api_url = '/agent/library/' + id;
    return this.http.patch<any>(api_url,
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
  getTableList(id:string) {
    let api_url = '/agent/repository/table/list/' + id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    })); 
  }
  getTableColumnList(systemId:string, tableId:string){
    let api_url = '/agent/repository/table/column/list/' + systemId + "/" + tableId;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    })); 
  }
  getConfigFileList(id:string){
    let api_url = '/agent/config/list/' + id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    })); 
  }
  getConfigFile(systemId:string, fileName:string){
    let api_url = '/agent/config/file/' + systemId + "/name/" + fileName;

    let header = new HttpHeaders();
    header.set('Content-Type', 'application/json; charset=utf-8');
    header.set('Accept', 'application/octet-stream');

    return this.http.get(api_url,{responseType: 'text'})
    .map(data =>{
      return data;//thefile;
    });
  }

  getLogFileList(id:string){
    let api_url = '/agent/log/list/' + id;
    return this.http.get<any>(api_url,
      {
        headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
      })
    .pipe(map( result =>{
      return result;
    })); 
  }
  getLogFile(systemId:string, fileName:string) {
    let api_url = '/agent/log/file/' + systemId + "/name/" + fileName;

    let header = new HttpHeaders();
    header.set('Content-Type', 'application/json; charset=utf-8');
    header.set('Accept', 'application/octet-stream');

    return this.http.get(api_url,{responseType: 'text'})
    .map(data =>{
      return data;//thefile;
    });

  }
}
