import { Component, OnInit } from '@angular/core';

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
        private service:SCIMService
    ){}

    ngOnInit(){
        this.admin = new Admin;
    }

    login(event){
        console.log(event);
        console.log(this.admin);
        this.service.login(this.admin);
    }
}
