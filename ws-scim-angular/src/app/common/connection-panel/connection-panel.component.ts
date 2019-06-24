import { Component,Input, OnInit } from '@angular/core';


import { ScimApiService } from '../../service/scim-api.service';
import { AlertService } from '../../service/alert.service';

@Component({
  selector: 'app-connection-panel',
  templateUrl: './connection-panel.component.html',
  styleUrls: ['./connection-panel.component.css']
})
export class ConnectionPanelComponent implements OnInit {

  private _query:string = "";
  private _result:string = "";
  private _system:any;

  constructor(
    private scimApiService:ScimApiService,
    private alertService:AlertService,
  ) { 
  }

  ngOnInit() {
  }
  @Input()
  set system(system:any) {
    console.log('prev value: ', this._system);
    console.log('got name: ', system);
    
    this._system = system;

    console.log('prev value: ', this._system);
  }

  executeQuery(){
    console.log("query : ", this._query);

      this.scimApiService.executeQuery(this._system.systemId,this._query)
      .subscribe( result =>{
        console.log("result : ", result);      
        this._result = JSON.stringify(result.data,null, 2);
      },error =>{
          console.log("error : ", error);
      });
    
  }
}
