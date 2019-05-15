import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';


import { ScimApiService } from './../../../service/scim-api.service';
import { AlertService } from './../../../service/alert.service';


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
  


  constructor(
    private alertService:AlertService,
    private scimApiService:ScimApiService) {
   }

  ngOnInit() {
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

    this.scimApiService.findAccounts("","",0,5)
    .pipe(first())
    .subscribe( result =>{
      console.log("findUsers : ", result);
      this.accounts   = result.data.Resources;

      console.log("this.accounts : ", this.accounts);

      this.totalCount = result.data.totalResults;
    },error =>{
        console.log("login-error : ", error);
    });
  }

  onPaginate(event:any){
    console.log("event",event)
    this.scimApiService.findAccounts("","",event.page,event.rows)
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

    this.scimApiService.getSysAccountByUserId(account.id)
    .pipe(first())
    .subscribe( result =>{
      console.log("getSysAccountByUserId : ", result);
      this.sysAccounts = result.data;
    },error =>{
        console.log("login-error : ", error);
    });

  }
}
