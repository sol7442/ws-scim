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
        console.log(event);
        console.log(this.admin);
        this.service.login(this.admin.puserid,this.admin.ppasswd).subscribe(
        result =>{
            console.log(result)
            this.router.navigate(["home"]);
        },
        error => {
            console.log("Error", error);
        });
    }
}
