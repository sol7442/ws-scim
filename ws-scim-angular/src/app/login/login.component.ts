import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

//import {SCIMService} from '../service/service.component';
import {SCIMAuthService} from '../service/SCIMAuth.service';
import {Admin} from '../model/admin';

@Component({
    selector: 'login',
    templateUrl: 'login.component.html',
    styleUrls: ['login.component.scss']
})
export class LoginComponent implements OnInit{
    admin:Admin;

    constructor(
        private service:SCIMAuthService,
        private router: Router
    ){}

    ngOnInit(){
        this.admin = new Admin;
    }

    login(event){
        this.service.login(this.admin.puserid,this.admin.ppasswd).subscribe(
        result =>{
            console.log("login-result : ",result)
            if(result.code == "0001"){
                this.router.navigate(["home"]);
            }else{
                // ignore...
            }
        },
        error => {
            console.log("Error", error);
        });
    }
}
