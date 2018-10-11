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
                label: '원장 시스템',
            },
            {
                label: '업무 시스템',
            },
            {
                label: '계정 현황',
            },
            {
                label: '작업 현황',
            },
            {
                label: '계정 정책',
            },
            {
                label: '감사/로그',
            },
            {
                label: '현황/통계',
            },
            {
                label: '환경설정',
            }
        ];
  }

}
