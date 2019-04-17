import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { ScimApiService } from './../../../service/scim-api.service';
import { System , Scheduler , SchedulerHistory} from '../../../model/model';

import { first } from 'rxjs/operators';

@Component({
  selector: 'app-system-account',
  templateUrl: './system-account.component.html',
  styleUrls: ['./system-account.component.css']
})
export class SystemAccountComponent implements OnInit {

  private sub:any;
  private systemId:string;
  
  private accounts:any[];
  private totalCount:number;
  
  private accountStatus;
  private accountState;

  private activeCount:number = 1;
  private inActiveCount:number = 1;

  private integrateCount:number = 1;
  private ghostCount:number = 1;

  constructor(
    private scimApiService:ScimApiService,
    private route: ActivatedRoute
  ) { 
    //this.displayDialog = false;
  }

 

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      this.systemId = params['id'];
      console.log("systemId : ", this.systemId);
      
      this.scimApiService.getSysAccountState(this.systemId)
        .pipe(first())
        .subscribe( result =>{
          console.log("getSysAccountState : ", result);
          
          this.totalCount    = result.data.total;
          this.activeCount   = result.data.active;
          this.inActiveCount = result.data.inactive;
    
          this.accountState = {
            labels: ['활성', '비활성'],
            datasets: [
              {
                  data: [this.activeCount, this.inActiveCount],
                  backgroundColor: [
                    "#36A2EB",
                    "#FF6384",              
                    "#FFCE56"
                  ],
                  hoverBackgroundColor: [
                    "#36A2EB",
                    "#FF6384",              
                    "#FFCE56"
                ]
              }
            ]
          };
      },error =>{
          console.log("login-error : ", error);
      });
      
      this.scimApiService.getSysAccountStatus(this.systemId)
        .pipe(first())
        .subscribe( result =>{
        
          console.log("getSysAccountStatus : ", result);
      
          this.totalCount      = result.data.total;
          this.integrateCount  = result.data.integrate;
          this.ghostCount      = result.data.ghost;
            
          this.accountStatus = {
            labels: ['통합', '고스트'],
            datasets: [
              {
                  data: [this.integrateCount, this.ghostCount],
                  backgroundColor: [
                    "#36A2EB",
                    "#FF6384",              
                    "#FFCE56"
                  ],
                  hoverBackgroundColor: [
                    "#36A2EB",
                    "#FF6384",              
                    "#FFCE56"
                ]
              }
            ]};

            console.log("getSysAccountState - ghostCount: ", this.ghostCount);

      },error =>{
          console.log("login-error : ", error);
      });
      
    this.scimApiService.findSystemAccount(this.systemId,"","",0,5)
      .pipe(first())
      .subscribe( result =>{
        console.log("findSystemAccount : ", result);
        this.accounts   = result.data.Resources;
        this.totalCount = result.data.totalResults;
      },error =>{
          console.log("login-error : ", error);
      });
    });
  }


  ngOnDestroy() {
    console.log("onDestroy : ", this.systemId);
    this.sub.unsubscribe();
  }

  onPaginate(event:any){
    console.log("onPaginate",event)

    this.scimApiService.findSystemAccount(this.systemId,"","",event.page,event.rows)
    .pipe(first())
    .subscribe( result =>{
      console.log("findSystemAccount : ", result);
      this.accounts   = result.data.Resources;
      this.totalCount = result.data.totalResults;
    },error =>{
        console.log("login-error : ", error);
    });
  }

  onSelelectAccount(account){
    console.log("selected account",account)

  }

}
