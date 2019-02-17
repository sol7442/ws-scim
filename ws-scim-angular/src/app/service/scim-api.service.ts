import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';


@Injectable()
export class ScimApiService {
  constructor(private http: HttpClient) { }
  
  /**************************************************
   * 
   **************************************************/  
  getSystemAll(){
    let api_url = '/system/';
    return this.http.get<any>(api_url)
    .pipe(map( result =>{
      return result;
    }));
  }

  getSystem(id:string){
    return "system"
  }
}
