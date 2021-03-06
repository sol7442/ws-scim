import { Component, OnInit } from '@angular/core';
import {MenuItem} from 'primeng/api';

@Component({
  selector: 'app-basic-menu',
  templateUrl: './basic-menu.component.html',
  styleUrls: ['./basic-menu.component.css']
})
export class BasicMenuComponent implements OnInit {

  items: MenuItem[];

  constructor() { }

  ngOnInit() {
    this.items = [
            {
                label: '계정 현황',
            },
            {
                label: '계정 분석',
            },
            {
                label: '시스템 관리',
            },
            {
                label: '계정 신청',
            },
            {
                label: '계정 승인',
            },            
            {
                label: '감사 로그',
            },
            {
                label: '환경 설정',
            }
        ];
  }

}
