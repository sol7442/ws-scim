import { Component, OnInit } from '@angular/core';
import {MenuItem} from 'primeng/api';


@Component({
  selector: 'app-environment',
  templateUrl: './environment.component.html',
  styleUrls: ['./environment.component.css']
})
export class EnvironmentComponent implements OnInit {

  constructor() { }
  menus: MenuItem[];

  ngOnInit() {
    this.menus = [
      {label: '서버환경', icon: 'fa fa-fw fa-bar-chart', routerLink:["work"]},
      {label: 'XX환경', icon: 'fa fa-fw fa-calendar', routerLink:["account"]},
      {label: '에이전트', icon: 'fa fa-fw fa-book', routerLink:["policy"]},
      {label: '관리자', icon: 'fa fa-fw fa-support', routerLink:["audit"]},
  ];
  }

}
