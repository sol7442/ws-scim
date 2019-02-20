import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders,HttpParams } from '@angular/common/http';
import { map } from 'rxjs/operators';


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
    let api_url = '/scheduler/run/system';
    
    let  httpParams = new HttpParams();
    httpParams.append("schedulerId",schedulerId);    
    return this.http.post<any>(api_url,{'schedulerId': schedulerId})
    .pipe(map( result =>{
      return result;
    }));
  }

  // runScheduler(systemId:string, schedulerId:string){
  //   let api_url = '/api/'+ systemId +'/scheduler/run/' + schedulerId;
  //   return this.http.post<any>(api_url,
  //     {
  //       headers:new HttpHeaders().set('Content-Type', 'application/json; charset=utf-8')
  //     })
  //   .pipe(map( result =>{
  //     return result;
  //   }));
  // }
  // HR - System 
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
}
