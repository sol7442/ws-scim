import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import {SCIMService} from '../service/service.component';
import {Admin} from '../model/admin';

@Component({
    selector: 'login',
    templateUrl: 'login.component.html',
    styleUrls: ['login.component.scss']
})
export class LoginComponent implements OnInit{
    admin:Admin;

    constructor(
        private service:SCIMService,
        private router: Router
    ){}

    ngOnInit(){
        this.admin = new Admin;
    }

    login(event){
        console.log(event);
        console.log(this.admin);
        this.service.post('/auth/login',this.admin).subscribe(
        result =>{
            console.log(result)
            this.router.navigate(["main"]);
        },
        error => {
            console.log("Error", error);
        });
    }
}
