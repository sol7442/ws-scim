import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders,HttpParams } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Observable } from "rxjs/Observable";

import { Admin, System,Scheduler } from '../model/model';

import { FrontSearchRequest , MapperRequest, FrontRequest } from '../model/scim.model';


@Injectable()
export class ScimApiService {
  constructor(private http: HttpClient) { }
  

  updateSchemaOutputMapper(systemId:string, mapper:any){

    let _request:MapperRequest = new MapperRequest();
    _request.name = mapper.name;
    _request.type = "output";
    _request.mapper = mapper;

    let api_url = '/agent/schema/output/' + systemId;
    return this.http.post<any>(api_url,_request)
    .pipe(map( result =>{
      return result;
    }));  
  }
  getSchemaOutputMapper(systemId:string){
    let api_url = '/agent/schema/output/' + systemId;
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    })); 
  }

  getSchemaInputMapper(systemId:string){
    let api_url = '/agent/schema/input/' + systemId;
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    })); 
  }
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

  findAccounts(search_request:FrontSearchRequest){    
    let api_url = '/account/im/find';
    return this.http.post<any>(api_url,search_request)
    .pipe(map( result =>{
      return result;
    }));  
  }

  findSystemAccount(systemId:string, search_request:FrontSearchRequest){    
    let api_url = '/account/sys/find/' + systemId;
    return this.http.post<any>(api_url,search_request)
    .pipe(map( result =>{
      return result;
    }));  
  }
  
  // findSystemAccount(systemId:string, where:string, order:string, startIndex:number, count:number){
  //   let find_request:FrontSearchRequest = new FrontSearchRequest();
  //   find_request.where = where;
  //   find_request.order = order;
  //   find_request.startIndex = startIndex;
  //   find_request.count = count;

  //   let api_url = '/account/sys/find/' + systemId;
  //   return this.http.post<any>(api_url,find_request)
  //   .pipe(map( result =>{
  //     return result;
  //   }));  
  // }
  getSysAccountByUserId(userId:string){
    let api_url = '/account/im/usersys/' + userId;
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));  
  }


  //********************************************/
  createSystem(system:System){
    let request = new FrontRequest();
    request.method = "PUT";
    request.params = {
      'systemId':system.systemId,
      'systemName':system.systemName,
      'systemDesc':system.systemDesc,
      'systemType':system.systemType,
      'systemUrl':system.systemUrl,
    };

    console.log("system : ", system);
    let api_url = '/system/';
    return this.http.put<any>(api_url, JSON.stringify(request))
    .pipe(map( result =>{
      return result;
    }));
  }
  updateSystem(system:System){
    let request = new FrontRequest();
    request.method = "POST";
    request.params = {
      'systemId':system.systemId,
      'systemName':system.systemName,
      'systemDesc':system.systemDesc,
      'systemType':system.systemType,
      'systemUrl':system.systemUrl,
    };    
    let api_url = '/system/';
    return this.http.post<any>(api_url, JSON.stringify(request))
    .pipe(map( result =>{
      return result;
    }));
  }
  deleteSystem(system:System){
    let api_url = '/system/' +  system.systemId;
    return this.http.delete<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));
  }
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

  /********************************************************* */
  createScheduler(scheduler:Scheduler){
    let request = new FrontRequest();
    request.method = "PUT";
    request.params = {
      'schedulerId':scheduler.schedulerId,
      'schedulerName':scheduler.schedulerName,
      'schedulerType':scheduler.schedulerType,
      'schedulerDesc':scheduler.schedulerDesc,
      'jobClass':scheduler.jobClass,
      'encode':scheduler.encode,
      'triggerType':scheduler.triggerType,
      'dayOfMonth':"'"+scheduler.dayOfMonth+"'",
      'dayOfWeek':"'"+scheduler.dayOfWeek+"'",
      'hourOfDay':"'"+scheduler.hourOfDay+"'",
      'minuteOfHour':"'"+scheduler.minuteOfHour+"'",
      'sourceSystemId':scheduler.sourceSystemId,
      'targetSystemId':scheduler.targetSystemId,
      'executeSystemId':scheduler.executeSystemId,
      'lastExecuteDate':scheduler.lastExecuteDate
    }

    console.log("scheduler",scheduler);

    let api_url = '/scheduler/';
    return this.http.put<any>(api_url, JSON.stringify(request))
    .pipe(map( result =>{
      return result;
    }));
  }

  updateScheduler(scheduler:Scheduler){
    let request = new FrontRequest();
    request.method = "POST";
    request.params = {
      'schedulerId':scheduler.schedulerId,
      'schedulerName':scheduler.schedulerName,
      'schedulerType':scheduler.schedulerType,
      'schedulerDesc':scheduler.schedulerDesc,
      'jobClass':scheduler.jobClass,
      'encode':scheduler.encode,
      'triggerType':scheduler.triggerType,
      'dayOfMonth':scheduler.dayOfMonth,
      'dayOfWeek':scheduler.dayOfWeek,
      'hourOfDay':scheduler.hourOfDay,
      'minuteOfHour':scheduler.minuteOfHour,
      'sourceSystemId':scheduler.sourceSystemId,
      'targetSystemId':scheduler.targetSystemId,
      'executeSystemId':scheduler.executeSystemId,
      'lastExecuteDate':scheduler.lastExecuteDate
    }
    
    

    console.log("scheduler",scheduler);

    let api_url = '/scheduler/';
    return this.http.post<any>(api_url, JSON.stringify(request))
    .pipe(map( result =>{
      return result;
    }));
  }
  deleteScheduler(scheduler:Scheduler){
    let api_url = '/scheduler/' +  scheduler.schedulerId;
    return this.http.delete<any>(api_url)
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
    return null;
    // let api_url = '/system/columns/' + systemId;
    // return this.http.get<any>(api_url)
    // .pipe(map( result =>{
    //   return result;
    // }));
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
    let api_url = '/scheduler/run/' + schedulerId;    
    let  httpParams = new HttpParams();
   //httpParams.append("schedulerId",schedulerId);    
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

  getSystemAccountHistory(systemdId:string, userId:string){
    let api_url = '/account/sys/userhis/' + systemdId + '/' + userId;
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

  deleteAdimn(admin:any){    
    let api_url = '/env/admins/' +  admin.adminId;
    console.log("delete admin request : ",api_url )
    return this.http.delete<any>(api_url)
    .pipe(map( result =>{
      console.log("http result : ", result);
      return result;
    }));
  }
  updateAdimn(admin:any){
    let request = new FrontRequest();
    request.method = "POST";
    request.params = {'admin':admin}

    console.log("update admin request : ",request )

    let api_url = '/env/admins/';
    return this.http.post<any>(api_url, JSON.stringify(request))
    .pipe(map( result =>{
      return result;
    }));
   }
   createAdimn(admin:any){

    let request = new FrontRequest();
    request.method = "PUT";
    request.params = {'admin':admin}

    console.log("create admin request : ",request )

    let api_url = '/env/admins/';
    return this.http.put<any>(api_url, JSON.stringify(request))
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
  getTableColumnList(systemId:string, tableName:string, keyColumn:string){

    let request = new FrontRequest();
    request.method = "PUT";
    request.params = {'systemId':systemId, 'tableName':tableName, 'keyColumn':keyColumn}


    // let api_url = '/system/';
    // return this.http.put<any>(api_url, JSON.stringify(request))
    // .pipe(map( result =>{
    //   return result;
    // }));


    let api_url = '/agent/repository/table/column/list';
    return this.http.post<any>(api_url, JSON.stringify(request))
    .pipe(map( result =>{
      return result;
    })); 
  }

  getScimSchemas(){
    let api_url = '/config/schemas';
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
