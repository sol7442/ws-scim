import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import {ConfigService} from '../service/config.service';
import {AuthenticationService} from '../service/authentication.service';
import {SCIMAuthService} from '../service/SCIMAuth.service';
import {Admin} from '../model/admin';

import {User} from '../model/model';
import { first } from 'rxjs/operators';

@Component({
    selector: 'login',
    templateUrl: 'login.component.html',
    styleUrls: ['login.component.scss']
})
export class LoginComponent implements OnInit{
    admin:Admin;

    private user:User;

    constructor(
        private config:ConfigService,
        private authenticationService:AuthenticationService,
        private service:SCIMAuthService,
        private router: Router
    ){}

    ngOnInit(){
        this.admin = new Admin;
        this.admin.puserid = "sys-scim-admin";
        this.admin.ppasswd = "pasword!1234";

        this.user = new User;
        this.user.id = "sys-admin";
        this.user.passwd = "pasword!1234";

        this.authenticationService.logout();
    }

    login(event){
        this.authenticationService.login(this.user.id,this.user.passwd)
            .pipe(first())
            .subscribe( result =>{
                console.log("login-user : ", result);
                
                // set titlebar user info //

                this.router.navigate(["/main/hrsystem"]);
            },error =>{
                console.log("login-error : ", error);
            })
    }
}
