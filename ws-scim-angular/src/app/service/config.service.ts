import { Injectable, APP_INITIALIZER } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment'; //path to your environment files

@Injectable()
export class ConfigService {
  private config:Object;
  private env:string;

  constructor(private http: HttpClient) { }

  load(){
    return new Promise((resolve, reject) => {
      
      //this.env = environment.production;
      console.log("env : ", environment.production);
      
      this.http.get('./assets/config/office_dev_config.json')
      .map( res => res)
      .subscribe((data) =>{
        this.config = data;
        resolve(true);
      },
      (error:any) => {
        console.error(error);
        return Observable.throw(error.json().error || 'Server error');
      });
    });
  }
  get(key:string){
    return this.config[key];
  }
}

export function ConfigFactory(config: ConfigService) {
  return () => config.load();
}

export function init() {
  return {
      provide: APP_INITIALIZER,
      useFactory: ConfigFactory,
      deps: [ConfigService],
      multi: true
  }
}

export const ConfigModule = {
  init: init
}
