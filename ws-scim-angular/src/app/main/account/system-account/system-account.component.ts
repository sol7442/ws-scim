import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { ScimApiService } from './../../../service/scim-api.service';
import { System , Scheduler , SchedulerHistory} from '../../../model/model';
import { FrontSearchRequest } from '../../../model/scim.model';

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

  private accountHistory:any[];

  private searchOption:any[]=[];
  private selectedSearchOption:any;
  private searchValue:any;

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
      
      this.searchOption = [
        {label: '사번', name: 'id'},
        {label: '이름', name: 'name'},
      ]


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
    });
  }


  ngOnDestroy() {
    console.log("onDestroy : ", this.systemId);
    this.sub.unsubscribe();
  }

  onPaginate(event:any){
    console.log("event",event)
    console.log("selectedSearchOption",this.selectedSearchOption)
    console.log("this.searchValue",this.searchValue)

    let search_request:FrontSearchRequest = new FrontSearchRequest();
    if(this.selectedSearchOption != undefined){
      if(this.searchValue != ""){
        this.selectedSearchOption["value"] = this.searchValue;
        search_request.attributes = [this.selectedSearchOption]
      }
    }
    
    search_request.startIndex = event.first / event.rows;
    search_request.count      = event.rows;

    console.log("search_request : ", search_request);

    this.scimApiService.findSystemAccount(this.systemId,search_request)
    .pipe(first())
    .subscribe( result =>{
      console.log("findUsers : ", result);
      this.accounts   = result.data.Resources;
      this.totalCount = result.data.totalResults;
    },error =>{
        console.log("login-error : ", error);
    });
  }

  onSelelectAccount(account:any){
    console.log("selected account",account)

    this.scimApiService.getSystemAccountHistory(this.systemId , account.attributes.id)
    .pipe(first())
    .subscribe( result =>{
      console.log("history : ", result);
      if(result.state === "Success"){
        this.accountHistory = result.data;

        console.log("history : ", this.accountHistory);

      }else{
        console.log("history : ", result.message);
      }
    },error =>{
        console.log("login-error : ", error);
    });
  }

  onSearch(){    
    console.log("selectedSearchOption",this.selectedSearchOption)
    console.log("this.searchValue",this.searchValue)

    let search_request:FrontSearchRequest = new FrontSearchRequest();
    if(this.selectedSearchOption != undefined){
      if(this.searchValue != ""){
        this.selectedSearchOption["value"] = this.searchValue;
        search_request.attributes = [this.selectedSearchOption]
      }
    }

    search_request.where      = "eq";    
    search_request.startIndex = 0;
    search_request.count      = 5;

    console.log("search_request : ", search_request);
    this.scimApiService.findSystemAccount(this.systemId,search_request)
    .pipe(first())
    .subscribe( result =>{
      console.log("findUsers : ", result);
      this.accounts   = result.data.Resources;
      this.totalCount = result.data.totalResults;
    },error =>{
        console.log("login-error : ", error);
    });
  }

}
