import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import {MenuItem} from 'primeng/api';


@Component({
  selector: 'app-environment',
  templateUrl: './environment.component.html',
  styleUrls: ['./environment.component.css']
})
export class EnvironmentComponent implements OnInit {

  menus: MenuItem[];

  constructor(private router: Router) { }
    
  ngOnInit() {
    this.menus = [
      {label: '관리자 설정'  , routerLink:["/main/environment/admin"]},
      {label: '원장 에이전트',  routerLink:["/main/environment/hr_agent"]},
      {label: '시스템 에이전트',  routerLink:["/main/environment/sys_agent"]},
      {label: '로그 관리'  ,  routerLink:["/main/environment/env_log"]},
    ];
    
  }

  onSelect(event){    
    this.router.navigate([event.value.routerLink[0]]);
  }
}
