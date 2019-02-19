import { Component, OnInit } from '@angular/core';
import {MenuItem} from 'primeng/api';

@Component({
  selector: 'app-tab-menu',
  templateUrl: './tab-menu.component.html',
  styleUrls: ['./tab-menu.component.css']
})
export class TabMenuComponent implements OnInit {

  items: MenuItem[];

  constructor() { }

  ngOnInit() {
    this.items = [   
            {
                label: '원장 관리', routerLink:"/main/hrsystem"
            },         
            {
                label: '시스템 관리', routerLink:"/main/system"
            },
            {
                label: '계정 현황' , routerLink:"/main/account"
            },
            {
                label: '계정 관리',
            },
            {
                label: '계정 분석',
            },
            {
                label: '계정 신청',
            },
            {
                label: '계정 승인',
            },            
            {
                label: '감사 로그', routerLink:"/main/audit"
            },
            {
                label: '환경 설정',
            }
        ];
  }

}
