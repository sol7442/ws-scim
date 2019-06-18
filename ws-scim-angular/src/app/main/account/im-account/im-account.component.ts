import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';


import { ScimApiService } from './../../../service/scim-api.service';
import { AlertService } from './../../../service/alert.service';

import { FrontSearchRequest } from '../../../model/scim.model';

@Component({
  selector: 'app-im-account',
  templateUrl: './im-account.component.html',
  styleUrls: ['./im-account.component.css']
})
export class ImAccountComponent implements OnInit {
  private accountStatus;
  private accountState;

  private accounts:any[];
  private sysAccounts:any[];
  private selectedAccount:any;
  
  private totalCount:number;
  private activeCount:number;
  private inActiveCount:number;

  private integrateCount:number;
  private isolatateCount:number;
  
  private searchOption:any[]=[];
  private selectedSearchOption:any;
  private searchValue:any;

  constructor(
    private alertService:AlertService,
    private scimApiService:ScimApiService) {
   }

  ngOnInit() {

    
    this.searchOption = [
      {label: '사번', name: 'id'},
      {label: '이름', name: 'name'},
    ]

    this.scimApiService.getAccountState()
    .pipe(first())
    .subscribe( result =>{
      console.log("getAccountState : ", result);
      
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


    this.scimApiService.getAccountStatus()
    .pipe(first())
    .subscribe( result =>{
      console.log("getAccountState : ", result);
      
      this.totalCount      = result.data.total;
      this.integrateCount  = result.data.integrate;
      this.isolatateCount  = result.data.isolatate;

      this.accountStatus = {
        labels: ['통합', '고아'],
        datasets: [
          {
              data: [this.integrateCount, this.isolatateCount],
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

    this.scimApiService.findAccounts(search_request)
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

    this.scimApiService.getSysAccountByUserId(account.attributes.id)
    .pipe(first())
    .subscribe( result =>{
      console.log("getSysAccountByUserId : ", result);
      this.sysAccounts = result.data;
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
    this.scimApiService.findAccounts(search_request)
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
