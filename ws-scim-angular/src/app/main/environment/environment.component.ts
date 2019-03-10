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
      {label: '관리자 설정', icon: 'fa fa-fw fa-bar-chart', routerLink:["/main/environment/admin"]},
      {label: '원장 에이전트 설정', icon: 'fa fa-fw fa-calendar', routerLink:["/main/environment/hr_agent"]},
     // {label: '스케줄러 설정', icon: 'fa fa-fw fa-calendar', routerLink:["/main/environment/scheduler"]},
    ];
    
  }

  onSelect(event){    
    this.router.navigate([event.value.routerLink[0]]);
  }
}
